package com.example.c7.databindingtest;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.c7.databindingtest.databinding.ActivityMain5Binding;
import com.example.c7.databindingtest.model.ObservableGoods;

public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMain5Binding activityMain5Binding= DataBindingUtil.setContentView(this,R.layout.activity_main5);
        ObservableGoods goods=new ObservableGoods("glass","cup",20);
        activityMain5Binding.setGoods(goods);
    }
}
