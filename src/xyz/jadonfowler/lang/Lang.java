package xyz.jadonfowler.lang;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lang {

    public static void main(String[] args) {
        if (args.length == 1) {
            File file = new File(args[0]);
            if (file.isDirectory()) {
                // TODO recursive file parsing
            } else if (file.isFile()) {
                try {
                    String content = new String(Files.readAllBytes(Paths.get(file.getPath())), Charset.defaultCharset());
                    // TODO parse content
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (!file.exists()) {
                System.err.println(file + " does not exist!");
            }
        }
    }

}
