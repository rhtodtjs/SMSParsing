package kkk8888.smsparsing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alfo06-18 on 2017-09-28.
 */

public class SMSReceiver extends BroadcastReceiver {

    RequestQueue queue;
    Context qc;
    String php ="";
    String number1, message1;
    MainActivity ma;


    @Override
    public void onReceive(Context context, Intent intent) {
        // 메세지가 온 경우
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            String message = ""; // 문자 메세지 내용
            String sender = ""; // 문자 보낸 사람 핸드폰 번호

            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (Object pdu : pdusObj) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    message += smsMessage.getMessageBody(); // 메세지 내용 받아오기

                    if (sender != null) {
                        sender = smsMessage.getOriginatingAddress(); // 메세지 보낸사람 핸드폰번호
                    }//if
                }//for
            }//if
            //Toast.makeText(context, message + "\n:" + sender, Toast.LENGTH_SHORT).show();

            number1 = sender;
            message1 = message;

            SharedPreferences load = context.getSharedPreferences("save", context.MODE_PRIVATE);
            String temp = load.getString("num", "");
            String temp1 = load.getString("uri", "");
            String temp2 = load.getString("user", "");
            String temp3 = load.getString("pass", "");
            String temp4 = load.getString("dbname", "");
            String temp5 = load.getString("portnum", "");
            String temp6 = load.getString("code", "");

            php = load.getString("url","");

            ma = (MainActivity) MainActivity.MContext;

            if (temp == null)
                Toast.makeText(context, "Unsaved Phone Number", Toast.LENGTH_SHORT).show();

            String[] fuck = temp.split(",");

            queue = Volley.newRequestQueue(context);
            qc = context;

            //log.d

            for (int i = 0; i < fuck.length; i++) {
                Log.i("키키", fuck[i]);

                if (sender.contains(fuck[i])) {

                    SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, php, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //Toast.makeText(qc, response, Toast.LENGTH_SHORT).show();
                            //Log.i("확인해", response + "임");
                            Date date = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("mm-dd hh:mm:ss");
                            if (response.equals("0")) {
                                ma.addList(new LittleMSG(number1, message1, sdf.format(date), "성공"));
                            } else if (response.equals("1")) {
                                ma.addList(new LittleMSG(number1, message1, sdf.format(date), "실패"));

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(qc, "에러.." + error, Toast.LENGTH_SHORT).show();
                            Log.i("에러메세지", error + "");
                        }
                    });


                    smpr.addStringParam("number", sender);
                    smpr.addStringParam("message", message);

                    smpr.addStringParam("uri", temp1);
                    smpr.addStringParam("user", temp2);
                    smpr.addStringParam("pass", temp3);
                    smpr.addStringParam("dbname", temp4);
                    smpr.addStringParam("port", temp5);
                    smpr.addStringParam("code", temp6);

                    queue.add(smpr);


                }
            }


        } else if (MainActivity.USER_DEFINED_MSG.equals(intent.getAction())) { // 사용자가 요구한 경우

            Toast.makeText(context, intent.getStringExtra("data"), Toast.LENGTH_SHORT).show();
        }
    }//onReceive


}
