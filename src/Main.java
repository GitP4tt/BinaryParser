import java.io.IOException;
import java.io.StringReader;

public class Main {

    public static void main(String[] args) throws IOException {

        String bn = ".10111010100101";

        BinaryParser bp = new BinaryParser();
        bp.input = new StringReader(bn);

        bp.parse();

    }
}
