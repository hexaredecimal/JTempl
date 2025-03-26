package jtempl;

import engine.Engine;
import engine.config.Config;
import engine.errors.Errors;

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

		if (config.getInputFile() == null) {
			Errors.die("No input file");
		}

		
		Engine engine = new Engine(config);
		engine.process();
	}
}
