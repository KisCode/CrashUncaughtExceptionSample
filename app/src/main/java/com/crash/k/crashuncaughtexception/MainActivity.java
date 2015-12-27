package com.crash.k.crashuncaughtexception;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initViews();

    }

    private void initData()
    {
        mContext=this;
    }

    private void initViews() {
        findViewById(R.id.btn_next_thread).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext,SecondActivity.class));
                    }
                });

        findViewById(R.id.btn_main_thread).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        throwNullPointException();
                    }
                });

        findViewById(R.id.btn_thread_test).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                throwNullPointException();
                            }
                        }).start();
                    }
                });
    }

    /**
     * //抛出NullPointException
     */
    private void throwNullPointException() {
        String str = null;
        str.toString();
    }

}
