package me.leon.intel;

import imgui.type.ImString;

public class CTheme {
    public static ImString themeString = new ImString(999);

    public static void recompile(ImString outLog) {
        outLog.set("[INFO] Start of compilation...");

        String author = "Not Defined", desc = "Nothing Provided", dname = "Who knows";
        boolean began = false;
        for (String line : themeString.toString().split("\n")) {
            if (line.startsWith("&")) {
                if (line.startsWith("&begin")) {
                    log(outLog, "[INFO] &begin operator called! workflow is now up.");
                    began = true;
                }
                if (line.startsWith("&end")) {
                    log(outLog, "[INFO] (Compile SUC0) Compilation finished with \"SUC0\"");
                    log(outLog, "Theme: " + dname);
                    log(outLog, "By: " + author);
                    log(outLog, "INFO: " + desc);
                    break;
                }
            }

            if (line.startsWith("Penis")) {

            }

            if (line.startsWith("@")) {
                if (!began) {
                    log(outLog, "[ERROR] (Compile ER0) @ worker before workflow BEGIN or after workflow END!");
                    break;
                }
                log(outLog, "[INFO] Working on starting INFO!");

                switch (line.replace("@", "")) {
                    case "author":
                        author = line.replace("@author ", "");
                        log(outLog, author);
                        break;
                    case "description":
                        desc = line.replace("@description ", "");
                        break;
                    case "dname":
                        dname = line.replace("@dname ", "");
                        break;
                }
            }

            if (line.startsWith("!")) {
                if (!began) {
                    log(outLog, "[ERROR] (Compile ER0) ! worker before workflow BEGIN or after workflow END!");
                    break;
                }
            }
        }
    }

    private static void log(ImString str, String l) {
        StringBuilder b = new StringBuilder(str.toString());
        b.append(l).append("\n");
        str.set(b.toString());
    }
}
