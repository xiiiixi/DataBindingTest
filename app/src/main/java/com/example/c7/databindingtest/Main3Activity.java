package com.example.c7.databindingtest;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.c7.databindingtest.databinding.ActivityMain3Binding;
import com.example.c7.databindingtest.model.ObservableGoods;

import java.util.Random;

public class Main3Activity extends AppCompatActivity {
    private ObservableGoods observableGoods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMain3Binding activityMain3Binding= DataBindingUtil.setContentView(this,R.layout.activity_main3);
        observableGoods=new ObservableGoods("HUAWEI","telephone",5000);
        activityMain3Binding.setObservableGoods(observableGoods);
        activityMain3Binding.setObservableGoodsHandler(new ObservableGoodsHandler());
    }
    public class ObservableGoodsHandler{
        public void changeGoodsName() {
            observableGoods.getName().set("kangshifu" + new Random().nextInt(100));
        }

        public void changeGoodsDetails() {
            observableGoods.getDetails().set("water" + new Random().nextInt(100));
        }
    }
}
