package com.sunzhibin.studyproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sunzhibin
 * <p>
 * date: 2018/5/30.
 * description: description
 * e-mail: E-mail
 * modify： the history
 * </p>
 */
public class SwipeActivity extends FragmentActivity {
    private ListView mListView;
    private ListViewAdapter mAdapter;
    private Context mContext = this;
    private List<String> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        mListView = (ListView) findViewById(R.id.listview);
        mListData = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mListData.add("" + i);
        }
        mAdapter = new ListViewAdapter(this, mListData);
        mListView.setAdapter(mAdapter);
        mAdapter.setMode(Attributes.Mode.Single);
    }


    public class ListViewAdapter extends BaseSwipeAdapter {

        private Context mContext;
        private List<String> mlistData;

        public ListViewAdapter(Context mContext, List<String> mlistData) {
            this.mContext = mContext;
            this.mlistData = mlistData;
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipeLayout;
        }

        @Override
        public View generateView(int position, ViewGroup parent) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_listview, null);
            SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
//                YoYo.with( Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                }
            });
            return v;
        }

        @Override
        public void fillValues(int position, View convertView) {
            TextView t = (TextView) convertView.findViewById(R.id.id_textAlredy);
            t.setText(getItem(position));
        }

        @Override
        public int getCount() {
            return mlistData.size();
        }

        @Override
        public String getItem(int position) {
            return mlistData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }


}
