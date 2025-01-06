package me.leon.intel;

import imgui.*;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.*;
import imgui.type.ImString;
import javax.swing.*;
import java.awt.*;
import java.io.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Entry extends Application {
    public static boolean openModal = false;
    public static final String _appTitle = "Intelligence Center TV1";

    ImString display = new ImString();
    ImString desc = new ImString();
    ImString savedFilesPath = new ImString();
    ImString themeFile = new ImString();

    boolean devMode = false;
    public static ImString console = new ImString();

    public Entry() {
        DataHandler.Load();
        /* can't fiddle with this shit apparently */
        // super= new Color(0, 0, 0, 1);
        // new ABS_MDB_HANDLE().initiateConnection();
        savedFilesPath = new ImString(DataHandler._savedFilesPath, 255);
        themeFile = new ImString(DataHandler._themeFile);
    }

    @Override
    protected void configure(Configuration config) {
        config.setTitle("Maploop Confidential Intel Center");

        // config.setFullScreen(true);

    }

    @Override
    protected void postRun() {
        super.postRun();

    }

    @Override
    protected void preRun() {
        super.preRun();
        ImGuiIO IO = ImGui.getIO();
        // IO.getFonts().addFontDefault();
        // IO.setFontDefault(IO.getFonts().addFontFromFileTTF("Arial.ttf", 256));
        // IO.getFonts().build();
    }

    boolean themeModal = false;

    ImString logStr = new ImString(999);

    @Override
    public void process() {
        ImGui.styleColorsDark();
        ImGui.getStyle().setColor(ImGuiCol.Button, 126, 66, 245, 255);
        ImGui.getStyle().setColor(ImGuiCol.WindowBg, 0, 35, 51, 255);
        ImGui.getStyle().setColor(ImGuiCol.ChildBg, 252, 21, 0, 255);
        ImGui.getStyle().setFramePadding(5, 5);
        ImGui.getStyle().setFrameRounding(5);

        if (themeModal) {
            ImGui.begin("Theme INPUT", ImGuiWindowFlags.NoCollapse |
                    ImGuiWindowFlags.NoMove |
                    ImGuiWindowFlags.NoResize);
            ImGui.text("Compilable INLT Theme String");
            ImGui.inputTextMultiline("IXD", CTheme.themeString, 900, 500, ImGuiInputTextFlags.AllowTabInput);

            if (ImGui.button("Compile")) {
                CTheme.recompile(logStr);
            }
            ImGui.sameLine();
            if (ImGui.button("Nevermind..")) {
                themeModal = false;
            }
            ImGui.text("Ouput Log");
            ImGui.inputTextMultiline("Log", logStr, 900, 100, ImGuiInputTextFlags.ReadOnly);
            ImGui.end();
        }

        if (openModal) {

            ImGui.begin("Create new node", ImGuiWindowFlags.NoCollapse |
                    ImGuiWindowFlags.NoMove |
                    ImGuiWindowFlags.NoResize);
            ImGui.inputText("Display Name", display);
            ImGui.inputText("Description", desc);

            if (ImGui.button("Cancel")) {
                openModal = false;
            }
            ImGui.sameLine();
            if (ImGui.button("Create")) {
                openModal = false;
                System.out.println(display.toString());
                System.out.println(desc.toString());

                if (!display.toString().isEmpty() && !desc.toString().isEmpty()) {
                    DataHandler.CreateNew(display.toString(), desc.toString());
                }

                display = new ImString();
                desc = new ImString();
            }

            ImGui.end();
        }

        ImGui.begin("Console Window",
                        ImGuiWindowFlags.NoMove |
                        ImGuiWindowFlags.NoResize);
        ImGui.inputTextMultiline("<<", console, 1265, 220, ImGuiInputTextFlags.ReadOnly);
        ImGui.end();

        ImGui.begin("General Options",
                ImGuiWindowFlags.NoCollapse |
                        ImGuiWindowFlags.NoMove |
                        ImGuiWindowFlags.NoResize |
                        ImGuiWindowFlags.NoTitleBar);
        ImGui.text("Maploop Confidential Intel Center");
        ImGui.spacing();
        if (ImGui.button("Create New Node")) {
            openModal = true;
        }
        if (ImGui.button("Exit")) {
            System.exit(0);
        }
        ImGui.text("");
        ImGui.text("CFG [DO NOT EDIT]");
        ImGui.inputText("Saves Folder", savedFilesPath);
        if (ImGui.button("Update Config")) {
            DataHandler._savedFilesPath = savedFilesPath.toString();
            try {
                DataHandler.SaveConfig();
            } catch (Exception e) {
                showExceptionDialog(e);
            }
        }

        if (ImGui.button("Theme Options")) {
            themeModal = true;
        }
        if (ImGui.checkbox("Developer Mode", devMode)) {
            devMode = !devMode;
        }

        ImGui.end();

        ImGui.begin("Confidential Intel Documents",
                ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoTitleBar);
        ImGui.text("Confidential Intel Documents");
        DataHandler.UpdateList();
        ImGui.end();

        DataHandler.Update();
    }

    public static void showExceptionDialog(Exception e) {
        String message = "<html><b><font color='red'>Error:</font></b><br><br>"
                + "<font size='4' color='black'>" + e.getMessage() + "</font><br><br>"
                + "<font size='3' color='gray'>" + e.toString() + "</font></html>";

        JOptionPane optionPane = new JOptionPane(
                message,
                JOptionPane.ERROR_MESSAGE,
                JOptionPane.DEFAULT_OPTION
        );

        JDialog dialog = optionPane.createDialog("Exception Occurred");

        dialog.setBackground(Color.DARK_GRAY);
        dialog.setResizable(true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out) {
            @Override
            public void println(String x) {
                console.set(console.get() + "\n" + x);
                super.println(x);
            }
        });

        launch(new Entry());
    }
}
