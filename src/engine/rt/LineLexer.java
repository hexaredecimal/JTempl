package engine.rt;

import engine.ast.Ast;
import engine.ast.HtmlTextNodeAst;
import engine.token.Token;
import engine.token.TokenKind;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author hexaredecimal
 */
public class LineLexer {

	private static final List<String> KEYWORDS = List.of(
		"var", "if", "else", "switch", "return", "for", "while", "do", "new"
	);

	private static final List<Character> OPERATORS = List.of(
		'=', '+', '*', '/', '-', '%', '!', '>', '<'
	);

	private List<Character> chars;

	public LineLexer(String code) {
		this.chars = code
			.chars()
			.boxed()
			.map((c) -> Character.valueOf((char) c.intValue()))
			.collect(Collectors.toList());
	}

	public List<Token> lex() {
		List<Token> tokens = new ArrayList<>();
		while (!this.chars.isEmpty()) {
			var top = next();
			if (Character.isLetter(top)) {
				tokens.add(collectIdentifier(top));
			} else if (Character.isDigit(top)) {
				tokens.add(collectNumber(top));
			} else if (top == '"') {
				tokens.add(collectString());
			} else if (top == "'".charAt(0)) {
				tokens.add(collectChar());
			} else if (top == ';' || top == '}') {
				tokens.add(new Token(TokenKind.TERMINATOR, String.valueOf(top)));
			} else if (OPERATORS.contains(top)) {
				tokens.add(collectOperator(top));
			} else if (!Character.isWhitespace(top)) {
				tokens.add(new Token(TokenKind.SYMBOL, String.valueOf(top)));
			}
		}

		return tokens;
	}

	public char next() {
		assert !this.chars.isEmpty();
		return this.chars.removeFirst();
	}

	public void unnext(char c) {
		this.chars.addFirst(c);
	}

	public char peek(int offset) {
		int index = 0;
		assert this.chars.size() < index + offset;
		return this.chars.get(index + offset);
	}

	private Token collectIdentifier(char top) {
		StringBuilder sb = new StringBuilder();
		while (!chars.isEmpty() && (Character.isLetter(top) || top == '_')) {
			sb.append(top);
			if (chars.isEmpty()) {
				break;
			}
			top = next();
		}

		if (KEYWORDS.contains(sb.toString())) {
			return new Token(TokenKind.KEYWORD, sb.toString());
		}
		return new Token(TokenKind.ID, sb.toString());
	}

	private Token collectNumber(char top) {
		StringBuilder sb = new StringBuilder();

		
		while (Character.isDigit(top)) {
			sb.append(top);
			if (chars.isEmpty()) {
				break;
			}
			top = next();
		}

		if (top == '.' && Character.isDigit(peek(0))) {
			var t = collectNumber(next());
			var num = String.format("%s.%s", sb, t.text);
			return new Token(TokenKind.VALUE, num);
		}

		if (sb.isEmpty()) sb.append(top);

		return new Token(TokenKind.VALUE, sb.toString());
	}

	private Token collectString() {
		var top = next();
		StringBuilder sb = new StringBuilder();
		while (!chars.isEmpty() && top != '"') {
			sb.append(top);
			if (chars.isEmpty()) {
				break;
			}
			top = next();
		}
		return new Token(TokenKind.VALUE, sb.toString());
	}

	private Token collectChar() {
		var top = next();
		StringBuilder sb = new StringBuilder();
		while (!chars.isEmpty() && top != "'".charAt(0)) {
			sb.append(top);
			if (chars.isEmpty()) {
				break;
			}
			top = next();
		}
		return new Token(TokenKind.VALUE, sb.toString());
	}

	private Token collectOperator(char top) {
		StringBuilder sb = new StringBuilder();
		sb.append(top);

		var next = peek(0);

		if (top == '=' && next == '=') {
			sb.append(next());
		} else if (top == '+' && next == '=') {
			sb.append(next());
		} else if (top == '+' && next == '+') {
			sb.append(next());
		} else if (top == '-' && next == '=') {
			sb.append(next());
		} else if (top == '-' && next == '-') {
			sb.append(next());
		} else if (top == '*' && next == '=') {
			sb.append(next());
		} else if (top == '/' && next == '=') {
			sb.append(next());
		} else if (top == '!' && next == '=') {
			sb.append(next());
		} else if (top == '>' && next == '>') {
			sb.append(next());
		} else if (top == '>' && next == '=') {
			sb.append(next());
		} else if (top == '<' && next == '<') {
			sb.append(next());
		} else if (top == '<' && next == '=') {
			sb.append(next());
		}
		return new Token(TokenKind.OPERATOR, sb.toString());
	}
}
