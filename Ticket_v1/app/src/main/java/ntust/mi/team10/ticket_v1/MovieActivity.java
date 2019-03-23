package ntust.mi.team10.ticket_v1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MovieActivity extends AppCompatActivity {

    public static final String HOST = "https://jaspersui.pw/api/";
    public static final String FORMAT = "/?format=json";

    public static final String getTicket = "ticket_type";
    public static final String buyTicket = "ticket";

    aMovie thisMovie;
    String url;
    String selectedId;
    String selectedName;
    String selectedNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        //===================================== 抓取主頁傳送資料 =====================================
        Intent intent = getIntent();
        int intent_id = intent.getIntExtra("id",0);
        TextView label_id = (TextView) findViewById(R.id.label_movie_id);
        label_id.setText(String.valueOf(intent_id));
        //===================================== 抓取主頁傳送資料 =====================================

        url  = HOST + getTicket + "/"+ intent_id + FORMAT;
        pageLinkData();
    }

    public static JSONObject getTheMovie(ArrayList<JSONObject> jObject, int id) throws JSONException {
        for(int i = 0; i < jObject.size(); i ++){
            int thisID = Integer.parseInt(jObject.get(i).get("id").toString());
            if(thisID == id){
                return jObject.get(i);
            }
        }

        return null;
    }

    public void pageLinkData(){
        //===================================== 抓取資料 =====================================
        MyStringRequest request = new MyStringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                ArrayList<JSONObject> jObject = null;
                try {
                    jObject = new getJson().parseJson(s);
                    TextView label_id = (TextView)findViewById(R.id.label_movie_id);
                    String id = label_id.getText().toString();
                    aMovie movie = new aMovie(getTheMovie(jObject,Integer.parseInt(id)));
                    thisMovie = movie;
                    setLabelText(movie);
                    buildSpinner();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });


        RequestQueue rQueue = Volley.newRequestQueue(MovieActivity.this);
        rQueue.add(request);
        //===================================== 抓取資料 =====================================
    }

    public void setLabelText(aMovie movie){

        TextView label_name = (TextView)findViewById(R.id.label_movie_ticket);
        TextView label_date_start = (TextView)findViewById(R.id.label_movie_start);
        TextView label_date_end = (TextView)findViewById(R.id.label_movie_stop);
        TextView label_seat = (TextView)findViewById(R.id.label_movie_seat);

        selectedId = String.valueOf(movie.mId);
        //label_id.setText(movie.mId);
        label_name.setText(movie.mName);
        selectedName = movie.mName;
        label_date_start.setText(movie.mTime_start_str);
        label_date_end.setText(movie.mTime_end_str);
        label_seat.setText(movie.mSeat_remain + "/" + movie.mSeat_number);
    }

    public void buildSpinner(){
        //取得剩餘位置數
        TextView label_seat = (TextView)findViewById(R.id.label_movie_seat);
        String strSeat = label_seat.getText().toString();
        int peopleRemain = Integer.parseInt(strSeat.substring(0,strSeat.indexOf("/")));

        //建立下拉式選單
        Spinner spinner = (Spinner)findViewById(R.id.spinner_movie_number);
        ArrayList<String> stringArrayList = new ArrayList<String>();
        for(int i = 1; i <= peopleRemain; i++){
            stringArrayList.add(String.valueOf(i));
        }
        ArrayAdapter<String> pList = new ArrayAdapter<>(MovieActivity.this, android.R.layout.simple_spinner_dropdown_item, stringArrayList);
        spinner.setAdapter(pList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStr = parent.getItemAtPosition(position).toString();
                //Toast.makeText(DisplayActivity.this, "你選的是" + selectedStr, Toast.LENGTH_SHORT).show();
                selectedNumber = selectedStr;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void btBuy_cancel(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void btBuy_sent(View view) {
        Date now = new Date();
        if(now.after(thisMovie.mTime_start_date) && now.before(thisMovie.mTime_end_date)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("購票確認");
            builder.setMessage("您目前設定的購票資訊如下\n購票名稱：" + selectedName + "\n購票張數：" + selectedNumber + "\n\n您確定要購買嗎？");
            builder.setCancelable(false);
            builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    postData();
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "已取消購票", Toast.LENGTH_SHORT).show();
                }
            });

            builder.show();
        }else{
            Toast.makeText(getApplicationContext(), "此票目前無法購買，請於購票期間再行操作，謝謝", Toast.LENGTH_LONG).show();
        }
    }

    public void postData(){
        String url = HOST + buyTicket + "/";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("请求结果:" ,response);
                Toast.makeText(getApplicationContext(), "購票成功", Toast.LENGTH_LONG).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("请求错误:", error.toString());
                Toast.makeText(getApplicationContext(), "購票失敗", Toast.LENGTH_LONG).show();
                pageLinkData();
            }
        }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                String buyTime = getPythonTime();
                String query = "{\"ticket_type_id\":"+ selectedId + ",\"number_of_people\":"+ selectedNumber + ",\"buy_time\":\""+ buyTime + "\"} ";
                return query.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

        RequestQueue rQueue = Volley.newRequestQueue(MovieActivity.this);
        rQueue.add(request);
    }

    public static String getPythonTime(){
        return (new SimpleDateFormat("yyyy-MM-dd").format(new Date())) + "T" + (new SimpleDateFormat("HH:mm:ss").format(new Date())) + "Z";
    }
}
