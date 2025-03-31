package engine;

import engine.config.Config;
import engine.fs.Fs;
import engine.rt.FileLexer;
import engine.rt.OutPut;
import engine.rt.TemplateInfoLexer;
import engine.utils.TypingCases;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author hexaredecimal
 */
public class Engine {

	Config config;

	public Engine(Config config) {
		this.config = config;
	}

	public void repl() {
		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				System.out.print("jtmpl> ");
				StringBuilder sb = new StringBuilder();
				var tmp = sc.nextLine(); 
				while (!tmp.isEmpty()) {
					sb.append(tmp);
					tmp = sc.nextLine();
				}
				var code = sb.toString();
				_repl(code);
			}
		}
	}

	private void _repl(String code) {
		var lexer = new FileLexer(TemplateInfoLexer.removeTemplateInfo(code));
		var nodes = lexer.lex();
		var filepath = "repl";
		StringBuilder sb = new StringBuilder();

		var tinfo = new TemplateInfoLexer(code, filepath);
		tinfo.lex();
		tinfo.reportErrors();

		var imports = tinfo.getImports();
		var package_ = config.getPackageName() == null ? tinfo.getPackage() : config.getPackageName();

		for (var node : nodes) {
			sb.append(node.exec());
		}


		if (package_ != null) {
			sb.append(String.format("package %s;\n", package_));
		}

		for (var import_ : imports) {
			sb.append(String.format("import %s;\n", import_));
		}
		sb.append("\n");
		code = sb.toString();

		System.out.println(code);
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

		var outFile = config.getOutFile() == null ? filepath : config.getOutFile();
		code = processClass(sb.toString(), TypingCases.title(outFile), config, tinfo.getArgs());

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
			System.out.println("Writing to: " + outFile);
			out.write(outFile);
		}
	}

	private String processClass(String code, String file, Config config, String args) {
		String name = Fs.pathToName(file);
		name = Character.isLowerCase(name.charAt(0))
			? TypingCases.title(name)
			: name;

		StringBuilder sb = new StringBuilder();
		sb
			.append(String.format("public class %s {", name))
			.append("\n")
			.append(String.format("public static String generate(%s) {", args == null ? "" : args).indent(4))
			.append("\n")
			.append("StringBuilder sb = new StringBuilder();".indent(8))
			.append(code.indent(4))
			.append("return sb.toString().trim();".indent(8))
			.append("}".indent(4))
			.append("}".indent(0));

		return sb.toString();
	}
}
