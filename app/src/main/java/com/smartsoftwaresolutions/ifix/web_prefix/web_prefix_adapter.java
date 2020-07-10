package com.smartsoftwaresolutions.ifix.web_prefix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartsoftwaresolutions.ifix.R;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner;
import com.smartsoftwaresolutions.ifix.Sub_country_spinner.Sub_country_spinner_adapter;

import java.util.List;

public class web_prefix_adapter extends BaseAdapter {
    List<web_info> SpinnerList;
    Context context;
    LayoutInflater inflater;

    public web_prefix_adapter(List<web_info> spinner_list, Context context){
        this.SpinnerList = spinner_list;
        this.context = context;
        inflater = (LayoutInflater.from(context));

    }
    @Override
    public int getCount() {
        return SpinnerList.size();
    }

    @Override
    public Object getItem(int position) {
        return SpinnerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return SpinnerList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
// we creat a view holder
        PayInfoHolder holder = new PayInfoHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.web_prefix, null);
            // Now we can fill the layout with the right values
            TextView web_prefix = (TextView) v.findViewById(R.id.tv_web_prefix);

            holder.web_prefix = web_prefix;




            v.setTag(holder);
        }
        else
            holder = (PayInfoHolder) v.getTag();
        // we use it to get the information of the element from customer info calss
       web_info p =SpinnerList.get(position);
        holder. web_prefix.setText(p.getWeb_prefix());




        return v;
    }
    private static class PayInfoHolder {

        public TextView web_prefix; // id of the contry that the sub country belong to


    }
}
