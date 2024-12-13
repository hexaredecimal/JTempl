package jtempl;

/**
 *
 * @author user
 */
public class FileOutput {
	private String name, contents;

	public FileOutput(String name, String contents) {
		this.name = name;
		this.contents = contents;
	}

	public String getName() {
		return name + ".java";
	}

	public String getContents() {
		return contents;
	}
}
