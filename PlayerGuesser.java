import java.io.*;
import java.net.Socket;

public class PlayerGuesser {

    private static final int GAMEMASTER_PORT = 2025;

    public static void main(String[] args) {

        if (args.length != 3) {
            System.err.println("Usage: java PlayerGuesser <IP_GameMaster> <IP_PlayerDisplay> <Port_PlayerDisplay>");
            System.exit(1);
        }

        String gameMasterIP = args[0];
        String playerDisplayIP = args[1];
        String playerDisplayPort = args[2];

        // Étape 2 — Envoyer le message HELLO automatiquement
        sendHello(gameMasterIP, playerDisplayIP, playerDisplayPort);

        // Étape 3 — Lire continuellement l'entrée utilisateur
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String line = stdin.readLine();
                if (line == null)
                    continue;

                // Étape 4 — Vérification : une seule lettre minuscule OU "_"
                if (!line.matches("^[a-z_]$")) {
                    System.err.println("Erreur : veuillez entrer une seule lettre minuscule (ou '_').");
                    continue;
                }

                // Étape 8 — Si "_" → fin du programme
                if (line.equals("_")) {
                    System.out.println("Fin du programme PlayerGuesser.");
                    System.exit(0);
                }

                // Étape 5 — Envoyer le message GUESS
                boolean success = sendGuess(gameMasterIP, line.charAt(0));

                if (!success) {
                    System.err.println("Erreur lors de l'envoi du message GUESS.");
                }

                // Étape 6 — Si succès → retourner à l’étape 3
                // rien de particulier à faire ici

            } catch (IOException e) {
                System.err.println("Erreur lecture STDIN : " + e.getMessage());
            }
        }
    }

    private static void sendHello(String gameMasterIP, String playerDisplayIP, String playerDisplayPort) {
        try (Socket socket = new Socket(gameMasterIP, GAMEMASTER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("HELLO");
            out.println(playerDisplayIP);
            out.println(playerDisplayPort);

        } catch (IOException e) {
            System.err.println("Erreur lors de l’envoi du HELLO : " + e.getMessage());
            System.exit(1);
        }
    }

    private static boolean sendGuess(String gameMasterIP, char letter) {
        try (Socket socket = new Socket(gameMasterIP, GAMEMASTER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("GUESS");
            out.println(letter);

            return true;

        } catch (IOException e) {
            return false;
        }
    }
}
