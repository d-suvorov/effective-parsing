import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

/**
 * Created by dmsuvorov on 10.03.2015.
 */
public class Test {
    public static void main(String[] args) throws ParseException, IOException {
    	String filename = "expr.grammar";
		InputStream is = new FileInputStream(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		br.readLine();
		br.readLine();
		IOUtils.expectDelimiter(br, IOUtils.DEFAULT_DELIMITER);
		
		Grammar grammar = new Grammar(br);
		
		System.out.println(grammar.start);
    }
}
