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
		double result;
		return result;
	}

	double T() {
		double result;
		return result;
	}

	double E() {
		double result;
		return result;
	}

	double F() {
		double result;
		return result;
	}

	double TPrime() {
		double result;
		return result;
	}

}

