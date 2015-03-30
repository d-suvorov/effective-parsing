import java.io.InputStream;
import java.text.ParseException;

class ExprParser{
	ExprLexer lexer;

	double parse(InputStream is) throws ParseException {
		lexer = new ExprLexer(is);
		lexer.nextToken();
		return E();
	}

	double EPrime() throws ParseException {
		ExprToken curToken = lexer.curToken();
		if (curToken == ExprToken.PLUS) {
			if (lexer.curToken != ExprToken.PLUS) {
				throw new ParseException("Expected PLUS at position" , lexer.curPos());
			}
			lexer.nextToken();
			double attr1 = T();
			double attr2 = EPrime();
			return +(attr1 + attr2);
		}
		if (curToken == ExprToken.MINUS) {
			if (lexer.curToken != ExprToken.MINUS) {
				throw new ParseException("Expected MINUS at position" , lexer.curPos());
			}
			lexer.nextToken();
			double attr1 = T();
			double attr2 = EPrime();
			return -(attr1 + attr2);
		}
		if (curToken == ExprToken.END ||
		    curToken == ExprToken.RPAREN) {
			if (lexer.curToken != ExprToken.) {
				throw new ParseException("Expected  at position" , lexer.curPos());
			}
			lexer.nextToken();
			return 0;
		}
		throw new AssertionError();
	}

	double T() throws ParseException {
		ExprToken curToken = lexer.curToken();
		if (curToken == ExprToken.LPAREN ||
		    curToken == ExprToken.MINUS ||
		    curToken == ExprToken.NUMBER) {
			double attr0 = F();
			double attr1 = TPrime();
			return attr0 * attr1;
		}
		throw new AssertionError();
	}

	double E() throws ParseException {
		ExprToken curToken = lexer.curToken();
		if (curToken == ExprToken.LPAREN ||
		    curToken == ExprToken.MINUS ||
		    curToken == ExprToken.NUMBER) {
			double attr0 = T();
			double attr1 = EPrime();
			return attr0 + attr1;
		}
		throw new AssertionError();
	}

	double F() throws ParseException {
		ExprToken curToken = lexer.curToken();
		if (curToken == ExprToken.NUMBER) {
			if (lexer.curToken != ExprToken.NUMBER) {
				throw new ParseException("Expected NUMBER at position" , lexer.curPos());
			}
			double attr0 = lexer.getAttribute();
			lexer.nextToken();
			return attr0;
		}
		if (curToken == ExprToken.LPAREN) {
			if (lexer.curToken != ExprToken.LPAREN) {
				throw new ParseException("Expected LPAREN at position" , lexer.curPos());
			}
			lexer.nextToken();
			double attr1 = E();
			if (lexer.curToken != ExprToken.RPAREN) {
				throw new ParseException("Expected RPAREN at position" , lexer.curPos());
			}
			lexer.nextToken();
			return attr1;
		}
		if (curToken == ExprToken.MINUS) {
			if (lexer.curToken != ExprToken.MINUS) {
				throw new ParseException("Expected MINUS at position" , lexer.curPos());
			}
			lexer.nextToken();
			double attr1 = F();
			return -attr1;
		}
		throw new AssertionError();
	}

	double TPrime() throws ParseException {
		ExprToken curToken = lexer.curToken();
		if (curToken == ExprToken.MUL) {
			if (lexer.curToken != ExprToken.MUL) {
				throw new ParseException("Expected MUL at position" , lexer.curPos());
			}
			lexer.nextToken();
			double attr1 = F();
			double attr2 = TPrime();
			return attr1 * attr2;
		}
		if (curToken == ExprToken.DIV) {
			if (lexer.curToken != ExprToken.DIV) {
				throw new ParseException("Expected DIV at position" , lexer.curPos());
			}
			lexer.nextToken();
			double attr1 = F();
			double attr2 = TPrime();
			return 1.0 / (attr1 * attr2);
		}
		if (curToken == ExprToken.END ||
		    curToken == ExprToken.MINUS ||
		    curToken == ExprToken.PLUS ||
		    curToken == ExprToken.RPAREN) {
			if (lexer.curToken != ExprToken.) {
				throw new ParseException("Expected  at position" , lexer.curPos());
			}
			lexer.nextToken();
			return 1;
		}
		throw new AssertionError();
	}

}
