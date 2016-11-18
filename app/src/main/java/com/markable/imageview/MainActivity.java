package com.markable.imageview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.markable.photoimage.CharAvatarView;

public class MainActivity extends AppCompatActivity {

    CharAvatarView mCharAvatarView1;
    CharAvatarView mCharAvatarView2;
    CharAvatarView mCharAvatarView3;
    CharAvatarView mCharAvatarView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCharAvatarView1 = (CharAvatarView) findViewById(R.id.imageview1);
        mCharAvatarView2 = (CharAvatarView) findViewById(R.id.imageview2);
        mCharAvatarView3 = (CharAvatarView) findViewById(R.id.imageview3);
        mCharAvatarView4 = (CharAvatarView) findViewById(R.id.imageview4);
        mCharAvatarView1.setText("杜代忠");
        mCharAvatarView2.setText("万磊");
        mCharAvatarView3.setText("杜");
        mCharAvatarView4.setText("订盘");
    }
}
