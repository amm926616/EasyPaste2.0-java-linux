import java.util.*;

public class KeyGenerator {
    static String key = shuffleKey(generateKey());

    public static String generateKey() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // appending one number
        int randomNumber = random.nextInt(10);
        sb.append(randomNumber);


        // append random 4 characters out of a to z
        for (int i = 0; i < 4; i++) {
            char randomLetter = (char) (random.nextInt(26) + 'a');
            sb.append(randomLetter);
        }

        // append 3 transformers
        char[] specialCharacters = {'@', '#', '$'};
        sb.append(specialCharacters);

        return sb.toString();
    }

    // method to shuffle the string
    public static String shuffleKey(String input) {
        List<Character> characters = new ArrayList<>();

        for (char c : input.toCharArray()) {
            characters.add(c);
        }

        Collections.shuffle(characters);

        StringBuilder sb = new StringBuilder();
        for (char c : characters) {
            sb.append(c);
        }

        return sb.toString();
    }
}
