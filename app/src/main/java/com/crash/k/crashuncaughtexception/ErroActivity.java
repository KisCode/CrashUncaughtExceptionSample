package com.crash.k.crashuncaughtexception;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crash.k.crashuncaughtexception.utils.AppInfoUtils;
import com.crash.k.crashuncaughtexception.utils.CrashUncaugtExceptionUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ErroActivity extends AppCompatActivity {

    private Context mContext;
    private String mErroDetailsStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erro);
        initData();
        initViews();
    }

    private void initData(){
        mContext=this;
        String erroInfo= getIntent().getStringExtra(CrashUncaugtExceptionUtils.EXTRA_STRACE_MESSAGE);
        mErroDetailsStr=getErroDetailsInfo()+"\n strace \n"+erroInfo;
    }

    private void initViews()
    {
        Button btnRestart=(Button)findViewById(R.id.btn_restart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class launcherActivity= CrashUncaugtExceptionUtils.getLauncherActivity(mContext);

                if(launcherActivity!=null)
                {
                    Intent intent=new Intent(mContext,launcherActivity);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(mContext,"重启失败了",Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView tvInfo=(TextView)findViewById(R.id.tv_erro_info);
        tvInfo.setText(mErroDetailsStr);
    }

    /**获取错误信息
     *
     * @param thread
     * @param throwable
     * @return
     */
    private String getErroDetailsInfo()
    {
        String erroDetailInfo="";
        //设备名称
        erroDetailInfo+="DeviceName:"+ AppInfoUtils.getDeviceModelName()+ " \n";
        //版本号
        erroDetailInfo+="VersionName:"+AppInfoUtils.getVersionName(mContext)+ " \n";

        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        //记录当前出错时间
        erroDetailInfo+="currentTime"+dateFormat.format(new Date())+ " \n";

        //安装时间
        erroDetailInfo+="BuildTime:"+AppInfoUtils.getBuildDateAsString(mContext,dateFormat)+ " \n";

        return  erroDetailInfo;
    }
}
