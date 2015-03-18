import java.io.BufferedReader;
import java.io.IOException;


public class IOUtils {
	public static String DEFAULT_DELIMITER = "%%";
	
	public static void expectDelimiter(BufferedReader br, String delimiter) throws IOException {
		if (!delimiter.equals(br.readLine())) {
			throw new IOException("Delimiter expected");
		}
	}
}
