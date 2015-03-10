/**
 * Created by dmsuvorov on 10.03.2015.
 */

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public abstract class Lexer<T> {
    private InputStream is;
    private int curChar;
    private int curPos;
    private T curToken;

    public Lexer(InputStream is) throws ParseException {
        this.is = is;
    }

    private void nextChar() throws ParseException {
        curPos++;
        try {
            curChar = is.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), curPos);
        }
    }

    public abstract void nextToken() throws ParseException;

    public T curToken() {
        return curToken;
    }

    public int curPos() {
        return curPos;
    }
}
