package me.leon.intel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.util.Map;

public class SHENDataObjectV2 {
    private final File filePath;
    private JSONObject jsonObject;

    public SHENDataObjectV2() {
        filePath = null;
        jsonObject = new JSONObject();
    }

    public SHENDataObjectV2(File file, JSONObject obj) {
        filePath = file;
        jsonObject = obj;
    }

    public SHENDataObjectV2(File file) {
        filePath = file;

        if (!file.exists()) {
            jsonObject = new JSONObject();
            return;
        }
        try {
            String dcc = SecurityV2.decode(CUtil.readFromFile(file));

            jsonObject = (JSONObject) new JSONParser().parse(dcc);
        } catch (Exception e) {
            jsonObject = new JSONObject();
        }
    }

    public File getFilePath() {
        return this.filePath;
    }

    public Map<Object, Object> getMappedObject() {
        return jsonObject;
    }

    public SHENDataObjectV2 put(String key, Object value) {
        jsonObject.put(key, value);
        return this;
    }

    public SHENDataObjectV2 set(String key, Object value) {
        return put(key, value);
    }

    public SHENDataObjectV2 append(String key, Object value) {
        return put(key, value);
    }

    public SHENDataObjectV2 save() {
        if (!filePath.exists()) {
            try {
                filePath.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String jsonString = jsonObject.toJSONString();
        CUtil.writeToFile(filePath, SecurityV2.encode(jsonString));
        return this;
    }

    public Object get(String key) {
        return jsonObject.get(key);
    }

    public JSONArray getArray(String key) {
        return (JSONArray) jsonObject.get(key);
    }

    public JSONObject getJsonObject(String key) {
        return (JSONObject) jsonObject.get(key);
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public int getInt(String k) {
        return Integer.parseInt(jsonObject.get(k).toString());
    }

    public long getLong(String k) {
        return Long.getLong(jsonObject.get(k).toString());
    }

    public double getDouble(String k) {
        return Double.parseDouble(jsonObject.get(k).toString());
    }

    public float getFloat(String k) {
        return Float.parseFloat(jsonObject.get(k).toString());
    }

    public String getString(String k) {
        return jsonObject.get(k).toString();
    }

    public boolean getBoolean(String k) {
        return Boolean.parseBoolean(getString(k));
    }
}
