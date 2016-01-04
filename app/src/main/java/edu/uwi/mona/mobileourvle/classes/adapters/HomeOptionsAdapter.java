package edu.uwi.mona.mobileourvle.classes.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.uwi.mona.mobileourvle.R;


/**
 * Created by javon_000 on 09/06/2015.
 */
public class HomeOptionsAdapter extends BaseAdapter {

    private Context mContext;
    private String[] options;

    public HomeOptionsAdapter(Context context)
    {
        mContext = context;
        options = mContext.getResources().getStringArray(R.array.options);
    }

    @Override
    public int getCount() {
        return options.length;
    }

    @Override
    public Object getItem(int i) {
        return options[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView = view;

        OptionHolder holder;

        if(mView==null)
        {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            mView = inflater.inflate(R.layout.home_option,viewGroup,false);

            holder = new OptionHolder();

            holder.option = (TextView) mView.findViewById(R.id.option);
            holder.option.setText((String) getItem(i));

        }
        else
        {
            holder = (OptionHolder) mView.getTag();
        }


        return mView;
    }

    static class OptionHolder
    {
        TextView option;
        //TextView notification;
    }
}
