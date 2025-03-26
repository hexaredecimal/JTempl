package engine;

import com.sun.source.tree.TypeCastTree;
import engine.config.Config;
import engine.fs.Fs;
import engine.rt.FileLexer;
import engine.rt.OutPut;
import engine.rt.TemplateInfoLexer;
import engine.utils.TypingCases;
import java.io.FileWriter;

/**
 *
 * @author hexaredecimal
 */
public class Engine {

	Config config; 
	public Engine(Config config) {
		this.config = config;
	}

	public void process() {
		var filepath = config.getInputFile();
		var code = Fs.readToString(filepath).unwrap();
		var lexer = new FileLexer(TemplateInfoLexer.removeTemplateInfo(code));
		var nodes = lexer.lex();

		StringBuilder sb = new StringBuilder();

		var tinfo = new TemplateInfoLexer(code, filepath);
		tinfo.lex();
		tinfo.reportErrors();

		var imports = tinfo.getImports();
		var package_ = config.getPackageName() == null ? tinfo.getPackage() : config.getPackageName();

		for (var node : nodes) {
			sb.append(node.exec());
		}

		var outFile = config.getOutFile() == null ? TypingCases.title(filepath) : config.getOutFile();
		code = processClass(sb.toString(), outFile, config);

		if (package_ != null) {
			sb.append(String.format("package %s;\n", package_));
		}

		for (var import_ : imports) {
			sb.append(String.format("import %s;\n", import_));
		}

		sb.append("\n");

		var out = new OutPut(code, imports.toArray(String[]::new), package_);

		if (config.isDump()) {
			out.write(System.out);
		} else {
			var outFolder = config.getOutFolder();
			if (outFolder != null) {
				outFile = String.format("%s.java", outFolder + TypingCases.title(Fs.pathToName(outFile)));
			}
			out.write(outFile);
		}
	}

	private String processClass(String code, String file, Config config) {
		String name = Fs.pathToName(file);
		name = String.format("%s", name);
		StringBuilder sb = new StringBuilder();
		sb
			.append(String.format("public class %s {", name))
			.append("\n")
			.append("public static String generate() {".indent(4))
			.append("\n")
			.append("StringBuilder sb = new StringBuilder();".indent(8))
			.append(code.indent(4))
			.append("return sb.toString();".indent(8))
			.append("}".indent(4))
			.append("}".indent(0));
		
		return sb.toString();
	}
}
