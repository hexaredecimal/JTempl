package engine.fs;

import engine.adt.Result;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author hexaredecimal
 */
public class Fs {

	public static Result<String, IOException> readToString(String path) {
		try (Scanner sc = new Scanner(new File(path))) {
      StringBuilder sb = new StringBuilder();
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine()).append("\n");
			}
			return Result.ok(sb.toString());
		} catch (IOException e) {
			return Result.err(e);
		}
	}


  public static Result<Boolean, Exception> writeToFile(File fp, String data) {
    if (fp == null) {
      return Result.err(new Exception("File reference is null"));
    }

    if (!fp.exists()) {
      return Result.err(new Exception(String.format("File `%s` does not exist", fp.getName())));
    }

    if (fp.isDirectory()) {
      return Result.err(new Exception(String.format("File `%s` is a directory", fp.getName())));
    }

    try (FileWriter fw = new FileWriter(fp)) {
      fw.write(data);
      return Result.ok(true);
    } catch (IOException e) {
      return Result.err(e);
    }
  }

  public static Result<Boolean, Exception> appendToFile(File fp, String... data) {
    if (fp == null) {
      return Result.err(new Exception("File reference is null"));
    }

    if (!fp.exists()) {
      return Result.err(new Exception(String.format("File `%s` does not exist", fp.getName())));
    }

    if (!fp.isAbsolute()) {
      return Result.err(new Exception(String.format("File `%s` is a directory", fp.getName())));
    }

    try (FileWriter fw = new FileWriter(fp, true)) {
      for (var dt : data) {
        fw.append(dt);
      }
      return Result.ok(true);
    } catch (IOException e) {
      return Result.err(e);
    }
  }

	public static String pathToName(String path) {
		var name = new File(path).getName();
		var dot = name.lastIndexOf(".");
		return dot > 0 ? name.substring(0, dot) : name;
	}
}
