package engine.utils;

/**
 *
 * @author hexaredecimal
 */
public class TypingCases {
	public static String upper(String s) {
		return s.toUpperCase();
	}

	public static String lower(String s) {
		return s.toLowerCase();
	}

	public static String title(String s) {
		char f = Character.toUpperCase(s.charAt(0));
		String rest = s.substring(1).toLowerCase();
		return String.format("%c%s", f, rest);
	}
}
