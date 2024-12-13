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

		String input = args[args.length - 1];
		var out = TemplEngine.generate(new File(input));
		File out_file = new File(out.getName());
		out_file.createNewFile();
		FileSystem.writeToFile(new File(out.getName()), out.getContents());
	}

	public static void printUsage() {
		System.out.println("templ [options] file");
		System.out.println();
		System.out.println("options :".indent(4).trim());
		System.out.println("-package name".indent(8));
	}
}
