import java.io.FileNotFoundException;
import java.text.ParseException;

/**
 * Created by dmsuvorov on 10.03.2015.
 */
public class LexerTest {
    public static void main(String[] args) throws ParseException, FileNotFoundException {
    	Lexer<ExprToken> exprLexer = new ExprLexer(System.in);
    	ExprToken token;
    	do {
    		exprLexer.nextToken();
    		token = exprLexer.curToken();
    		System.out.println(token + " ");
    	} while (token != ExprToken.END);
    }
}
