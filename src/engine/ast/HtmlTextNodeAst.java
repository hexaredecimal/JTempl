package engine.ast;

/**
 *
 * @author hexaredecimal
 */
public class HtmlTextNodeAst implements Ast {

	private String text;

	public HtmlTextNodeAst(String text) {
		this.text = text.contains("\"") ? text.replace("\"", "\\\'").trim() : text;
	}

	@Override
	public String exec() {
		return String.format("sb.append(\"\"\"\n%s\"\"\");", this.text).indent(4);
	}

	@Override
	public String toString() {
		return "<ht>" + this.text;
	}
}
