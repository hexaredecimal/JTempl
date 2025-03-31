package engine.config;

import engine.adt.Result;
import engine.errors.Errors;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hexaredecimal
 */
public class Config {

	private List<String> args;
	private String outFolder;
	private String packageName;
	private String inFile;
	private String outFile;
	private boolean dump;

	private static final String VERSION = "0.1.1";
	private static final String APP = "JTempl";
	private static final String Author = "Gama Sibusiso (https://github.com/hexaredecimal)";

	public Config(String[] args) {
		this.outFolder = "./";
		this.packageName = null;
		this.inFile = null;
		this.outFile = null;
		this.dump = false;
		this.args = new ArrayList<>(List.of(args));
	}

	public String getOutFolder() {
		return (outFolder.endsWith("/")
			? outFolder.substring(0, outFolder.lastIndexOf("/"))
			: outFolder) + "/";
	}

	public String getOutFile() {
		return outFile;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getInputFile() {
		return inFile;
	}

	public boolean isDump() {
		return dump;
	}

	private Result<String, Exception> shift() {
		return args.isEmpty() ? Result.err(new Exception("End of args")) : Result.ok(args.removeFirst());
	}

	public void parse() {
		while (!args.isEmpty()) {
			var top = shift();
			if (top.isErr()) {
				break;
			}

			var arg = top.unwrap();
			if (arg.equals("-d")) {
				top = shift();
				if (top.isErr()) {
					Errors.die("Expected a directory to be provided after -d");
				}
				outFolder = top.unwrap();
			} else if (arg.equals("-p")) {
				top = shift();
				if (top.isErr()) {
					Errors.die("Expected a package name to be provided after -p");
				}
				packageName = top.unwrap();
			} else if (arg.equals("-o")) {
				top = shift();
				if (top.isErr()) {
					Errors.die("Expected a file name to be provided after -o");
				}
				outFile = top.unwrap();
			} else if (arg.equals("-h")) {
				help();
			} else if (arg.equals("-s")) {
				dump = true;
			}  else if (arg.equals("-v")) {
				version();
			}else {
				if (inFile == null) {
					inFile = arg;
				} else {
					Errors.die("Invalid command line argument provided: " + top.unwrap());
				}
			}
		}
	}

	private void help() {
		System.out.println("Usage: [OPTIONS] <file>");
		System.out.println();
		System.out.print("\t-d");
		System.out.print("- Specifies the output directory.".indent(4));
		System.out.print("\t-p");
		System.out.print("- Specifies the package name for the generated templage.".indent(4));
		System.out.print("\t-o");
		System.out.print("- Specifies the output file name".indent(4));
		System.out.print("\t-s");
		System.out.print("- Outputs to stdout.".indent(4));
		System.out.print("\t-v");
		System.out.print("- Dispalys version information.".indent(4));
		System.out.print("\t-h");
		System.out.print("- Displays this help information.".indent(4));

		System.exit(Errors.EXIT_SUCCESS);
	}

	private void version() {
		System.out.println(String.format("%s version %s", APP, VERSION));
		System.out.println("by: " + Author);
		System.exit(Errors.EXIT_SUCCESS);
	}

}
