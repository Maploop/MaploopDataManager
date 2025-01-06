package me.leon.intel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.util.Map;

public class SHENDataObject {
    private final File filePath;
    private JSONObject jsonObject;

    public SHENDataObject() {
        filePath = null;
        jsonObject = new JSONObject();
    }

    public SHENDataObject(File file) {
        filePath = file;

        if (!file.exists()) {
            jsonObject = new JSONObject();
            return;
        }
        try {
            String dcc = DataEncoder.DEC_BIN_(CUtil.readFromFile(file));

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

    public SHENDataObject put(String key, Object value) {
        jsonObject.put(key, value);
        return this;
    }

    public SHENDataObject set(String key, Object value) {
        return put(key, value);
    }

    public SHENDataObject append(String key, Object value) {
        return put(key, value);
    }

    public SHENDataObject save() {
        if (!filePath.exists()) {
            try {
                filePath.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String jsonString = jsonObject.toJSONString();
        CUtil.writeToFile(filePath, DataEncoder.ENC_BIN_(jsonString));
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
