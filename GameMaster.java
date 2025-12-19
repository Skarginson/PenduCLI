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

        // Etape 2 : Ecoute sur le port 2025
        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(2025)) {
            System.out.println("PlayerDisplay en attente de messages sur le port 2025");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    // Lire la première ligne et vérifier qu'elle est HELLO OU GUESS
                    String firstLine = in.readLine();
                    if (firstLine == null || (!firstLine.equals("HELLO") && !firstLine.equals("GUESS"))) {
                        System.err.println("Erreur : première ligne doit être HELLO ou GUESS");
                        continue; 
                    }

                    if (firstLine.equals("HELLO")) {
                        String ipLine = in.readLine();
                        String portLine = in.readLine();

                        if (ipLine == null) {
                            System.err.println("Erreur : impossible de lire la ligne IP du message HELLO");
                            continue;
                        }
                        if (portLine == null) {
                            System.err.println("Erreur : impossible de lire la ligne Port du message HELLO");
                            continue;
                        }
                        
                        String helloMessageStr = String.format("HELLO%n%s%n%s%n", ipLine, portLine);
                        try {
                            HelloMessage helloMessage = HelloMessage.parse(helloMessageStr);
                            System.out.println("Reçu HELLO de " + helloMessageStr.replaceAll("\\R", " | "));
                        } catch (IllegalArgumentException e) {
                            System.err.println("Erreur lors du parsing du message HELLO : " + e.getMessage());
                        }
                    }

                    if (firstLine.equals("GUESS")) {
                        String letterline = in.readLine();
                        if (letterline == null) {
                            System.err.println("Erreur : impossible de lire la ligne Lettre du message GUESS");
                        }
                    try {
                        System.out.println("Reçu GUESS de " + letterline);
                    } catch (Exception e) {
                        System.err.println("Erreur lors du parsing du message GUESS : " + e.getMessage());
                    }
                    }

                } catch (IOException e) {
                    System.err.println("Erreur lors de la réception du message : " + e.getMessage());
                }
}

        } catch (IOException e) {
            System.err.println("Erreur lors de la création du ServerSocket : " + e.getMessage());
        }

    }
}
