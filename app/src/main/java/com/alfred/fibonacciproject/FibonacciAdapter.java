package com.alfred.fibonacciproject;

/**
 * Created by baojia on 2016/3/23.
 */
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FibonacciAdapter extends BaseAdapter {

    private Context context;
    private List<BigInteger> list = new ArrayList<BigInteger>();

    public FibonacciAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<BigInteger> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fibonacci, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(String.valueOf(list.get(position)));

        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }

}

