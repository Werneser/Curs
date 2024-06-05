package com.example.curs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TestAdapter extends BaseAdapter {
    private Context context;
    private List<Test> testList;

    public TestAdapter(Context context, List<Test> testList) {
        this.context = context;
        this.testList = testList;
    }

    @Override
    public int getCount() {
        return testList.size();
    }

    @Override
    public Object getItem(int position) {
        return testList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        Test test = testList.get(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(test.testName);

        return convertView;
    }
}


