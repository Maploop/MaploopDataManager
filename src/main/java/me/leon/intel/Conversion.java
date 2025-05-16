package me.leon.intel;

import imgui.type.ImString;
import org.json.simple.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Conversion {
    public static boolean attemptingChangePasscode = false;

    public static boolean convert(String newKey) {
        attemptingChangePasscode = true;
        try {
            DataHandler.Load();
            DataHandler.UpdateList();
        } catch (IllegalStateException e) {
            attemptingChangePasscode = false;
            return false;
        }
        List<SHENDataObjectV2> currentDataList = new ArrayList<>(DataHandler._dataObjects);
        DataHandler._dataObjects.clear();

        SecurityV2.key.set(newKey);
        for (SHENDataObjectV2 dataV2 : currentDataList) {
            SHENDataObjectV2 data = new SHENDataObjectV2(dataV2.getFilePath(), dataV2.getJsonObject());
            data.save();
            DataHandler._dataObjects.add(data);
            System.out.println("Switched Encryption of " + dataV2.getFilePath().getName());
        }
        return true;
    }
}
