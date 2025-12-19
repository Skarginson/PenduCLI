import java.io.*;

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

            // while (true) {
            //     try (Socket clientSocket = serverSocket.accept();
            //          BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            //         // Lire la première ligne et vérifier qu'elle est DISPLAY
            //         String firstLine = in.readLine();
            //         if (firstLine == null || !firstLine.equals("DISPLAY")) {
            //             System.err.println("Erreur : première ligne doit être DISPLAY");
            //             continue;
            //         }

            //         // Lire exactement 4 lignes supplémentaires
            //         String maskedWord = in.readLine(); 
            //         String lettersProposed = in.readLine();
            //         String errorsStr = in.readLine();
            //         String statusStr = in.readLine();

            //         if (maskedWord == null || lettersProposed == null || errorsStr == null || statusStr == null) {
            //             System.err.println("Erreur : impossible de lire les 4 lignes du message");
            //             continue;
            //         }

            //         // Afficher l'état du jeu en console
            //         System.out.println("=== État du jeu ===");
            //         System.out.println("Mot masqué : " + maskedWord);
            //         System.out.println("Lettres proposées : " + lettersProposed);
            //         System.out.println("Erreurs : " + errorsStr);
            //         System.out.println("Statut : " + statusStr);
            //         System.out.println("==================");

            //         // Vérifier si le jeu est terminé, annoncer le résultat et terminer le programme. 
            //         if (statusStr.equals("WIN")) {
            //             System.out.println("Partie gagnée, félicitations ! ");
            //             System.out.println("Fin du PlayerDisplay.");
            //             System.exit(0);
            //         }
            //         if (statusStr.equals("LOSE")) {
            //             System.out.println("Partie perdue, dommage ! ");
            //             System.out.println("Fin du PlayerDisplay.");
            //             System.exit(0);
            //         }

            //     } catch (IOException e) {
            //         System.err.println("Erreur lors de la réception du message : " + e.getMessage());
            //     }
            // }

        } catch (IOException e) {
            System.err.println("Erreur lors de la création du ServerSocket : " + e.getMessage());
        }

    }
}