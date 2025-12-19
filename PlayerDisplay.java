import java.io.*;
import java.net.Socket;

public class PlayerDisplay {

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

                    // Lire la première ligne et vérifier qu'elle est DISPLAY
                    String firstLine = in.readLine();
                    if (firstLine == null || !firstLine.equals("DISPLAY")) {
                        System.err.println("Erreur : première ligne doit être DISPLAY");
                        continue;
                    }

                    // Lire exactement 4 lignes supplémentaires
                    String maskedWord = in.readLine(); 
                    String lettersProposed = in.readLine();
                    String errorsStr = in.readLine();
                    String statusStr = in.readLine();

                    if (maskedWord == null || lettersProposed == null || errorsStr == null || statusStr == null) {
                        System.err.println("Erreur : impossible de lire les 4 lignes du message");
                        continue;
                    }

                    // Afficher l'état du jeu en console
                    System.out.println("=== État du jeu ===");
                    System.out.println("Mot masqué : " + maskedWord);
                    System.out.println("Lettres proposées : " + lettersProposed);
                    System.out.println("Erreurs : " + errorsStr);
                    System.out.println("Statut : " + statusStr);
                    System.out.println("==================");

                    // Vérifier si le jeu est terminé, annoncer le résultat et terminer le programme. 
                    if (statusStr.equals("WIN")) {
                        System.out.println("Partie gagnée, félicitations ! ");
                        System.out.println("Fin du PlayerDisplay.");
                        System.exit(0);
                    }
                    if (statusStr.equals("LOSE")) {
                        System.out.println("Partie perdue, dommage ! ");
                        System.out.println("Fin du PlayerDisplay.");
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