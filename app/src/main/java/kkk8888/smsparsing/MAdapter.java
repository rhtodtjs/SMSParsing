package kkk8888.smsparsing;

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by home on 2017-09-17.
 */

public class MAdapter extends BaseAdapter {

    ArrayList<LittleMSG> list;
    Context mContext;

    public MAdapter(ArrayList<LittleMSG> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem, parent, false);
        TextView number = (TextView) view.findViewById(R.id.number);
        TextView content = (TextView)view.findViewById(R.id.content);
        TextView time = (TextView)view.findViewById(R.id.time);
        TextView did = (TextView)view.findViewById(R.id.did) ;

        number.setText(list.get(position).who);
        content.setText(list.get(position).msg);
        time.setText(list.get(position).getNow());
        did.setText(list.get(position).getDid());




        return view;
    }
}
