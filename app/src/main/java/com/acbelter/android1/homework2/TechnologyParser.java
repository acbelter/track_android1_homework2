package com.acbelter.android1.homework2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TechnologyParser {
    public static List<TechnologyItem> parse(String data) throws JSONException {
        if (data == null) {
            return new ArrayList<>(0);
        }

        JSONObject techJson = new JSONObject(data).getJSONObject("technology");
        List<TechnologyItem> result = new ArrayList<>();
        Iterator<String> idIterator = techJson.keys();
        while (idIterator.hasNext()) {
            JSONObject techObj = techJson.getJSONObject(idIterator.next());
            TechnologyItem techItem = new TechnologyItem();
            techItem.id = techObj.getInt("id");
            techItem.picture = techObj.getString("picture");
            techItem.title = techObj.getString("title");
            techItem.info = techObj.optString("info");
            result.add(techItem);
        }

        return result;
    }
}
