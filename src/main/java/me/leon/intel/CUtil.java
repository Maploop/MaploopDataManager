package me.leon.intel;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class CUtil {
    public static void writeToFile(File f, String val) {
        try (FileWriter fr = new FileWriter(f)) {
            fr.write(val);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFromFile(File f) {
        try {
            return new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static String trimString(String displayName) {
        return displayName.replaceAll(" ", "_").toLowerCase();
    }

    public static void buildProgressBar(String title, float progress, int size, String underlog) {
        StringBuilder builder = new StringBuilder();
        builder.append(title + " [");
        float percentageOfSize = progress * size;
        for (int i = 0; i < size; i++) {
            if (i <= percentageOfSize)
                builder.append("=");
            else
                builder.append("-");
        }
        if (progress >= 1)
            builder.append("] (FINISHED)\n");
        else
            builder.append("] (" + underlog + ")\r");

        System.out.print(builder);
    }
}
