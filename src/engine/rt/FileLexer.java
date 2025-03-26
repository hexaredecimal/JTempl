package engine.rt;

import engine.ast.Ast;
import engine.ast.CodeFragmentNodeAST;
import engine.ast.HtmlTextNodeAst;
import engine.ast.InsertionNodeAST;
import engine.token.Token;
import engine.token.TokenKind;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author hexaredecimal
 */
public class FileLexer {

	private List<Character> chars;

	public FileLexer(String code) {
		this.chars = code
			.chars()
			.boxed()
			.map((c) -> Character.valueOf((char) c.intValue()))
			.collect(Collectors.toList());
	}

	public List<Ast> lex() {
		int index = 0;
		List<Ast> nodes = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		while (!this.chars.isEmpty()) {
			var top = next();
			if (top == '%' && peek(0) == '%') {
				next(); // Collect the second %
				if (!sb.isEmpty()) {
					nodes.add(new HtmlTextNodeAst(sb.toString()));
					sb.delete(0, sb.length());
				}
				nodes.add(collectNode());
			} else {
				sb.append(top);
			}
		}

		if (!sb.isEmpty()) {
			nodes.add(new HtmlTextNodeAst(sb.toString()));
			sb.delete(0, sb.length());
		}

		return nodes;
	}

	public char next() {
		assert !this.chars.isEmpty();
		return this.chars.removeFirst();
	}

	public char peek(int offset) {
		int index = 0;
		assert this.chars.size() < index + offset;
		return this.chars.get(index + offset);
	}

	private Ast collectNode() {
		var top = next();
		StringBuilder sb = new StringBuilder();
		while (top != '%' && peek(0) != '%') {
			sb.append(top);
			top = next();
		}
		if (top != '%') sb.append(top);
		next();
		next();

		LineLexer lexer = new LineLexer(sb.toString().trim());
		var tokens = lexer.lex();
		return decideNode(tokens, sb.toString());
	}

	private Ast decideNode(List<Token> tokens, String code) {
		if (tokens.isEmpty()) {
			return new CodeFragmentNodeAST(code);
		}

		var decider = tokens.getFirst();
		var last = tokens.getLast();
		var kind = decider.kind;
		var text = decider.text;

		var last_kind = last.kind;
		var last_text = last.text; 

		if (kind == TokenKind.KEYWORD && text.equals("new")) {
			return new InsertionNodeAST(code);
		}

		if (kind == TokenKind.KEYWORD || kind == TokenKind.TERMINATOR) {
			return new CodeFragmentNodeAST(code);
		}

		if (kind == TokenKind.ID && Token.hasToken(new Token(TokenKind.OPERATOR, "="), tokens) && last_text.equals(";")) {
			return new CodeFragmentNodeAST(code);
		} 

		if (kind == TokenKind.ID && last_kind == TokenKind.TERMINATOR && last_text.equals(";")) {
			return new CodeFragmentNodeAST(code);
		}

		if (kind == TokenKind.ID && Token.hasKind(TokenKind.OPERATOR, tokens)) {
			return new InsertionNodeAST(code);
		}

		if (kind == TokenKind.VALUE && Token.hasKind(TokenKind.OPERATOR, tokens)) {
			return new InsertionNodeAST(code);
		}
		
		if (kind == TokenKind.VALUE){
			return new InsertionNodeAST(code);
		}

		if (Token.hasKind(TokenKind.ID, tokens) && Token.hasKind(TokenKind.OPERATOR, tokens) && Token.hasKind(TokenKind.VALUE, tokens)) {
			return new InsertionNodeAST(code);
		}

		It.todo("Make exhaustive: " + kind + " " + text);
		return null;
	}

}
