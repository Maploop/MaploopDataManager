package me.leon.intel;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DataHandler {
    public static String _savedFilesPath = ".";
    public static File _localConfigFile = new File("./local_config.SHEN");
    public static String _themeFile;
    public static final Map<String, Boolean> _openWindows = new HashMap<>();
    public static final Map<String, Map<String, ImString>> _internalData = new HashMap<>();

    private static final List<String> _alreadyErroredKeys = new ArrayList<>();
    public static List<SHENDataObjectV2> _dataObjects = new ArrayList<>();

    private static int dataSize = 0;
    public static int corruptedSize = 0;

    public static void PreLoad() {
        if (!_localConfigFile.exists()) {
            try {
                _localConfigFile.createNewFile();

                FileWriter fw = new FileWriter(_localConfigFile);
                fw.write("{}");
                fw.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SHENDataObject cfg = new SHENDataObject(_localConfigFile);
        if (cfg.get("savedFilesPath") != null) {
            _savedFilesPath = cfg.getString("savedFilesPath");
        }
    }

    public static void Load() {
        SHENDataObject cfg = new SHENDataObject(_localConfigFile);
        JSONArray arr = cfg.getArray("fileList");
        if (arr == null)
            return;
        for (Object obj : arr) {
            String str = obj.toString();
            _dataObjects.add(new SHENDataObjectV2(new File(str)));
            System.out.println("-> LOADED '" + str + "' IN.");
        }

        dataSize = arr.size();
        System.out.println("Loaded " + dataSize + " data objects.");
    }

    public static void CreateNew(String displayName, String desc) {
        SHENDataObjectV2 data = new SHENDataObjectV2(
                new File(_savedFilesPath + File.separator + CUtil.trimString(displayName) + ".SHEN"));
        data.append("displayName", displayName)
                .append("description", desc)
                .append("data", new HashMap<String, String>())
                .save();

        _dataObjects.add(data);
        try {
            SaveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Created new EMPTY data node.");
    }

    public static void SaveConfig() throws IOException {
        SHENDataObject cfg = new SHENDataObject(_localConfigFile);

        List<String> stringArr = new ArrayList<>();
        for (SHENDataObjectV2 data : _dataObjects) {
            stringArr.add(data.getFilePath().getCanonicalPath());
        }

        cfg.set("savedFilesPath", _savedFilesPath);
        cfg.set("fileList", stringArr);
        cfg.save();
    }

    public static void UpdateList() {
        for (SHENDataObjectV2 data : _dataObjects) {
            if (data == null) {
                System.out.println("ERR- 1 FILE WAS CORRUPT (?)");
                continue;
            }
            String name;
            try {
                name = data.getString("displayName");
            } catch (NullPointerException e) {
                String p = data.getFilePath().getPath();
                if (!_alreadyErroredKeys.contains(p)) {
                    System.out.println("<!> '" + data.getFilePath().getPath() + "' MISSING PROPERTY \"displayName\"");
                    _alreadyErroredKeys.add(p);
                    System.out.println("-> INTERNAL POLICTY: THIS ERROR WILL ONLY BE SHOWN ONCE.");
                    corruptedSize++;
                }
                name = "[CORRUPTED] " + data.getFilePath().getName();
            }
            ImGui.getStyle().setButtonTextAlign(0, 0.5f);
            if (name.contains("[CORRUPTED]")) {
                ImGui.getStyle().setColor(ImGuiCol.Button, 255, 0, 0, 255);
            }
            if (ImGui.button("[DOC] " + name, 960, 30)) {
                ImGui.setNextWindowSize(500, 300);
                if (!_openWindows.containsKey(name)) {
                    _openWindows.put(name, true);
                } else {
                    _openWindows.put(name, !_openWindows.get(name));
                }
            }
            if (name.contains("[CORRUPTED]")) {
                ImGui.getStyle().setColor(ImGuiCol.Button, 126, 66, 245, 255);
            }
        }

        if (corruptedSize >= dataSize) {
            System.out.println("<!> ALL DATA OBJECTS ARE CORRUPTED. ASSUMING WRONG PASSCODE");

            MaploopDataManager.incorrectPassword = true;
        }
    }

    static SHENDataObjectV2 nextData = null;
    static boolean openModal = false;
    static ImString propName = new ImString();
    static ImString propVal = new ImString();

    public static void Update() {
        if (openModal) {
            ImGui.begin("Add Property", ImGuiWindowFlags.NoCollapse |
                    ImGuiWindowFlags.NoMove |
                    ImGuiWindowFlags.NoResize);
            ImGui.inputText("Name", propName);
            ImGui.inputText("Value", propVal);

            if (ImGui.button("Add")) {
                HashMap<String, String> intData = (HashMap<String, String>) nextData.get("data");
                intData.put(propName.toString(), propVal.toString());
                nextData.set("data", intData);
                nextData.save();
                System.out.println("SAVED");
                propName = new ImString();
                propVal = new ImString();
                openModal = false;
            }
            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                openModal = false;
            }
            ImGui.sameLine();

            ImGui.end();

            return;
        }

        for (SHENDataObjectV2 data : _dataObjects) {
            if (data == null) {
                System.out.println("<!> 1 FILE WAS CORRUPT (?)");
                continue;
            }
            String name;
            try {
                name = data.getString("displayName");
            } catch (NullPointerException e) {
                String p = data.getFilePath().getPath();
                if (!_alreadyErroredKeys.contains(p)) {
                    System.out.println("<!> '" + data.getFilePath().getPath() + "' MISSING PROPERTY \"displayName\"");
                    _alreadyErroredKeys.add(p);
                    System.out.println("-> INTERNAL POLICTY: THIS ERROR WILL ONLY BE SHOWN ONCE.");
                }
                name = "[CORRUPTED] !" + data.getFilePath().getName();
            }
            if (!_openWindows.containsKey(name)) {
                continue;
            }
            boolean open = _openWindows.get(name);

            if (open) {
                ImGui.begin(name);
                ImGui.setCursorPosX(ImGui.getWindowSizeX() - 40);
                if (ImGui.button("X", 20, 20)) {
                    _openWindows.put(name, false);
                }

                ImGui.sameLine();
                ImGui.setCursorPosX(10);
                ImGui.text(data.getString("description"));
                ImGui.newLine();

                HashMap<String, String> internal = (HashMap<String, String>) data.get("data");
                String finalName = name;
                AtomicInteger counter = new AtomicInteger();
                internal.forEach((key, value) -> {
                    counter.addAndGet(1);
                    ImString txt = new ImString(value.toString(), 255);
                    if (!_internalData.containsKey(finalName)) {
                        Map<String, ImString> map = new HashMap<>();
                        map.put(key, txt);
                        _internalData.put(finalName, map);
                    } else if (!_internalData.get(finalName).containsKey(key)) {
                        Map<String, ImString> map = _internalData.get(finalName);
                        map.put(key, txt);
                        _internalData.put(finalName, map);
                    }
                    ImGui.text(key.toString());
                    ImGui.inputText("##" + counter.get(), _internalData.get(finalName).get(key));
                    ImGui.sameLine();
                    if (ImGui.button("Copy " + counter.get())) {
                        ImGui.setClipboardText(_internalData.get(finalName).get(key).toString());
                        System.out.println("Copied text to clipboard");
                    }
                });

                if (ImGui.button("Save Changes")) {
                    HashMap<String, String> internalData = (HashMap<String, String>) data.get("data");
                    internalData.forEach((key, value) -> {
                        internalData.put(key, _internalData.get(finalName).get(key).toString());
                    });
                    data.set("data", internalData);
                }
                ImGui.sameLine();
                if (ImGui.button("Add Property")) {
                    openModal = true;
                    nextData = data;
                }
                ImGui.sameLine();

                ImGui.end();
            }
        }
    }
}
