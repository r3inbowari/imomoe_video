package com.r3inbowari.imo.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.r3inbowari.imo.R;
import com.r3inbowari.imo.base.HomeAdapter;
import com.r3inbowari.imo.imomoeAPI.ImomoeManager;
import com.r3inbowari.imo.imomoeAPI.ImomoeSearch;
import com.r3inbowari.imo.impl.OnLimitClickHelper;
import com.r3inbowari.imo.impl.OnLimitClickListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SearchDetailActivity extends AppCompatActivity {
    // 搜索items当前可能的最大数目
    private static int itemsCount = 0;
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private HomeAdapter mAdapter;

    protected void hideKeyforard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //适配器参数：item布局、列表数据源
        mAdapter = new HomeAdapter(R.layout.bangumi_item, mDatas);
        //设置数据
        mRecyclerView.setAdapter(mAdapter);
        //动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        findViewById(R.id.search_cancle_layout).setOnClickListener(
                new OnLimitClickHelper(new OnLimitClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                }));

        final ImomoeManager imomoeManager = new ImomoeManager();

        final EditText search = findViewById(R.id.search_edit);


        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mAdapter.getData().get(0).pages > itemsCount) {
                    imomoeManager.getBangumiSearch(search.getText().toString(), itemsCount / 10 + 1).subscribe(new Observer<ArrayList<ImomoeSearch>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(ArrayList<ImomoeSearch> imomoeSearches) {
                            mAdapter.addData(imomoeSearches);
                            itemsCount += 10;
                            mAdapter.loadMoreComplete();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mAdapter.loadMoreFail();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                } else {
                    mAdapter.loadMoreEnd();
                }
            }
        }, mRecyclerView);

        final AVLoadingIndicatorView avi = findViewById(R.id.avi);
        final TextView tw = findViewById(R.id.bangumi_nothing);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH && !textView.getText().toString().equals("")) {
                    tw.setVisibility(View.INVISIBLE);
                    mAdapter.setEnableLoadMore(true);
                    avi.show();
                    avi.bringToFront();
                    imomoeManager.getBangumiSearch(search.getText().toString(), 1).subscribe(new Observer<ArrayList<ImomoeSearch>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(ArrayList<ImomoeSearch> imomoeSearches) {
                            mAdapter.setNewData(imomoeSearches);
                            avi.hide();
                            if (imomoeSearches.size() < 10) {
                                mAdapter.loadMoreEnd();
                                itemsCount = 0;
                            }
                            itemsCount += 10;
                        }

                        @Override
                        public void onError(Throwable e) {
                            mAdapter.setNewData(new ArrayList<ImomoeSearch>());
                            tw.setVisibility(View.VISIBLE);
                            avi.hide();
                            itemsCount = 0;
                            mAdapter.loadMoreEnd();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }
                hideKeyforard(textView);
                return false;
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImomoeSearch a = (ImomoeSearch) adapter.getItem(position);
                Intent intent = new Intent(SearchDetailActivity.this, PlayerActivity.class);
                intent.putExtra("bangumi_data", a);
                startActivity(intent);
            }
        });
    }

}
