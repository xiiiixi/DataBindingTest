package com.example.c7.databindingtest;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.c7.databindingtest.databinding.ActivityMainBinding;
import com.example.c7.databindingtest.model.User;

public class MainActivity extends AppCompatActivity {
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        user=new User("chenqi","123456");
        activityMainBinding.setUserInfo(user);
    }
}
