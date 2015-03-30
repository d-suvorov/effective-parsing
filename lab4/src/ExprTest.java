import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

/**
 * Created by dmsuvorov on 10.03.2015.
 */
public class ExprTest {
	public static double eval(String input) throws ParseException {
		InputStream is = new ByteArrayInputStream(input.getBytes());
		ExprParser parser = new ExprParser();
		return parser.parse(is);
	}
	
	public static void main(String[] args) throws ParseException, IOException {
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);

		String curLine = "";
		while (!(curLine.equals("quit"))) {
			curLine = in.readLine();
			if (!(curLine.equals("quit"))) {
				System.out.println(eval(curLine));
			}
		}
	}
}
