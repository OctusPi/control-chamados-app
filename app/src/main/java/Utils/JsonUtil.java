package Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public class JsonUtil {

    public static ArrayList<JSONObject> jsonList(JSONArray jsonArray) throws JSONException {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObjects.add(jsonObject);
        }

        return jsonObjects;
    }

    public static String getJsonVal(JSONObject obj, String key) {
        try {
            return !obj.getString(key).equals("null") ? obj.getString(key) : "***";
        } catch (JSONException e) {
            e.printStackTrace();
            return "Dado Não Localizado";
        }
    }

    public static SortedMap<Integer, ArrayList<String>> mapJsonPropObject(JSONObject jsonObject) {
        Iterator<String> jsonObjectKey = jsonObject.keys();
        SortedMap<Integer, ArrayList<String>> jsonMap = new TreeMap<>();

        try {
            for (int i = 1; i <= jsonObject.length(); i++) {
                ArrayList<String> arrayList = new ArrayList<>();
                String key = jsonObjectKey.next();

                arrayList.add(key);
                arrayList.add(jsonObject.getString(key));

                jsonMap.put(i, arrayList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonMap;
    }

    public static JSONObject getJsonObject(JSONObject obj, String key) {
        try {
            return obj.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
