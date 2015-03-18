/**
 * Created by dmsuvorov on 10.03.2015.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ParserGenerator {
	private static String parserName, lexerName;
	
	private static Grammar grammar;
	
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage: java ParserGenerator <input-file-with-grammar>");
		}
		String filename = args[0];
		InputStream is = new FileInputStream(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		parserName = br.readLine();
		lexerName = br.readLine();
		IOUtils.expectDelimiter(br, IOUtils.DEFAULT_DELIMITER);
		
		grammar = new Grammar(br);
	}
}
