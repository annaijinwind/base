package com.lieier.base.yulan;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lieier.base.BaseActivity;
import com.lieier.base.R;
import com.lieier.base.adapters.SimpleViewPagerAdapter;
import com.lieier.base.bean.TextBean;
import com.lieier.base.views.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ViewPagerActivity extends BaseActivity {
    private static final String ISLOCKED_ARG = "isLocked";
    @BindView(R.id.view_pager) ViewPager mViewPager;
    private int position;
    private SimpleViewPagerAdapter adapter;
    List<TextBean> listData=new ArrayList<TextBean>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        setContentView(mViewPager);

        if (getIntent() != null) {
            position = getIntent().getIntExtra("position", 0);
            listData= (ArrayList<TextBean>) getIntent().getSerializableExtra("list");
            setEvent();
            mViewPager.setCurrentItem(position);
        }
        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }
    }
    private void setEvent(){
        adapter=new SimpleViewPagerAdapter(mViewPager,listData,ViewPagerActivity.this);
        mViewPager.setAdapter(adapter);
    }
    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onRecvData(String jsonStr, String method) {
        return true;
    }
}
