package utils;

public class GameState {

    public enum Status {
        PLAYING,
        WIN,
        LOSE
    }

    private final String secretWord;
    private String maskedWord;
    private String lettersProposed; // lettres séparées par des espaces
    private int errors;
    private Status status;

    public GameState(String secretWord) {
        this.secretWord = secretWord;
        StringBuilder sb = new StringBuilder(secretWord.length());
        for (int i = 0; i < secretWord.length(); i++) {
            sb.append('_');
        }
        this.maskedWord = sb.toString();
        this.lettersProposed = "";
        this.errors = 0;
        this.status = Status.PLAYING;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public String getMaskedWord() {
        return maskedWord;
    }

    public String getLettersProposed() {
        return lettersProposed;
    }

    public int getErrors() {
        return errors;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isFinished() {
        return status != Status.PLAYING;
    }

    public boolean hasLetterBeenProposed(char letter) {
        return lettersProposed.contains(String.valueOf(letter));
    }

    public void applyGuess(char letter) {

        // Ignorer si déjà utilisé ou partie finie
        if (hasLetterBeenProposed(letter) || status != Status.PLAYING) {
            return;
        }

        // Ajout de la lettre proposée
        if (lettersProposed.isEmpty()) {
            lettersProposed = String.valueOf(letter);
        } else {
            lettersProposed += " " + letter;
        }

        // Vérifier si la lettre est dans le mot
        boolean found = false;
        char[] maskedArray = maskedWord.toCharArray();

        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == letter) {
                maskedArray[i] = letter;
                found = true;
            }
        }

        maskedWord = new String(maskedArray);

        // Mise à jour erreur / victoire / défaite
        if (!found) {
            errors++;
            if (errors >= 8) {
                status = Status.LOSE;
            }
        } else {
            if (!maskedWord.contains("_")) {
                status = Status.WIN;
            }
        }
    }
}
