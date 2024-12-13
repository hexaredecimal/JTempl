package jtempl;

import blazing.fs.FileSystem;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class TemplEngine {

	private static int line_count = 0;

	public static FileOutput generate(File input_file) {
		var input = FileSystem.readFileToString(input_file.getAbsolutePath()).unwrap();
		StringBuilder sb = new StringBuilder();
		List<String> keywords = new ArrayList<>();
		keywords.add("if");
		keywords.add("else");
		keywords.add("switch");
		keywords.add("case");
		keywords.add("do");
		keywords.add("while");
		line_count = 0;
		input.lines().forEach(line -> {
			line_count++;
			if (line.contains("%")) {
				int start = line.indexOf("%");
				int end = line.lastIndexOf("%");
				if (start == end) {
					System.out.println(input_file.getName() + ":" + line_count + ": Invalid JSP input");
					System.exit(0);
				}

				var first = line.substring(0, start).trim();
				var code = line.substring(start + 1, end).trim();
				var tokens = Parser.parse(code);
				if (!first.isBlank()) {
					sb.append("sb.append(\"").append(first).append("\");\n");
				}
				if (keywords.contains(tokens.getFirst())) {
					sb.append(code).append("\n");
				} else {
					if (tokens.contains("=")) { // variable assign
						sb.append(code).append("\n");
					} else {
						var ff = tokens.getFirst();
						if (Parser.isId(ff) || Parser.isNumber(ff) || Parser.isString(ff)) {
							sb.append("sb.append(").append(code).append(");\n");
						} else {
							sb.append(ff).append("\n");
						}
					}
				}
				var last = line.substring(end + 1);
				if (!last.isBlank()) {
					sb.append("sb.append(\"").append(last).append("\");\n");
				}
			} else {
				sb.append("sb.append(\"").append(line).append("\");\n");
			}
		});

		int dot_index = input_file.getName().indexOf(".");
		String name = input_file.getName().substring(0, dot_index);
		var first_char = Character.toUpperCase(name.charAt(0));
		var rest = name.substring(1);
		var class_name = first_char + rest + "Template";
		var class_template
			= """
class %s {
	public static String generate(/*TODO: Support template args*/) {
 		StringBuilder sb = new StringBuilder();
  %s
		return sb.toString();
  } 
}
    """;
		var class_full = String.format(class_template, class_name, sb.toString().indent(4 * 4));
		return new FileOutput(class_name, class_full);
	}

	private class Parser {

		private static int index = 0;

		public static List<String> parse(String input) {
			List<String> tokens = new ArrayList<>();
			index = 0;
			while (index < input.length()) {
				char top = input.charAt(index);
				if (Character.isAlphabetic(top)) {
					tokens.add(collectId(input));
				} else if (Character.isWhitespace(top)) {
					// IGNORE ALL
					while (Character.isWhitespace(input.charAt(index)) && index < input.length()) {
						index++;
						if (index >= input.length()) {
							break;
						}
					}
				} else if (Character.isDigit(top)) {
					tokens.add(collectNumber(input));
				} else if (top == '"') {
					tokens.add(collectString(input));
				} else {
					tokens.add("" + top);
					index++;
				}
			}

			return tokens;
		}

		private static String collectString(String input) {
			char top = input.charAt(++index);
			StringBuilder sb = new StringBuilder();
			sb.append("\"");
			while (top != '"' && index < input.length()) {
				sb.append(top);
				++index;
				if (index >= input.length()) {
					break;
				}
				top = input.charAt(index);
			}
			sb.append("\"");
			return sb.toString();
		}

		private static String collectNumber(String input) {
			char top = input.charAt(index);
			StringBuilder sb = new StringBuilder();
			while ((top == '.' || Character.isDigit(top)) && index < input.length()) {
				sb.append(top);
				++index;
				if (index >= input.length()) {
					break;
				}
				top = input.charAt(index);
			}
			return sb.toString();
		}

		private static String collectId(String input) {
			char top = input.charAt(index);
			StringBuilder sb = new StringBuilder();
			while ((Character.isAlphabetic(top) || Character.isDigit(top)) && index < input.length()) {
				sb.append(top);
				++index;
				if (index >= input.length()) {
					break;
				}
				top = input.charAt(index);
			}
			return sb.toString();
		}

		public static boolean isId(String input) {
			if (!Character.isAlphabetic(input.charAt(0))) {
				return false;
			}
			for (int i = 1; i < input.length(); i++) {
				if (!Character.isAlphabetic(input.charAt(i)) || !Character.isDigit(input.charAt(i))) {
					return false;
				}
			}
			return true;
		}

		public static boolean isNumber(String input) {
			for (int i = 0; i < input.length(); i++) {
				char c = input.charAt(i);
				if (Character.isDigit(c) == false) {
					if (c == '.') continue;
					return false;
				}
			}
			return true;
		}

		public static boolean isString(String input) {
			return input.charAt(0) == '"' && input.charAt(input.length() - 1) == '"';
		}
	}

}