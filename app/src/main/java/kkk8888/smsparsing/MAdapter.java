package kkk8888.smsparsing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by home on 2017-09-17.
 */

public class MAdapter extends BaseAdapter {

    ArrayList<Message> list;
    Context mContext;

    public MAdapter(ArrayList<Message> list, Context mContext) {
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

        number.setText(list.get(position).getAddress());
        content.setText(list.get(position).getBody());
        time.setText(list.get(position).getTimestamp());

        return view;
    }
}
