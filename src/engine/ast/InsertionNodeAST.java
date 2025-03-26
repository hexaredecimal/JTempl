package engine.ast;

/**
 *
 * @author hexaredecimal
 */
public class InsertionNodeAST implements Ast {
	
	private String code;

	public InsertionNodeAST(String code) {
		this.code = code.replace("\"", "\\\"").trim();
	}

	@Override
	public String exec() {
		return String.format("sb.append(%s);", this.code).indent(4);
	}

	@Override
	public String toString() {
		return "<in> " + this.code;
	}
}
