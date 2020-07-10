package com.smartsoftwaresolutions.ifix.secret_ans_spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartsoftwaresolutions.ifix.R;


import java.util.List;

public class ans_adapter extends BaseAdapter {
    private List<ans_spinner> SpinnerList;
    private Context context;
    LayoutInflater inflter;

    public ans_adapter(List<ans_spinner> spinnerList, Context ctx) {

        this.SpinnerList = spinnerList;
        this.context = ctx;
        inflter = (LayoutInflater.from(ctx));


    }
    public int getCount() {
        return SpinnerList.size();
    }

    public ans_spinner getItem(int position) {
        return SpinnerList.get(position);
    }

    public long getItemId(int position) {
        return SpinnerList.get(position).hashCode();
    }
    //each time a row is inserted the listview will call the getview method
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
// we creat a view holder
        ans_adapter.PayInfoHolder holder = new ans_adapter.PayInfoHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.ans_item, null);
            // Now we can fill the layout with the right values

            TextView name = (TextView) v.findViewById(R.id.tv_ans);



            holder.ans_name = name;

            v.setTag(holder);
        }
        else
            holder = (ans_adapter.PayInfoHolder) v.getTag();
        // we use it to get the information of the element from customer info calss
        ans_spinner p =SpinnerList.get(position);

        holder.ans_name.setText(p.getQuestion());



        return v;
    }

    /* *********************************
     * We use the holder pattern
     * It makes the view faster and avoid finding the component
     * **********************************/
    private static class PayInfoHolder {

        public TextView ans_name;


    }
}
