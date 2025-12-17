/**
 * Après chaque modification de l'état du jeu, le GameMaster envoie l'état complet du jeu à tous les PlayerDisplay enregistrés, un à un. 
 * 
 * DISPLAY
 * <mot_masque>
 * <lettres_proposees>
 * <nb_erreurs>
 * <etat_partie>
 * 
 * - <mot_masque> : état du mot (exemple : _ _ a _ _). 
 * - <lettres_proposees> : lettre déjà proposées séprées par des espaces. 
 * - <nb_erreurs> : nombre d'erreurs commises. 
 * - <etat_partie> : PLAYING, WIN, LOSE. 
 * 
 * Chaque diffusion utilise une nouvelle connexion TCP. 
 */

public class MessageDisplay {
    private final String maskedWord;
    private final String proposedLetters;
    private final int errorCount;
    private final String gameState;

    public enum GameState {
        PLAYING, WIN, LOSE
    }

    public MessageDisplay(String maskedWord, String proposedLetters, int errorCount, GameState gameState) {
        validate(maskedWord, proposedLetters, errorCount, gameState);
        this.maskedWord = maskedWord;
        this.proposedLetters = proposedLetters;
        this.errorCount = errorCount;
        this.gameState = gameState.toString();
    }

    private static void validate(String maskedWord, String proposedLetters, int errorCount, GameState gameState) {
        if (maskedWord == null || maskedWord.isEmpty()) {
            throw new IllegalArgumentException("Mot masqué vide");
        }
        if (proposedLetters == null) {
            throw new IllegalArgumentException("Lettres proposées null");
        }
        if (errorCount < 0) {
            throw new IllegalArgumentException("Nombre d'erreurs négatif: " + errorCount);
        }
        if (gameState == null) {
            throw new IllegalArgumentException("État du jeu null");
        }
    }

    /**
     * Parse un message DISPLAY (5 lignes) et retourne un MessageDisplay.
     */
    public static MessageDisplay parse(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message nul");
        }

        String[] lines = message.trim().split("\\R");
        if (lines.length < 5) {
            throw new IllegalArgumentException("Message DISPLAY incomplet (attendu 5 lignes)");
        }

        String head = lines[0].trim();
        if (!"DISPLAY".equals(head)) {
            throw new IllegalArgumentException("En-tête attendu: DISPLAY, trouvé: " + head);
        }

        String maskedWord = lines[1].trim();
        String proposedLetters = lines[2].trim();
        String errorStr = lines[3].trim();
        String stateStr = lines[4].trim();

        if (maskedWord.isEmpty()) {
            throw new IllegalArgumentException("Mot masqué vide");
        }

        int errorCount;
        try {
            errorCount = Integer.parseInt(errorStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Nombre d'erreurs non-numérique: " + errorStr, e);
        }

        GameState gameState;
        try {
            gameState = GameState.valueOf(stateStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("État du jeu invalide: " + stateStr, e);
        }

        return new MessageDisplay(maskedWord, proposedLetters, errorCount, gameState);
    }

    /**
     * Sérialise le message DISPLAY en texte (5 lignes).
     */
    public String serialize() {
        return String.format("DISPLAY%n%s%n%s%n%d%n%s%n", maskedWord, proposedLetters, errorCount, gameState);
    }

    public String getMaskedWord() {
        return maskedWord;
    }

    public String getProposedLetters() {
        return proposedLetters;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public String getGameState() {
        return gameState;
    }

    @Override
    public String toString() {
        return "MessageDisplay[masked=" + maskedWord + ", proposed=" + proposedLetters
                + ", errors=" + errorCount + ", state=" + gameState + "]";
    }

    /** Petit test/demo en ligne de commande.
    public static void main(String[] args) {
        String sample = "DISPLAY\n_ _ a _ _\na e\n2\nPLAYING\n";
        System.out.println("Sample message:\n" + sample);
        try {
            MessageDisplay m = MessageDisplay.parse(sample);
            System.out.println("Parsed: " + m);
            System.out.println("Serialized:\n" + m.serialize());
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur: " + e.getMessage());
        }

        // Test WIN state
        System.out.println("\n--- Test WIN state ---");
        String winSample = "DISPLAY\na b c\na b c\n0\nWIN\n";
        try {
            MessageDisplay m = MessageDisplay.parse(winSample);
            System.out.println("Parsed: " + m);
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }
     */
}

