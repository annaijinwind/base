package com.lieier.base.adapters;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.lieier.base.bean.TextBean;

import java.util.ArrayList;
import java.util.List;

import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by fengjianqi on 2016/7/22.
 */
public class SimpleViewPagerAdapter extends PagerAdapter{
    private List<TextBean> listData=new ArrayList<TextBean>();
    private ViewPager mViewPager;
   private Context context;
    public SimpleViewPagerAdapter(ViewPager mViewPager, List<TextBean> listData,Context context) {
        this.mViewPager = mViewPager;
        this.listData = listData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        final PhotoDraweeView photoDraweeView = new PhotoDraweeView(container.getContext());
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(Uri.parse(listData.get(position).getUrl()));
        controller.setOldController(photoDraweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        photoDraweeView.setController(controller.build());

        try {
            container.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoDraweeView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}


