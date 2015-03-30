/**
 * Created by dmsuvorov on 10.03.2015.
 */

import java.io.*;
import java.util.Map.Entry;

public class ParserGenerator {
	private static String parserName, lexerName;

	private static Grammar grammar;

	private static void generateNonTerminalParsingSubroutine(
			PrintWriter outputStream, String name, String type) {
		outputStream.print("\t" + type + " " + name + "() {\n" +
		                   "\t\t" + type + " result;\n");
		
		outputStream.print("\t\treturn result;\n" +
		                   "\t}\n\n");
	}

	private static void generateSource() throws IOException {
		try (PrintWriter outputStream = new PrintWriter(new FileWriter("src/"
				+ parserName + ".java"))) {
			outputStream.print("import java.io.InputStream;\n"
					+ "import java.text.ParseException;\n\n" + "class "
					+ parserName + "{\n" + "\t" + lexerName + " lexer;\n\n");
			outputStream.print("\t" + grammar.nonTerminals.get(grammar.start)
					+ " parse(InputStream is)" + " throws ParseException "
					+ "{\n" + "\t\tlexer = new " + lexerName + "(is);\n"
					+ "\t\tlexer.nextToken();\n" + "\t\treturn "
					+ grammar.start + "();\n" + "\t}\n\n");

			for (Entry<String, String> entry : grammar.nonTerminals.entrySet()) {
				generateNonTerminalParsingSubroutine(outputStream, entry.getKey(), entry.getValue());
			}

			outputStream.println("}\n");
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out
					.println("Usage: java ParserGenerator <input-file-with-grammar>");
			return;
		}
		String filename = args[0];
		InputStream is = new FileInputStream(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		parserName = br.readLine();
		lexerName = br.readLine();
		IOUtils.expectDelimiter(br, IOUtils.DEFAULT_DELIMITER);

		grammar = new Grammar(br);

		br.close();
		is.close();

		generateSource();
	}
}
