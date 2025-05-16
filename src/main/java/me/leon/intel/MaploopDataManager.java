package me.leon.intel;

import imgui.*;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.flag.ImGuiItemFlags;
import imgui.type.ImString;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.awt.*;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class MaploopDataManager extends Application {
    public static boolean openModal = false;
    public static final String _appTitle = "Intelligence Center TV1";

    public static int attempt = 0;

    ImString display = new ImString();
    ImString desc = new ImString();
    ImString savedFilesPath = new ImString();
    public static ImString passcode = new ImString();
    ImString themeFile = new ImString();

    boolean devMode = false;
    public static ImString console = new ImString();

    public MaploopDataManager() {
        DataHandler.PreLoad();
        savedFilesPath = new ImString(DataHandler._savedFilesPath, 255);
        themeFile = new ImString(DataHandler._themeFile);
    }

    @Override
    protected void configure(Configuration config) {
        config.setTitle("Maploop Confidential Intel Center");
    }

    boolean themeModal = false;
    boolean passcodeModal = true;
    boolean error = false;
    public static boolean incorrectPassword = false;
    public static boolean changePasswordModal = false;
    boolean tooManyAttempts = false;
    boolean passcodeExplainModal = false;

    ImString logStr = new ImString(999);
    ImString newPass1 = new ImString();
    ImString newPass2 = new ImString();

    String passcodeError = "";

    @Override
    public void process() {
        ImGui.styleColorsDark();
        ImGui.getStyle().setColor(ImGuiCol.Button, 126, 66, 245, 255);
        ImGui.getStyle().setColor(ImGuiCol.WindowBg, 0, 35, 51, 255);
        ImGui.getStyle().setColor(ImGuiCol.ChildBg, 252, 21, 0, 255);
        ImGui.getStyle().setFramePadding(5, 5);
        ImGui.getStyle().setFrameRounding(5);

        if (changePasswordModal) {
            ImGui.begin("Change Password", ImGuiWindowFlags.NoCollapse |
                    ImGuiWindowFlags.NoMove |
                    ImGuiWindowFlags.NoResize);
            ImGui.text("Enter your current password");
            ImGui.inputText("Current Password", SecurityV2.key);
            ImGui.text("Enter your new password");
            ImGui.inputText("New Password", newPass1);
            ImGui.text("Confirm your new password");
            ImGui.inputText("Confirm New Password", newPass2);
            if (!Objects.equals(passcodeError, "")) {
                ImGui.getStyle().setColor(ImGuiCol.Text, 245, 56, 56, 255);
                ImGui.text(passcodeError);
                ImGui.getStyle().setColor(ImGuiCol.Text, 255, 255, 255, 255);
            }
            ImGui.getStyle().setColor(ImGuiCol.Button, 209, 204, 61, 255);
            ImGui.getStyle().setColor(ImGuiCol.Text, 0, 0, 0, 255);
            if (ImGui.button("Change Password")) {
                if (!newPass1.get().equals(newPass2.get())) {
                    passcodeError = "Passwords do not match.";
                    ImGui.end();
                    return;
                }
                if (!Conversion.convert(newPass1.get())) {
                    passcodeError = "Wrong previous password.";
                    ImGui.end();
                    return;
                }
                newPass2.set("");
                DataHandler.corruptedSize = 0;
                changePasswordModal = false;
            }
            ImGui.getStyle().setColor(ImGuiCol.Text, 255, 255, 255, 255);
            ImGui.getStyle().setColor(ImGuiCol.Button, 110, 3, 168, 255);
            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                changePasswordModal = false;
                passcodeError = "";
                passcodeModal = true;
            }
            ImGui.end();
            return;
        }

        if (incorrectPassword) {
            ImGui.begin("Incorrect Password", ImGuiWindowFlags.NoCollapse |
                    ImGuiWindowFlags.NoMove |
                    ImGuiWindowFlags.NoResize);
            ImGui.text("PLEASE ENTER A VALID KEY");
            ImGui.getStyle().setColor(ImGuiCol.Text, 245, 56, 56, 255);
            ImGui.text("The key you entered was unable to decrypt the necessary data.\n" +
                    "Please try again.");
            ImGui.getStyle().setColor(ImGuiCol.Text, 255, 255, 255, 255);
            ImGui.newLine();
            ImGui.getStyle().setColor(ImGuiCol.Text, 245, 56, 56, 255);
            if (tooManyAttempts) {
                ImGui.text("Too many attempts!");
            }
            ImGui.getStyle().setColor(ImGuiCol.Text, 255, 255, 255, 255);

            if (ImGui.button("Try again")) {
                if (attempt >= 3) {
                    System.out.println("Too many attempts");
                    tooManyAttempts = true;
                    ImGui.end();
                    return;
                }

                DataHandler._dataObjects.clear();
                DataHandler.corruptedSize = 0;
                DataHandler._alreadyErroredKeys.clear();
                attempt++;
                System.out.println("Attempt " + attempt);
                incorrectPassword = false;
                passcodeModal = true;
            }

            ImGui.getStyle().setColor(ImGuiCol.Button, 245, 56, 56, 255);
            ImGui.sameLine();
            if (ImGui.button("Exit")) {
                System.exit(0);
            }
            ImGui.end();
            return;
        }

        if (passcodeExplainModal) {
            ImGui.begin("What is this?", ImGuiWindowFlags.NoCollapse |
                    ImGuiWindowFlags.NoMove |
                    ImGuiWindowFlags.NoResize);
            ImGui.text("This is a key that is used to encrypt your data.\n" +
                    "It is used to prevent unauthorized access to your data.\n" +
                    "If you lose this key, you will lose access to your data.\n" +
                    "Keep in mind that if you enter a wrong different key, and use\n" +
                    "that key to create a new node, the node will be encoded with your\n" +
                    "current key, and will not work with your main key that you chose before.");
            if (ImGui.button("Okay")) {
                passcodeExplainModal = false;
                passcodeModal = true;
            }
            ImGui.end();
            return;
        }

        if (passcodeModal) {
            ImGui.begin("Confirm Identity", ImGuiWindowFlags.NoCollapse |
                    ImGuiWindowFlags.NoMove |
                    ImGuiWindowFlags.NoResize);
            if (error) {
                ImGui.getStyle().setColor(ImGuiCol.Text, 245, 56, 56, 255);
                ImGui.text("Please enter a valid key");
                ImGui.getStyle().setColor(ImGuiCol.Text, 255, 255, 255, 255);
            }
            boolean t = ImGui.inputText("Enter your key", SecurityV2.key, ImGuiInputTextFlags.AutoSelectAll |
                    ImGuiInputTextFlags.EnterReturnsTrue | ImGuiInputTextFlags.Password);
            if (t) {
                if (confirmKey()) {
                    passcodeModal = false;
                } else {
                    error = true;
                }
            }
            ImGui.getStyle().setColor(ImGuiCol.Text, 60, 201, 60, 255);
            ImGui.text("Press [Enter] to proceed.");
            ImGui.getStyle().setColor(ImGuiCol.Text, 255, 255, 255, 255);


            ImGui.getStyle().setColor(ImGuiCol.Button, 245, 56, 56, 255);
            if (ImGui.button("Exit")) {
                System.exit(0);
            }
            ImGui.getStyle().setColor(ImGuiCol.Button, 209, 204, 61, 255);
            ImGui.getStyle().setColor(ImGuiCol.Text, 0, 0, 0, 255);
            ImGui.sameLine();
            if (ImGui.button("Change Password")) {
                changePasswordModal = true;
                passcodeModal = false;
            }
            ImGui.getStyle().setColor(ImGuiCol.Text, 255, 255, 255, 255);
            ImGui.getStyle().setColor(ImGuiCol.Button, 110, 3, 168, 255);
            ImGui.sameLine();
            if (ImGui.button("What is this?")) {
                passcodeExplainModal = true;
                passcodeModal = false;
            }
            ImGui.end();

            return;
        }

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
        if (ImGui.button("Lock")) {
            passcodeModal = true;

            error = false;
            SecurityV2.key.set("");
            DataHandler.corruptedSize = 0;
            DataHandler._dataObjects.clear();
        }
        ImGui.text("");
        ImGui.text("Configuration");
        ImGui.text(".SHEN Files Directory");
        ImGui.inputText("##Saves Folder", savedFilesPath);
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

    private boolean confirmKey() {
        if (SecurityV2.key.get().isEmpty()) {
            System.out.println("No key set");
            return false;
        }
        passcodeModal = false;
        System.out.println("Passcode Confirmed " + SecurityV2.key.get());
        DataHandler.Load();
        return true;
    }

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out) {
            @Override
            public void println(String x) {
                console.set(console.get() + "\n" + x);
                super.println(x);
            }
        });

        launch(new MaploopDataManager());
    }
}
