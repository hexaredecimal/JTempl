package engine.errors;

import engine.fs.Fs;

/**
 *
 * @author hexaredecimal
 */
public class Errors {
	public static final int EXIT_SUCCESS = 0;
	public static final int EXIT_FAILURE = 1;
	
	public static String create(String file, int line, int col, String message) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s:%d:%d: %s\n", file, line, col, message.indent(2).replaceAll("\n", "")));
		var code = Fs.readToString(file).unwrap();
		var lines = code.lines().toArray(String[]::new); 
		if (line > 1 && line < lines.length - 1) {
			sb.append(String.format("%s \n", lines[line - 1]));
			sb.append(" ".repeat(col)).append("^\n");
		}

		return sb.toString();
	}

	public static void die(String message) {
		System.out.println("ERROR: " + message);
		System.exit(Errors.EXIT_FAILURE);
	}
}
