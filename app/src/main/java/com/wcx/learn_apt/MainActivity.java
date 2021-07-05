package com.wcx.learn_apt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wcx.apt_lib.Test;

@Test
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}