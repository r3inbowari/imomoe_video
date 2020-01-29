package com.r3inbowari.imo.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.r3inbowari.imo.R;
import com.r3inbowari.imo.base.PlayerAdapter;
import com.r3inbowari.imo.imomoeAPI.ImomoeBangumiSource;
import com.r3inbowari.imo.imomoeAPI.ImomoeManager;
import com.r3inbowari.imo.imomoeAPI.ImomoeSearch;
import com.r3inbowari.imo.imomoeAPI.ImomoeUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import tcking.github.com.giraffeplayer2.VideoView;

public class PlayerActivity extends AppCompatActivity {

    VideoView videoView = null;
    ArrayList<ImomoeBangumiSource> bangumiSourcesList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private PlayerAdapter mAdapter;


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 辣鸡回收
//        if (videoView.getPlayer().getVideoInfo().getUri() != null) {
//            videoView.getPlayer().onActivityDestroyed();
//        }
//        videoView.
    }

    private ImomoeSearch imomoeSearch;

    @Override
    protected void onStop() {
        super.onStop();
        // viewp.dispose();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        imomoeSearch = (ImomoeSearch) getIntent().getSerializableExtra("bangumi_data");

        mRecyclerView = findViewById(R.id.recyclerview_detail);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        mRecyclerView.setLayoutManager(manager);
        //适配器参数：item布局、列表数据源
        mAdapter = new PlayerAdapter(R.layout.textview_num, bangumiSourcesList);
        //设置数据
        mRecyclerView.setAdapter(mAdapter);
        //动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        View top = getLayoutInflater().inflate(R.layout.layout_player_detail, (ViewGroup) mRecyclerView.getParent(), false);
        mAdapter.addHeaderView(top);
        TextView i = top.findViewById(R.id.title3);
        TextView j = top.findViewById(R.id.title4);
        TextView k = top.findViewById(R.id.title5);

        i.setText(imomoeSearch.alt);
        j.setText(imomoeSearch.update);
        k.setText(imomoeSearch.description);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                startVideo((ImomoeBangumiSource) adapter.getData().get(position));
            }
        });
        videoView = findViewById(R.id.bangumi_player);
        videoView.getVideoInfo().setShowTopBar(true);
        videoView.getVideoInfo().setBgColor(Color.BLACK);
        String bangumiID = ImomoeUtil.subString(imomoeSearch.detailPath, "view/", ".html");
        ImomoeManager.getInstance().getBangumiSourceSet(bangumiID).subscribe(new Observer<ArrayList<ImomoeBangumiSource>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i("momoa", "start subscribe imomoeJS");
            }

            @Override
            public void onNext(ArrayList<ImomoeBangumiSource> imomoeBangumiSources) {
                if (isForeground(PlayerActivity.this)) {
                    bangumiSourcesList = imomoeBangumiSources;
                    startVideo(bangumiSourcesList.get(0));
                    mAdapter.setNewData(imomoeBangumiSources);
                }
            }

            @Override
            public void onError(Throwable e) {
                System.err.println(e.toString());
                finish();
            }

            @Override
            public void onComplete() {
                Log.i("momoa", "imomoeJS func done");
            }
        });
    }


    /**
     * 判断某个activity是否在前台显示
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     */
    public static boolean isForeground(Activity context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

    private void startVideo(ImomoeBangumiSource imomoeBangumiSource) {
        if (imomoeBangumiSource.getBangumiSource() != null) {
            videoView.getVideoInfo().setTitle(imomoeSearch.alt + " " + imomoeBangumiSource.getBangumiNum());
            videoView.setVideoPath(imomoeBangumiSource.getBangumiSource());
            videoView.getPlayer().start();
        }
    }
}
