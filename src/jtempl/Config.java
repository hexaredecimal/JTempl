package jtempl;

/**
 *
 * @author user
 */
public class Config {

	private String package_name = null, out_path = null;
	private String input_path;
	private String[] args;
	public static final String version = "1.5";

	public String getPackage_name() {
		return package_name;
	}

	public Config(String[] args) {
		this.args = args;
	}

	public String getOut_path() {
		return out_path;
	}

	public String getInput_path() {
		return input_path;
	}

	public void parse() {
		boolean expect_package = false;
		boolean expect_dir = false;
		for (int i = 0; i < args.length - 1; i++) {
			String top = args[i];
			if (top.equals("-package")) {
				expect_package = true;
				continue;
			} else if (top.equals("-dir")) {
				expect_dir = true;
				continue;
			} else if (top.equals("-help")) {
				JTempl.printUsage();
				return;
			}

			if (expect_package) {
				package_name = args[i];
				expect_package = false;
			} else if (expect_dir) {
				out_path = args[i];
				expect_dir = false;
			}
		}
		String last = args[args.length - 1];
		if (expect_dir) {
			System.out.println("-dir was specified by no input is provided. Last os args is " + last);
			System.exit(1);
		}
		if (expect_package) {
			System.out.println("-package was specified by no input is provided. Last of args is " + last);
			System.exit(1);
		}
		if (!last.startsWith("-"))
			this.input_path = last;
	}
}
