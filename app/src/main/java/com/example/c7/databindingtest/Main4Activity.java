package com.example.c7.databindingtest;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableList;
import android.databinding.ObservableMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.c7.databindingtest.databinding.ActivityMain4Binding;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {
    private ObservableMap<String,String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMain4Binding activityMain4Binding= DataBindingUtil.setContentView(this,R.layout.activity_main4);
        map=new ObservableArrayMap<>();
        map.put("name","chenqi");
        map.put("age","21");
        activityMain4Binding.setMap(map);
        ObservableList<String> list=new ObservableArrayList<>();
        list.add("chen");
        list.add("qi");
        activityMain4Binding.setList(list);
        activityMain4Binding.setIndex(0);
        activityMain4Binding.setKey("name");
    }
    public void onClick(View view){
        map.put("name","chenqi,hi"+new Random().nextInt(100));
    }
}
