package kkk8888.smsparsing;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    final static String USER_DEFINED_MSG = "com.appstudio.android.msg";

    Message msg;
    ArrayList<LittleMSG> arrayList = new ArrayList<>();

    boolean progressing = false;
    ListView listView;
    MAdapter adapter;
    EditText status, uri, user, pass, dbname, portnum, code , url;

    public static Context MContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new MAdapter(arrayList, this);
        listView.setAdapter(adapter);
        status = (EditText) findViewById(R.id.status);
        uri = (EditText) findViewById(R.id.uri);
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);
        dbname = (EditText) findViewById(R.id.dbname);
        portnum = (EditText) findViewById(R.id.portnum);
        code = (EditText) findViewById(R.id.code);
        url = (EditText)findViewById(R.id.url);

        MContext = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, 10);

            } else {


            }
        }


        SharedPreferences load = getSharedPreferences("save", MODE_PRIVATE);

        if (!load.getString("num", "").equals("")) {
            String temp = load.getString("num", "");
            String temp1 = load.getString("uri", "");
            String temp2 = load.getString("user", "");
            String temp3 = load.getString("pass", "");
            String temp4 = load.getString("dbname", "");
            String temp5 = load.getString("portnum", "");
            String temp6 = load.getString("code", "");
            String temp7 = load.getString("url","");

            status.setText(temp);
            uri.setText(temp1);
            user.setText(temp2);
            pass.setText(temp3);
            dbname.setText(temp4);
            portnum.setText(temp5);
            code.setText(temp6);
            url.setText(temp7);

        }


    }

    public void save(View v) {

        if (status.getText().toString().equals("")) return;

        SharedPreferences sp = getSharedPreferences("save", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("num", status.getText().toString());
        editor.putString("uri", uri.getText().toString());
        editor.putString("user", user.getText().toString());
        editor.putString("pass", pass.getText().toString());
        editor.putString("dbname", dbname.getText().toString());
        editor.putString("portnum", portnum.getText().toString());
        editor.putString("code", code.getText().toString());
        editor.putString("url",url.getText().toString());
        editor.commit();

        Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show();
    }

    public void start(View v) {


        new Thread() {
            @Override
            public void run() {
                Uri allMessage = Uri.parse("content://sms");
                ContentResolver cr = getContentResolver();
                Cursor c = cr.query(allMessage,
                        new String[]{"_id", "thread_id", "address", "person", "date", "body"},
                        null, null,
                        "date DESC");

                while (c.moveToNext()) {
                    msg = new Message(); // 따로 저는 클래스를 만들어서 담아오도록 했습니다.

                    long messageId = c.getLong(0);
                    msg.setMessageId(String.valueOf(messageId));

                    long threadId = c.getLong(1);
                    msg.setThreadId(String.valueOf(threadId));

                    String address = c.getString(2);


                    msg.setAddress(address);

                    long contactId = c.getLong(3);
                    msg.setContactId(String.valueOf(contactId));

                    String contactId_string = String.valueOf(contactId);
                    msg.setContactId_string(contactId_string);

                    long timestamp = c.getLong(4);
                    msg.setTimestamp(String.valueOf(timestamp));

                    String body = c.getString(5);
                    msg.setBody(body);

                    // if(address.contains("0977")){
                    //arrayList.add(msg); //이부분은 제가 arraylist에 담으려고 하기떄문에 추가된부분이며 수정가능합니다.

                    // }


                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();

                    }
                });
            }
        }.start();
    }

    public void stop(View v) {


        Intent serviceIntent = new Intent();
        serviceIntent.setAction(USER_DEFINED_MSG); // 엑션 셋
        serviceIntent.putExtra("data", "user defined msg");
        sendBroadcast(serviceIntent);


    }

    public void refresh(View v) {


        SMSReceiver smsReceiver = new SMSReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(USER_DEFINED_MSG);


        registerReceiver(smsReceiver, filter);


        arrayList.clear();


    }

    public void addList(LittleMSG message) {

        arrayList.add(message);
        adapter.notifyDataSetChanged();
    }


}
