package com.example.c7.databindingtest;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Toast;
import com.example.c7.databindingtest.databinding.ActivityMain6Binding;
import com.example.c7.databindingtest.model.User;

public class Main6Activity extends AppCompatActivity {
    private User user;
    private ActivityMain6Binding activityMain6Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMain6Binding= DataBindingUtil.setContentView(this,R.layout.activity_main6);
        user=new User("chenqi","123456");
        activityMain6Binding.setUserInfo(user);
        activityMain6Binding.setUserPresenter(new UserPresenter());
    }
    public class UserPresenter{
        public void onUserNameClick(User user){
            Toast.makeText(Main6Activity.this,"用户名："+user.getName(),Toast.LENGTH_LONG).show();
        }
        public void afterTextChanged(Editable s){
            user.setName(s.toString());
            activityMain6Binding.setUserInfo(user);
        }

        public void afterUserPasswordChanged(Editable s){
            user.setPassword(s.toString());
            activityMain6Binding.setUserInfo(user);
        }
    }
}
