import java.io.InputStream;
import java.text.ParseException;

class ExprParser{
	ExprLexer lexer;

	double parse(InputStream is) throws ParseException {
		lexer = new ExprLexer(is);
		lexer.nextToken();
		return E();
	}

	double EPrime() {
		ExprToken curToken = lexer.curToken();
		if (curToken == ExprToken.PLUS) {
			double attr1 = T();
			double attr2 = EPrime();
			return +(attr1 + attr2);
		}
		if (curToken == ExprToken.MINUS) {
			double attr1 = T();
			double attr2 = EPrime();
			return -(attr1 + attr2);
		}
		if (curToken == ExprToken.END ||
		    curToken == ExprToken.RPAREN) {
			return 0;
		}
		throw new AssertionError();
	}

	double T() {
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

	double E() {
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

	double F() {
		ExprToken curToken = lexer.curToken();
		if (curToken == ExprToken.NUMBER) {
			return attr0;
		}
		if (curToken == ExprToken.LPAREN) {
			double attr1 = E();
			return attr1;
		}
		if (curToken == ExprToken.MINUS) {
			double attr1 = F();
			return -attr1;
		}
		throw new AssertionError();
	}

	double TPrime() {
		ExprToken curToken = lexer.curToken();
		if (curToken == ExprToken.MUL) {
			double attr1 = F();
			double attr2 = TPrime();
			return attr1 * attr2;
		}
		if (curToken == ExprToken.DIV) {
			double attr1 = F();
			double attr2 = TPrime();
			return 1.0 / (attr1 * attr2);
		}
		if (curToken == ExprToken.END ||
		    curToken == ExprToken.MINUS ||
		    curToken == ExprToken.PLUS ||
		    curToken == ExprToken.RPAREN) {
			return 1;
		}
		throw new AssertionError();
	}

}
