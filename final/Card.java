import java.util.regex.Pattern;

public class Card {
    public static Pattern pattern() {
        // create and return your Pattern here
        return Pattern.compile("([2-9]|10|ace|jack|queen|king) of (hearts|diamonds|spades|clubs)", Pattern.CASE_INSENSITIVE);
    }
}