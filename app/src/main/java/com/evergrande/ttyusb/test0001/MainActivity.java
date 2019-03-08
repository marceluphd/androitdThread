package com.evergrande.ttyusb.test0001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //定义一个线程
    private myThread mmyThread = null;

    private final String  TAG = "TEST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mmyThread = new myThread();

        //初始化线程
        mmyThread.InitThread();
    }
}





