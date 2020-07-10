package com.smartsoftwaresolutions.ifix.country_spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartsoftwaresolutions.ifix.R;

import java.util.List;

public class spinnerAdapter extends BaseAdapter {
    private List<country_spinner> SpinnerList;
    private Context context;
    LayoutInflater inflter;

    public spinnerAdapter(List<country_spinner> spinnerList, Context ctx) {
        //super(ctx, R.layout.user_type_spinner, spinnerList);
        this.SpinnerList = spinnerList;
        this.context = ctx;
        inflter = (LayoutInflater.from(ctx));


    }
    public int getCount() {
        return SpinnerList.size();
    }

    public country_spinner getItem(int position) {
        return SpinnerList.get(position);
    }

    public long getItemId(int position) {
        return SpinnerList.get(position).hashCode();
    }
    //each time a row is inserted the listview will call the getview method
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
// we creat a view holder
        spinnerAdapter.PayInfoHolder holder = new spinnerAdapter.PayInfoHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.country_spinner, null);
            // Now we can fill the layout with the right values
            TextView Id = (TextView) v.findViewById(R.id.sp_country_id);
            TextView name = (TextView) v.findViewById(R.id.sp_country_name);

            holder.country_id = Id;

            holder.country_name = name;

            v.setTag(holder);
        }
        else
            holder = (spinnerAdapter.PayInfoHolder) v.getTag();
        // we use it to get the information of the element from customer info calss
        country_spinner p =SpinnerList.get(position);
        // we read from the list and put it in the text view
        holder.country_id.setText(p.getCountry_id());
        holder.country_name.setText(p.getCountry_name());



        return v;
    }

    /* *********************************
     * We use the holder pattern
     * It makes the view faster and avoid finding the component
     * **********************************/
    private static class PayInfoHolder {
        public TextView country_id;
        public TextView country_name;
        public TextView country_name_ar;
        public TextView country_code;

    }
}
