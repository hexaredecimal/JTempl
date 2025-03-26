package engine.token;

import java.util.List;

/**
 *
 * @author hexaredecimal
 */
public class Token {
	public TokenKind kind;
	public String text;

	public Token(TokenKind kind, String text) {
		this.kind = kind;
		this.text = text;
	}


	public static boolean hasKind(TokenKind kind, List<Token> toks) {
		for (var t : toks) {
			if (t.kind == kind) return true;
		}
		return false;
	}

	public static boolean hasToken(Token token, List<Token> toks) {
		for (var t : toks) {
			if (t.kind == token.kind && t.text.equals(t.text)) return true;
		}
		return false;
	}
}
