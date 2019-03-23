package ntust.mi.team10.ticket_v1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;


public class aMovie {
    int mId;
    String mName;
    String mPrice;
    String mTime_start_str;
    String mTime_end_str;
    Date mTime_start_date;
    Date mTime_end_date;
    String mSeat_number;
    String mSeat_remain;


    public aMovie(JSONObject jdata) throws JSONException{
        mId = jdata.getInt("id");
        mName = jdata.getString("name");
        mPrice = jdata.getString("price");

        String strData_st = jdata.getString("begin_time").replace("T", " ").replace("Z", "");
        String strData_en = jdata.getString("end_time").replace("T", " ").replace("Z", "");

        try {
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            mTime_start_date = sdf2.parse(strData_st);
            mTime_end_date = sdf2.parse(strData_en);

            mTime_start_str = dateFormat2.format(mTime_start_date);
            mTime_end_str = dateFormat2.format(mTime_end_date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSeat_number = jdata.getString("number_of_seat");
        mSeat_remain = jdata.getString("remaining_seat");
    }

}