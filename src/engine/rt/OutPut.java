package engine.rt;

import engine.adt.Result;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 *
 * @author hexaredecimal
 */
public class OutPut {
	private String code;
	private String[] imports;
	private String packageName;

	public OutPut(String code, String[] imports, String packageName) {
		this.code = code;
		this.imports = imports;
		this.packageName = packageName;
	}

	public Result<Void, IOException> write(String path) {
		try (FileWriter fr = new FileWriter(path)) {
			fr.write(combine());
			return Result.ok(null);
		} catch (IOException e) {
			return Result.err(e);
		}
	}

	public Result<Void, IOException> write(OutputStream sr) {
		try {
			sr.write(combine().getBytes());
			return Result.ok(null);
		} catch (IOException e) {
			return Result.err(e);
		} 
	}

	private String combine() {
		StringBuilder sb = new StringBuilder();
		if (packageName != null) {
			sb.append(String.format("package %s;\n", packageName));
		}

		for (var import_ : imports) {
			sb.append(String.format("import %s;\n", import_));
		}

		sb.append("// This code is auto generated by JTemple, edit at your own risk\n\n");
		sb.append(code);

		return sb.toString();
	}
}
