import java.util.Dictionary;
import java.util.Hashtable;

public class PasswordGenerator {

    // from generator class
    static String key = KeyGenerator.key;

    // indexes of transformers
    static int shuffle_index = key.indexOf("#"); // to determine shuffling style
    static int extract_index = key.indexOf("@"); // to determine which one to extract out of 4

    // I just realize the other transformer '$' does nothing.

    static private String plainKey() {
        /* the plain key without transformers and numbers */
        return key.replaceAll("[@#$0-9]", "");
    }

    static private String shuffleMethod() {
        // change the position of plainKey by shuffling
        String shuffledKey;
        String plainKey = plainKey();
        System.out.println("before shuffling: " + plainKey);
        StringBuilder sb = new StringBuilder();

        // all shuffle styles
        int[][] styles = {
                {0, 1, 3, 2},
                {1, 0, 3, 2},
                {2, 1, 0, 3},
                {0, 3, 1, 2},
                {1, 0, 2, 3},
                {3, 0, 2, 1},
                {3, 2, 0, 1},
                {0, 2, 3, 1}
        };

        int[] chosenMethod = styles[shuffle_index];
        System.out.println("Shuffle method number: " + shuffle_index);
        for (int i = 0; i < 4; i++) {
            sb.append(plainKey.charAt(chosenMethod[i]));
        }

        shuffledKey = sb.toString();
        return shuffledKey;
    }

    private static String extractMethod(String shuffledKey) {
        /* Extract where @ lies in the key, according to the index, extract the @th position of encrypted value, and put them all behind and reconstructed */
        // Encryption keys, value
        Dictionary<Character, String> encryptKeys = new Hashtable<>();

        // Populate the dictionary with key-value pairs
        encryptKeys.put('a', "u3Y$");
        encryptKeys.put('b', "+b0P");
        encryptKeys.put('c', "~Cp6");
        encryptKeys.put('d', "O^d8");
        encryptKeys.put('e', "^e5B");
        encryptKeys.put('f', "Rw4~");
        encryptKeys.put('g', "n2N^");
        encryptKeys.put('h', "c@T8");
        encryptKeys.put('i', "_dG6");
        encryptKeys.put('j', "9G&o");
        encryptKeys.put('k', "w+W3");
        encryptKeys.put('l', "Gx@4");
        encryptKeys.put('m', "L4b@");
        encryptKeys.put('n', "+3zI");
        encryptKeys.put('o', "O6r!");
        encryptKeys.put('p', "D2p#");
        encryptKeys.put('q', "Wg&3");
        encryptKeys.put('r', "3Jj&");
        encryptKeys.put('s', "E6&e");
        encryptKeys.put('t', "X~3g");
        encryptKeys.put('u', "^eO6");
        encryptKeys.put('v', "J4+o");
        encryptKeys.put('w', "h@6K");
        encryptKeys.put('x', "q#W4");
        encryptKeys.put('y', "Bm5^");
        encryptKeys.put('z', "+3Ip");

        int chosenExtractIndex = 0;
        if (extract_index == 2 || extract_index == 3) {
            chosenExtractIndex = 1;
        } else if (extract_index == 4 || extract_index == 5) {
            chosenExtractIndex = 2;
        } else if (extract_index == 6 || extract_index == 7) {
            chosenExtractIndex = 3;
        }

        System.out.println(chosenExtractIndex);

        String en1 = encryptKeys.get(shuffledKey.charAt(0));
        String en2 = encryptKeys.get(shuffledKey.charAt(1));
        String en3 = encryptKeys.get(shuffledKey.charAt(2));
        String en4 = encryptKeys.get(shuffledKey.charAt(3));
        System.out.println("Before: " + en1 + en2 + en3 + en4);

        String ex1 = String.valueOf(en1.charAt(chosenExtractIndex));
        String ex2 = String.valueOf(en2.charAt(chosenExtractIndex));
        String ex3 = String.valueOf(en3.charAt(chosenExtractIndex));
        String ex4 = String.valueOf(en4.charAt(chosenExtractIndex));

        en1 = en1.replace(ex1, "");
        en2 = en2.replace(ex2, "");
        en3 = en3.replace(ex3, "");
        en4 = en4.replace(ex4, "");

        String extractedString = ex1 + ex2 + ex3 + ex4;
        String reConstructedString = en1 + en2 + en3 + en4 + extractedString;

        System.out.println("reConstructedString: " + reConstructedString);
        return reConstructedString;
    }

    private static String inversionMethod(String extractedString) {
        /* flip all the encrypted password with their counterparts according to the under two Dictionary */
        // inversion key/value
        Dictionary<Character, Character> characterKeys = new Hashtable<>();
        Dictionary<Character, Character> numberKeys = new Hashtable<>();

        characterKeys.put('~', '1');
        characterKeys.put('!', '2');
        characterKeys.put('@', '3');
        characterKeys.put('#', '4');
        characterKeys.put('$', '5');
        characterKeys.put('^', '6');
        characterKeys.put('&', '7');
        characterKeys.put('*', '8');
        characterKeys.put('_', '9');
        characterKeys.put('+', '0');

        numberKeys.put('1', '~');
        numberKeys.put('2', '!');
        numberKeys.put('3', '@');
        numberKeys.put('4', '#');
        numberKeys.put('5', '$');
        numberKeys.put('6', '^');
        numberKeys.put('7', '&');
        numberKeys.put('8', '*');
        numberKeys.put('9', '_');
        numberKeys.put('0', '+');

        System.out.println("Before: " + extractedString);

        String invertedString;

        StringBuilder sb = new StringBuilder();

        for (char c : extractedString.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(numberKeys.get(c));
            } else if (Character.isLowerCase(c)) {
                sb.append(Character.toUpperCase(c));
            } else if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(characterKeys.get(c));
            }
        }

        invertedString = sb.toString();

        return invertedString;
    }

    public static String getPassCode () {
        String passcode;
        String shuffledString = shuffleMethod();
        System.out.println("After shuffling: " + shuffledString);
        String extractedString = extractMethod(shuffledString);
        String invertedString = inversionMethod(extractedString);
        System.out.println("inverted: " + invertedString);
        passcode = invertedString;
        return passcode;
    }
}
