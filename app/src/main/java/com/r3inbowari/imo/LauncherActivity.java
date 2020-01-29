package com.r3inbowari.imo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.r3inbowari.imo.base.MyApplication;
import com.r3inbowari.imo.imomoeAPI.ImomoeManager;
import com.r3inbowari.imo.imomoeAPI.ImomoeSearch;
import com.r3inbowari.imo.ui.SearchTitleBarActivity;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        ImomoeManager.getInstance().getBangumiSearch("天", 1).subscribe(new Observer<ArrayList<ImomoeSearch>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i("momoa", "start");
            }

            @Override
            public void onNext(ArrayList<ImomoeSearch> imomoeSearches) {
                MyApplication.arrayList.addAll(imomoeSearches);
                Intent intent = new Intent(LauncherActivity.this, SearchTitleBarActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Log.i("momoa", e.toString());
                Toast.makeText(LauncherActivity.this, "发生错误, 请检查您的网络", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LauncherActivity.this, SearchTitleBarActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onComplete() {
                Log.i("momoa", "done");
            }
        });
    }
}
