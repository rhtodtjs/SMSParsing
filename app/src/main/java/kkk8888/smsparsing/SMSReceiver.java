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

/**
 * Created by alfo06-18 on 2017-09-28.
 */

public class SMSReceiver extends BroadcastReceiver {

    RequestQueue queue;
    Context qc;
    String php = "http://ec2-13-124-92-145.ap-northeast-2.compute.amazonaws.com/receive.php";

    String number1, message1;


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

            if (temp == null)
                Toast.makeText(context, "Unsaved Phone Number", Toast.LENGTH_SHORT).show();

            if (sender.contains(temp)) {
                queue = Volley.newRequestQueue(context);
                qc = context;
                SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, php, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(qc, "저장 완료", Toast.LENGTH_SHORT).show();
                        Log.i("확인해", response);

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

                queue.add(smpr);


//                new Thread() {
//                    @Override
//                    public void run() {
//
//                        try {
//                            URL url = new URL(php);
//                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                            connection.setDoOutput(true);
//                            connection.setDoInput(true);
//                            connection.setUseCaches(false);
//                            connection.setRequestMethod("POST");
//
//                            number1 = URLEncoder.encode(number1, "utf-8");
//                            message1 = URLEncoder.encode(message1, "utf-8");
//
//                            String data = "number=" + number1 + "&message=" + message1;
//
//                            OutputStream os = connection.getOutputStream();
//                            os.write(data.getBytes());
//                            os.flush();
//                            os.close();
//
//                            InputStream is = connection.getInputStream();
//                            InputStreamReader isr = new InputStreamReader(is);
//                            BufferedReader reader = new BufferedReader(isr);
//                            final StringBuffer buffer = new StringBuffer();
//
//                            String line = reader.readLine();
//
//                            while (line != null) {
//
//                                buffer.append(line);
//                                line = reader.readLine();
//                            }
//
//
//                            Log.i("tlqkf", line.toString());
//
//
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();

            }
        } else if (MainActivity.USER_DEFINED_MSG.equals(intent.getAction())) { // 사용자가 요구한 경우

            Toast.makeText(context, intent.getStringExtra("data"), Toast.LENGTH_SHORT).show();
        }
    }//onReceive


}
