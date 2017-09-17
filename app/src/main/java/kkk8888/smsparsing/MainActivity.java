package kkk8888.smsparsing;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Message msg;
    ArrayList<Message> arrayList = new ArrayList<>();

    boolean progressing = false;
    ListView listView;
    mThread t;
    MAdapter adapter;
    TextView status;

    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new MAdapter(arrayList, this);
        listView.setAdapter(adapter);
        status = (TextView) findViewById(R.id.status);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, 10);

            } else {


            }
        }

    }

    public void start(View v) {

        refresh(v);

        t = new mThread();
        progressing = true;
        t.start();


    }

    public void stop(View v) {

        synchronized (t) {
            progressing = false;
        }

        status.setText("중지");


    }

    public void refresh(View v) {

        arrayList.clear();
        adapter.notifyDataSetChanged();

    }

    class mThread extends Thread {
        @Override
        public void run() {

            while (progressing) {
                startParsing();

                if (progressing) {
                    break;
                }
            }
        }
    }


    void startParsing() {

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
            arrayList.add(msg); //이부분은 제가 arraylist에 담으려고 하기떄문에 추가된부분이며 수정가능합니다.

            // }



        }

        cnt++;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                status.setText("진행중........." + cnt);
                if (cnt >= 100000) cnt = 0;
            }
        });
    }
}
