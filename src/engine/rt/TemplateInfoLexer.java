package engine.rt;

import engine.errors.Errors;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author hexaredecimal
 */
public class TemplateInfoLexer {

	private String code;
	private String path;
	private List<String> errors;
	private List<String> imports;
	private String _package;

	public TemplateInfoLexer(String code, String path) {
		this.code = code;
		this.path = path;
		this.errors = new ArrayList<>();
		this._package = null;
		this.imports = new ArrayList<>();
	}

	public List<String> getImports() {
		return imports;
	}

	public String getPackage() {
		return _package;
	}

	public void lex() {
		var lines = this.code.lines().collect(Collectors.toList());

		for (int i = 0; i < lines.size(); i++) {
			var line = lines.get(i);
			if (!line.startsWith("#")) {
				continue;
			}
			// Remove the #
			line = line.substring(1);

			// Figure out the column
			int col = 1;
			while (Character.isWhitespace(line.charAt(0))) {
				line = line.substring(1);
				col++;
			}

			var splits = line.split(" ");
			if (splits[0].equals("package") == false && splits[0].equals("import") == false) {
				int sp = line.indexOf(" ");
				line = sp > 0 ? line.substring(0, sp) : line;
				errors.add(Errors.create(path, i + 1, col, "Invalid meta data description found: " + splits[0]));
				continue;
			}

			String operation = splits[0];
			col += operation.length();
			line = line.substring(operation.length());

			while (Character.isWhitespace(line.charAt(0))) {
				line = line.substring(1);
				col++;
			}

			if (operation.equals("import")) {
				imports.add(line.replaceAll(";", ""));
			} else {
				if (_package != null) {
					errors.add(Errors.create(path, i + 1, col, "Package aleady set to " + _package + " but " + line + " is provided"));
				}
				_package = line.replaceAll(";", "");
			}
		}
	}

	public static String removeTemplateInfo(String code) {
		var lines = code.lines().collect(Collectors.toList());

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lines.size(); i++) {
			var line = lines.get(i);
			if (line.startsWith("#")) {
				continue;
			}
			sb.append(line).append("\n");
		}

		return sb.toString();
	}

	public void reportErrors() {
		if (errors.isEmpty()) {
			return;
		}

		for (var error : errors) {
			System.out.println(error);
			System.out.println();
		}
		System.exit(Errors.EXIT_FAILURE);
	}
}
