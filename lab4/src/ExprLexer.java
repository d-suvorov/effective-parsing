/**
 * Created by dmsuvorov on 10.03.2015.
 */

import java.io.InputStream;
import java.text.ParseException;

enum ExprToken {
    NUMBER,
    PLUS, MINUS, MUL, DIV,
    LPAREN, RPAREN,
    END
}

public class ExprLexer extends Lexer<ExprToken> {
    public ExprLexer(InputStream is) throws ParseException {
		super(is);
	}

	private int curNumber;

    public int getCurNumber() {
        return curNumber;
    }

    public int getAttribute() {
        return curNumber;
    }

    @Override
    public void nextToken() throws ParseException {
        while (Character.isWhitespace(curChar)) {
            nextChar();
        }
        if (Character.isDigit(curChar)) {
        	curNumber = 0;
            do {
            	curNumber = curNumber * 10 + Character.digit(curChar, 10);
            	nextChar();
            } while (Character.isDigit(curChar));
            curToken = ExprToken.NUMBER;
            return;
        }
        switch (curChar) {
            case '+':
                nextChar();
                curToken = ExprToken.PLUS;
                break;
            case '-':
                nextChar();
                curToken = ExprToken.MINUS;
                break;
            case '*':
                nextChar();
                curToken = ExprToken.MUL;
                break;
            case '/':
                nextChar();
                curToken = ExprToken.DIV;
                break;
            case '(':
                nextChar();
                curToken = ExprToken.LPAREN;
                break;
            case ')':
                nextChar();
                curToken = ExprToken.RPAREN;
                break;
            case -1:
                curToken = ExprToken.END;
                break;
            default:
                throw new ParseException("Illegal character" + (char) curChar, curPos);
        }
    }

}
