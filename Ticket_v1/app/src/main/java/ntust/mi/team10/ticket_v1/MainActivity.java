package ntust.mi.team10.ticket_v1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;

import org.json.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String HOST = "https://jaspersui.pw/api/";
    public static final String FORMAT = "/?format=json";

    public static final String getTicket = "ticket_type";

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = HOST + getTicket + FORMAT;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();


        //===================================== 抓取資料 =====================================
        MyStringRequest request = new MyStringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Log.d("Testing Message-GetURL", s);


                ArrayList<JSONObject> jObject = null;
                try {
                    jObject = new getJson().parseJson(s);
                    bt_Link_MovieData(jObject);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });


        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);

        //===================================== 抓取資料 =====================================

    }

    public void bt_Link_MovieData(ArrayList<JSONObject> jObject) throws JSONException{
        for(int i = 0; i < jObject.size(); i++){
            aMovie movie = new aMovie(jObject.get(i));

            Button bt = (Button) findViewById(getResources().getIdentifier("bt_movie_" + (i+1), "id",
                    this.getPackageName()));

            bt.setText(movie.mName);
        }
    }


    public void click_bt(View view){
        int btId = 0;
        switch (view.getId()){
            case R.id.bt_movie_1:
                btId = 1;
                break;
            case R.id.bt_movie_2:
                btId = 2;
                break;
            case R.id.bt_movie_3:
                btId = 3;
                break;
            case R.id.bt_movie_4:
                btId = 4;
                break;
            case R.id.bt_movie_5:
                btId = 5;
                break;
            case R.id.bt_movie_6:
                btId = 6;
                break;
        }

        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("id",btId );
        startActivity(intent);
    }
}
