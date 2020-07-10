package com.smartsoftwaresolutions.ifix.Sub_country_spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartsoftwaresolutions.ifix.R;
import com.smartsoftwaresolutions.ifix.country_spinner.country_spinner;
import com.smartsoftwaresolutions.ifix.country_spinner.spinnerAdapter;

import java.util.List;

public class Sub_country_spinner_adapter extends BaseAdapter {
    private List<Sub_country_spinner> SpinnerList;
    private Context context;
    LayoutInflater inflter;
    public Sub_country_spinner_adapter(List<Sub_country_spinner> spinnerList, Context ctx) {
        //super(ctx, R.layout.user_type_spinner, spinnerList);
        this.SpinnerList = spinnerList;
        this.context = ctx;
        inflter = (LayoutInflater.from(ctx));


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
            v = inflater.inflate(R.layout.country_spinner, null);
            // Now we can fill the layout with the right values
            TextView Id = (TextView) v.findViewById(R.id.sp_country_id);
            TextView name = (TextView) v.findViewById(R.id.sp_country_name);

            holder.country_id = Id;

            holder. CS_Name = name;

            v.setTag(holder);
        }
        else
            holder = (PayInfoHolder) v.getTag();
        // we use it to get the information of the element from customer info calss
        Sub_country_spinner p =SpinnerList.get(position);
        // we read from the list and put it in the text view
        holder.country_id.setText(p.getCountry_id());
        holder. CS_Name.setText(p.getCS_Name());




        return v;
    }
    /* *********************************
     * We use the holder pattern
     * It makes the view faster and avoid finding the component
     * **********************************/
    private static class PayInfoHolder {

        public TextView CS_ID; // id of the contry that the sub country belong to
        public TextView CS_Name;
        public TextView CS_name_ar;
        public TextView country_id;

    }
}
