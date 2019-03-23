package ntust.mi.team10.ticket_v1;

import java.util.ArrayList;
import org.json.*;

public class getJson {

    public ArrayList<JSONObject> parseJson(String str) throws JSONException{
        JSONArray jArray;
        try{
            jArray= new JSONArray(str);
        }catch(Exception e){
            jArray = new JSONArray("[" + str + "]");
        }

        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        for(int i = 0; i < jArray.length(); i ++){
            list.add(jArray.getJSONObject(i));
        }

        // JSONObject[] jObject = new JSONObject(jArray);
        //return jObject;

        return list;
    }
}