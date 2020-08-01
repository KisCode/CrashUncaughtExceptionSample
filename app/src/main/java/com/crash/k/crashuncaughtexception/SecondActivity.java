package com.crash.k.crashuncaughtexception;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.btn_throws_exception).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               throwParseException();
                           }
                       }).start();
                    }
                });
    }


    /**
     * 抛出类型装换异常
     */
    private void throwParseException() {
        throw new RuntimeException("测试异常");
    }
}
