package jtempl;

import blazing.fs.FileSystem;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author user
 */
public class JTempl {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			printUsage();
			return;
		}

		Config config = new Config(args);
		config.parse();

		var input = config.getInput_path(); 
		if (input == null) {
			printUsage();
			return;
		}
		
		var out = TemplEngine.generate(new File(input));
		String path = "./";
		if (config.getOut_path() != null) {
			path = config.getOut_path();
		}
		path = path + "/" + out.getName();
		File out_file = new File(path);
		out_file.createNewFile();
		String contents = out.getContents();
		if (config.getPackage_name() != null) {
			contents = String.format("package %s;\n%s", config.getPackage_name(), contents);
		}
		FileSystem.writeToFile(new File(path), contents);
	}

	public static void printUsage() {
		System.out.println("templ [options] file");
		System.out.println();
		System.out.println("options :".indent(4).trim());
		System.out.println("(-package | -p) name             - Adds the generated code to template".indent(8).replaceAll("\n", ""));
		System.out.println("(-dir     | -d) path             - generate this code to (./ by default)".indent(8).replaceAll("\n", ""));
		System.out.println("(-help    | -h)                  - Shows this usage".indent(8));
		System.out.println("Made with Love by Gama Sibusiso".indent(8 * 2).replaceAll("\n", ""));
		System.out.println(("version: " + Config.version).indent(8 * 3));
	}

}
