/**
 * Created by dmsuvorov on 10.03.2015.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class ParserGenerator {
	private static String parserName, lexerName, tokenName;

	private static Grammar grammar;
	
	private static String tokenQualifiedName(String name) {
		return tokenName + "." + name;
	}

	private static void generateNonTerminalParsingSubroutine(
			PrintWriter outputStream, String nonTermName, String nonTermType) {
		outputStream.print("\t" + nonTermType + " " + nonTermName + "() throws ParseException {\n" +
				           "\t\t" + tokenName + " curToken = lexer.curToken();\n");
		
		for (RightPart right : grammar.grammar.get(nonTermName)) {
			Set<String> next = right.isEpsilon() ? grammar.getFollow(nonTermName) : grammar.getFirst(right);
			outputStream.print("\t\tif (");
			Iterator<String> it = next.iterator();
			if (it.hasNext()) {
				String s = it.next();
				outputStream.print("curToken == " + tokenQualifiedName(s));
			}
			while (it.hasNext()) {
				String s = it.next();
				outputStream.print(" ||\n\t\t    curToken == " + tokenQualifiedName(s));
			}
			outputStream.print(") {\n");
			
			if (!right.isEpsilon()) {
				for (int i = 0; i < right.right.size(); i++) {
					Symbol symbol = right.right.get(i);
					String name = symbol.name;
					String attrName = "attr" + i;
					if (symbol.type == SymbolType.TERMINAL) {
						if (name.isEmpty()) {
							throw new AssertionError();
						}
						outputStream.print("\t\t\tif (lexer.curToken != " + tokenQualifiedName(name) + ") {\n" +
					                       "\t\t\t\tthrow new ParseException(\"Expected " + name +
	                                       " at position\" , lexer.curPos());\n" +
								           "\t\t\t}\n");
						if (right.action.contains(attrName)) {
							String type = grammar.terminals.get(name);
							outputStream.println("\t\t\t" + type + " " + attrName + " = lexer.getAttribute();");
						}
						outputStream.println("\t\t\tlexer.nextToken();");
					} else {
						String type = grammar.nonTerminals.get(name);
						outputStream.print("\t\t\t" + type + " " + attrName + " = " + name + "();\n");
					}
				}
			}
			
			outputStream.print("\t\t\t" + right.action + "\n");
			outputStream.print("\t\t}\n");
		}
		
		outputStream.print("\t\tthrow new AssertionError();\n" +
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

			outputStream.println("}");
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
		tokenName = br.readLine();
		IOUtils.expectDelimiter(br, IOUtils.DEFAULT_DELIMITER);

		grammar = new Grammar(br);

		br.close();
		is.close();

		generateSource();
	}
}
