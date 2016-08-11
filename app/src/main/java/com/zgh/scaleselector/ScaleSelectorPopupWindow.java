package com.zgh.scaleselector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.zgh.scaleselector.view.ScaleSelectorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuguohui on 2016/8/9.
 */
public class ScaleSelectorPopupWindow extends PopupWindow {


    private Activity activity;
    private ScaleSelectorView scaleSelectorView;

    public ScaleSelectorPopupWindow(Context context, List<String> data, int defaultIndex, ScaleSelectorView.OnSelectChangeListener listener) {
        if (!(context instanceof Activity)) {
            throw new RuntimeException("must use activity context");
        }
        activity = (Activity) context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_set, null);
        setContentView(view);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setOutsideTouchable(true);
        // 设置popWindow的显示和消失动画
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        //屏幕变暗
        changeWindowAlpha(0.6f);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeWindowAlpha(1.0f);
            }
        });

        scaleSelectorView = (ScaleSelectorView) view.findViewById(R.id.selceView);
        if(data!=null) {
            scaleSelectorView.setData(data, defaultIndex);
        }
        scaleSelectorView.setOnSelectChangeListener(listener);
    }

    public void show(View view) {
        // 在底部显示
        this.showAtLocation(view,
                Gravity.BOTTOM, 0, 0);
    }


    private void changeWindowAlpha(float alpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.alpha = alpha;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
        window.setAttributes(wl);
    }

}
