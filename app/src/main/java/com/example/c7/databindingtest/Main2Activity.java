package com.example.c7.databindingtest;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.c7.databindingtest.databinding.ActivityMain2Binding;
import com.example.c7.databindingtest.model.Goods;

import java.util.Random;

public class Main2Activity extends AppCompatActivity {
    private Goods goods;
    private static final String TAG = "Main2Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMain2Binding activityMain2Binding= DataBindingUtil.setContentView(this,R.layout.activity_main2);
        goods=new Goods("yili","milk",5);
        activityMain2Binding.setGoods(goods);
        activityMain2Binding.setGoodsHandler(new GoodsHandler());
        goods.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId==BR.name){
                    Log.e(TAG, "BR.name");
                } else if (propertyId == BR.details) {
                    Log.e(TAG, "BR.details");
                } else if (propertyId == BR._all) {
                    Log.e(TAG, "BR._all");
                } else {
                    Log.e(TAG, "未知");
                }
            }
        });
    }
    public class GoodsHandler{
        public void changeGoodsName(){
            goods.setName("yili"+new Random().nextInt(100));
            goods.setPrice(new Random().nextInt(100));
        }
        public void changeGoodsDetails() {
            goods.setDetails("hi" + new Random().nextInt(100));
            goods.setPrice(new Random().nextInt(100));
        }
    }
}
