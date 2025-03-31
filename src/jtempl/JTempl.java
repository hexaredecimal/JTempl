package jtempl;

import engine.Engine;
import engine.config.Config;
import engine.errors.Errors;
import java.io.File;

/**
 *
 * @author hexaredecimal
 */
public class JTempl {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Config config = new Config(args);
		config.parse();

		Engine engine = new Engine(config);
		
		if (config.isRepl()) {
			engine.repl();
			return;
		}
		
		var input = config.getInputFile();
		if (input == null) {
			Errors.die("No input file");
		}

		var fp = new File(input);
		if (!fp.exists()) {
			Errors.die(String.format("File does not exist: `%s`", input));
		}

		if (!fp.canRead()) {
			Errors.die(String.format("File cannot be read: `%s`", input));
		}

		engine.process();
	}
}
