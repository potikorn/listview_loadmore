package com.example.android.listviewloadmore;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    private List<String> mTotalData = new ArrayList<String>();
    private List<String> mCurrentData = new ArrayList<String>();

    private int mCurrentPage = 1;
    private int mItemPerRow = 20;

    private boolean isLoadMore = false;
    private Handler mHandler = new Handler();

    private ProgressDialog mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 1; i <= 300; i++) {
            mTotalData.add("Item #" + i);
        }

        mLoading = new ProgressDialog(this);
        mLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoading.setMessage("Loading....");

        getData();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mCurrentData);
        setListAdapter(adapter);


        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;

                if ((lastInScreen == totalItemCount) && !isLoadMore) {
                    isLoadMore = true;
                    mLoading.show();

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData();
                            adapter.notifyDataSetChanged();
                            isLoadMore = false;
                            mLoading.dismiss();
                        }
                    }, 1500);
                }
            }
        });
    }

    private void getData() {
        if (mItemPerRow * mCurrentPage >= mTotalData.size()) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < mItemPerRow; i++) {
            mCurrentData.add(mTotalData.get(i + ((mCurrentPage - 1) * mItemPerRow)));
        }
        mCurrentPage += 1;
    }
}
