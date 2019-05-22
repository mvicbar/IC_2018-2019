package main;

import java.awt.GraphicsEnvironment;
import java.io.Console;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		Console console = System.console();
        if (console == null && !GraphicsEnvironment.isHeadless()) {
            String filename = System.getProperty("user.dir") + "\\CM.jar";
            try {
                Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + filename + "\""});
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Program has ended, please type 'exit' to close the console");
        }
        Runtime.getRuntime().exit(0);
	}

}
