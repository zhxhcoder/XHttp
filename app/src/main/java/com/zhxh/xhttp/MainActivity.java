package com.zhxh.xhttp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zhxh.xhttplib.UpdateSoftService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UpdateSoftService.downUrl = "https://swww.niuguwang.com/download.ashx?channel=ngw";
                //UpdateSoftService.downUrl = "http://download.niuguwang.com/files/apk/ngw/niuguwang3.8.9_niuguwang.apk";
                //UpdateSoftService.stopDownload = false;

                Intent intent = new Intent();
                intent.putExtra("downUrl","http://download.niuguwang.com/files/apk/ngw/niuguwang3.8.9_niuguwang.apk");
                intent.putExtra("stopDownload",false);
                intent.putExtra("apkName","niuguwang.apk");
                intent.setClass(MainActivity.this, UpdateSoftService.class);
                startService(intent);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
