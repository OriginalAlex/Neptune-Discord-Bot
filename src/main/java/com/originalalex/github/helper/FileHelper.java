package com.originalalex.github.helper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class FileHelper {

    public static String getFileAsString(Charset charset) {
        File file = new File("src/main/resources/config.json");
        try {
            return new String(Files.readAllBytes(file.toPath()), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
