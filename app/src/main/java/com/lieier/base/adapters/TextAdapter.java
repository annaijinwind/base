package com.lieier.base.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lieier.base.R;
import com.lieier.base.bean.TextBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengjianqi on 2016/7/7.
 */
public class TextAdapter extends BaseAdapter{
    private List<TextBean> listData=new ArrayList<TextBean>();
    private Context context;
    private TextBean tb;

    public TextAdapter(Context context, List<TextBean> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listData.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return listData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHolder holder=null;
        tb=listData.get(arg0);
        if (arg1==null) {
            arg1= LayoutInflater.from(context).inflate(R.layout.text_item,null);
            holder=new ViewHolder(arg1);
            arg1.setTag(holder);
        }else {
            holder = (ViewHolder) arg1.getTag();
        }
        holder.text.setText(tb.getText());
        DraweeController draweeController1 = Fresco.newDraweeControllerBuilder().setUri(Uri.parse(tb.getUrl()))
                .setAutoPlayAnimations(true).build();
        holder.img1.setController(draweeController1);

        return arg1;
    }

    class ViewHolder{
        private SimpleDraweeView img1;
        private TextView text;
        ViewHolder(View v){
            img1=(SimpleDraweeView) v.findViewById(R.id.img1);
            text=(TextView) v.findViewById(R.id.text);

        }
    }
}
