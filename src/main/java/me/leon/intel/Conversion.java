package me.leon.intel;

import imgui.type.ImString;
import org.json.simple.JSONArray;

import java.io.File;

public class Conversion {
    public static void main(String[] args) {
        SecurityV2.key = new ImString("ekko");
        // DataHandler.Load();

        for (SHENDataObjectV2 data : DataHandler._dataObjects) {
//            SHENDataObjectV2 dataV2 = new SHENDataObjectV2(data.getFilePath(), data.getJsonObject());
//            dataV2.save();
//            System.out.println("Saved " + data.getFilePath().getName());
        }
    }
}
