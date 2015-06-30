package edu.uwi.mona.mobileourvle.app.Classes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.uwi.mona.mobileourvle.app.Classes.options.OptionContent;
import edu.uwi.mona.mobileourvle.app.R;

/**
 * Created by javon_000 on 09/06/2015.
 */
public class HomeOptionsAdapter extends BaseAdapter {

    private Context mContext;

    public HomeOptionsAdapter(Context context)
    {
        mContext = context;
    }

    @Override
    public int getCount() {
        return OptionContent.getCount();
    }

    @Override
    public Object getItem(int i) {
        return OptionContent.getOption(i);
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
            holder.option.setText(((OptionContent.Option) getItem(i)).content);

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
