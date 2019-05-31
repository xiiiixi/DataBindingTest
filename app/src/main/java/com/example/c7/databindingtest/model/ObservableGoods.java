package com.example.c7.databindingtest.model;

import android.databinding.ObservableField;
import android.databinding.ObservableFloat;

/**
 * Created by c7 on 2019/5/30.
 */

public class ObservableGoods {
    private ObservableField<String> name;
    private ObservableField<String> details;
    private ObservableFloat price;
    public ObservableGoods(String name,String details,float price){
        this.name=new ObservableField<>(name);
        this.details = new ObservableField<>(details);
        this.price=new ObservableFloat(price);
    }
    public ObservableField<String> getName(){
        return name;
    }
    public void setName(ObservableField<String> name) {
        this.name = name;
    }

    public ObservableField<String> getDetails() {
        return details;
    }

    public void setDetails(ObservableField<String> details) {
        this.details = details;
    }

    public ObservableFloat getPrice() {
        return price;
    }

    public void setPrice(ObservableFloat price) {
        this.price = price;
    }

}
