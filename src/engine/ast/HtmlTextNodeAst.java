package engine.ast;

/**
 *
 * @author hexaredecimal
 */
public class HtmlTextNodeAst implements Ast {
	private String text;

	public HtmlTextNodeAst(String text) {
		this.text = text.replace("\"", "\\\"");;
	}

	@Override
	public String exec() {
		return String.format("sb.append(\"\"\"\n%s\n\"\"\");", this.text ).indent(4);
	}

	@Override
	public String toString() {
		return "<ht>" + this.text;
	}
}
