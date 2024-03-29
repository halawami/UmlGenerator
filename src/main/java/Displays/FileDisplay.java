package Displays;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class FileDisplay implements Display {

	@Override
	public void display(String umlCode) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("temp" + System.getProperty("file.separator") + "UMLGenerator-"
					+ System.currentTimeMillis() + ".txt", "UTF-8");
			writer.print(umlCode);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
