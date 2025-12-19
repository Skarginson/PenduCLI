import java.io.*;
import java.net.Socket;

public class PlayerDisplay {

    private static GameState gameState;

    public static void main(String[] args) {

        // Etape 1 : Port d'écoute en argument qui doit être différent de 2025 + vérification. 
        if (args.length < 1) {
            System.err.println("Usage: java PlayerDisplay <Port>");
            System.exit(1);
        }

        int playerDisplayPort = Integer.parseInt(args[0]);
        if (playerDisplayPort == 2025) {
            System.err.println("Erreur : Le port du PlayerDisplay doit être différent de 2025.");
            System.exit(1);
        }

        if (playerDisplayPort < 0 || playerDisplayPort > 65535) {
            System.err.println("Erreur : Le port doit être entre 0 et 65535.");
            System.exit(1);
        }

        // Etape 2 : Ecoute passivement les connexion TCP entrantes sur le port d'écoute. 
        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(playerDisplayPort)) {
            System.out.println("PlayerDisplay en attente de messages sur le port " + playerDisplayPort);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String message = in.readLine();
                    if (message != null) {
                        System.out.println("Message reçu du GameMaster : " + message);
                    }

                    if (gameState != null && (gameState.getStatus() == GameState.Status.WIN || gameState.getStatus() == GameState.Status.LOSE)) {
                        System.out.println("Fin du programme PlayerDisplay.");
                        System.exit(0);
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