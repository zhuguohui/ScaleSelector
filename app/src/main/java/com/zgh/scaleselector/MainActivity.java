package com.zgh.scaleselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zgh.scaleselector.view.ScaleSelectorView;

public class MainActivity extends AppCompatActivity {




    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv_show);
        findViewById(R.id.btn_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSet();
            }
        });

    }

    private void showSet() {

        ScaleSelectorPopupWindow popupWindow = new ScaleSelectorPopupWindow(this, null, 0,
        new ScaleSelectorView.OnSelectChangeListener() {
            @Override
            public void onSelect(int index, String value) {
                textView.setText(value);
            }

        });
        popupWindow.show(findViewById(android.R.id.content));
    }



}
