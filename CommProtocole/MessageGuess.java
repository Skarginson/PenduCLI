/* Permet de proposer une lettre

GUESS 
<lettre>

- <lettre> est un caractère alphabétique minuscule.
- Toute proposition invalide ou djéà utilisée est ignorée
- Une proposition valide peut modifier l'état du jeu 
*/

public class MessageGuess {
    private final char letter;

    public MessageGuess(char letter) {
        validate(letter);
        this.letter = letter;
    }

    private static void validate(char letter) {
        if (!Character.isLowerCase(letter) || !Character.isAlphabetic(letter)) {
            throw new IllegalArgumentException("Lettre invalide: " + letter);
        }
    }

    /**
     * Parse un message GUESS (deux lignes) et retourne un MessageGuess.
     * Lance IllegalArgumentException si le message est mal formé.
     */

    public static MessageGuess parse(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message nul");
        }

        String[] lines = message.trim().split("\\R");
        if (lines.length < 2) {
            throw new IllegalArgumentException("Message GUESS incomplet");
        }
        String head = lines[0].trim();
        if (!"GUESS".equals(head)) {
            throw new IllegalArgumentException("En-tête attendu: GUESS, trouvé: " + head);
        }

        String letterPart = lines[1].trim();
        if (letterPart.length() != 1) {
            throw new IllegalArgumentException("Lettre attendue ligne 2");
        }

        char letter = letterPart.charAt(0);
        return new MessageGuess(letter);
    }

    /**
     * Sérialise le message GUESS en texte (deux lignes).
     */

    public String serialize() {
        return String.format("GUESS%n%c%n", letter);
    }

    public char getLetter() {
        return letter;
    }

    @Override
    public String toString() {
        return "MessageGuess[letter=" + letter + "]";
    }

    /** Petit test/demo en ligne de commande.
    public static void main(String[] args) {
        String[] samples = {
            "GUESS \n a",
            "GUESS \n z",
            "GUESS \n 1",
            "BADMESSAGE \n x"
        };

        for (String s : samples) {
            System.out.println("Input: " + s);
            try {
                MessageGuess m = MessageGuess.parse(s);
                System.out.println(" Parsed: " + m);
                System.out.println(" Serialized:\n" + m.serialize());
            } catch (IllegalArgumentException e) {
                System.out.println(" Error: " + e.getMessage());
            }
            System.out.println();
        }
    }
    */
}