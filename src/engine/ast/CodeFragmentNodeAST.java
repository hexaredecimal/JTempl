package engine.ast;

/**
 *
 * @author hexaredecimal
 */
public class CodeFragmentNodeAST implements Ast {
	
	private String code;

	public CodeFragmentNodeAST(String code) {
		this.code = code.replace("\"", "\\\"");
	}

	@Override
	public String exec() {
		return String.format("%s\n", this.code);
	}

	@Override
	public String toString() {
		return "<code> " + this.code;
	}
}
