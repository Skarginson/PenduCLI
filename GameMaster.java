import java.io.*;
import java.net.Socket;

public class GameMaster {

    public static void main(String[] args) {

        // Etape 1 : Argument doit être le mot à deviner, sans accent + en minuscule
        if (args.length < 1) {
            System.err.println("Usage: java GameMaster <Mot a deviner>");
            System.exit(1);
        }
        
        String wordToGuess = args[0];
        if (!wordToGuess.matches("[a-z]+")) {
            System.err.println("Erreur : Le mot à deviner doit être en minuscules et sans accents.");
            System.exit(1);
        }

        // Étape 3 : Maintenir l'état du jeu et la liste des PlayerDisplay connus
        GameState gameState = new GameState(wordToGuess);
        KnownDisplays knownDisplays = new KnownDisplays();

        // Étape 2 : Écoute sur le port 2025
        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(2025)) {
            System.out.println("GameMaster en attente de messages sur le port 2025");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    // Lire la première ligne et vérifier qu'elle est HELLO OU GUESS
                    String firstLine = in.readLine();
                    if (firstLine == null) {
                        System.err.println("Erreur : impossible de lire le premier message");
                        continue;
                    }

                    switch (firstLine) {
                        case "HELLO" -> handleHello(in, knownDisplays, gameState);
                        case "GUESS" -> {
                            handleGuess(in, gameState, knownDisplays);
                            
                            // Terminer proprement si le jeu est fini
                            if (gameState.isFinished()) {
                                System.out.println("Partie terminée !");
                                if (gameState.getStatus() == GameState.Status.WIN) {
                                    System.out.println("Victoire !");
                                } else {
                                    System.out.println("Défaite !");
                                }
                                return;
                            }
                        }
                        default -> System.err.println("Erreur : première ligne doit être HELLO ou GUESS, trouvée: " + firstLine);
                    }

                } catch (IOException e) {
                    System.err.println("Erreur lors de la réception du message : " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la création du ServerSocket : " + e.getMessage());
        }
    }

    private static void handleHello(BufferedReader in, KnownDisplays knownDisplays, GameState gameState) throws IOException {
        try {
            String ipLine = in.readLine();
            String portLine = in.readLine();

            if (ipLine == null || portLine == null) {
                System.err.println("Erreur : message HELLO incomplet");
                return;
            }

            String helloMessageStr = String.format("HELLO%n%s%n%s%n", ipLine, portLine);
            HelloMessage helloMessage = HelloMessage.parse(helloMessageStr);
            
            knownDisplays.addDisplay(helloMessage.getIp(), helloMessage.getPort());
            System.out.println("Enregistré PlayerDisplay : " + helloMessage.getIp() + ":" + helloMessage.getPort());
            
            // Envoyer l'état actuel du jeu au nouveau PlayerDisplay
            sendDisplayToClient(helloMessage.getIp(), helloMessage.getPort(), gameState);

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur lors du parsing du message HELLO : " + e.getMessage());
        }
    }

    private static void handleGuess(BufferedReader in, GameState gameState, KnownDisplays knownDisplays) throws IOException {
        try {
            String letterLine = in.readLine();

            if (letterLine == null) {
                System.err.println("Erreur : message GUESS incomplet");
                return;
            }

            String guessMessageStr = String.format("GUESS%n%s%n", letterLine);
            GuessMessage guessMessage = GuessMessage.parse(guessMessageStr);
            
            gameState.applyGuess(guessMessage.getLetter());
            System.out.println("Reçu GUESS : " + guessMessage.getLetter());
            
            // Envoyer l'état mis à jour à tous les PlayerDisplay
            broadcastDisplayToAllClients(knownDisplays, gameState);

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur lors du parsing du message GUESS : " + e.getMessage());
        }
    }

    private static void broadcastDisplayToAllClients(KnownDisplays knownDisplays, GameState gameState) {
        for (KnownDisplays.DisplayInfo display : knownDisplays.getDisplays()) {
            sendDisplayToClient(display.ip, display.port, gameState);
        }
    }

    private static void sendDisplayToClient(String ip, int port, GameState gameState) {
        try (Socket socket = new Socket(ip, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String proposedLetters = gameState.getLettersProposed();
            DisplayMessage displayMessage = new DisplayMessage(
                gameState.getMaskedWord(),
                proposedLetters.isEmpty() ? "" : proposedLetters,
                gameState.getErrors(),
                DisplayMessage.GameState.valueOf(gameState.getStatus().toString())
            );

            out.print(displayMessage.serialize());
            out.flush();

        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi de DISPLAY à " + ip + ":" + port + " - " + e.getMessage());
        }
    }
}
