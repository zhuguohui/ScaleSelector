# ScaleSelector
自定义卡尺选择器

#前言
由于某个项目需要，就写了这个东西。大家看看有需求的直接拿走。

#效果

1.支持拖动，支持点击

![默认效果](https://github.com/zhuguohui/ScaleSelector/blob/master/gif/1.gif?raw=true)

2.可定制的属性还是比较全面的包裹差值器都可以设置，下面这个使用的是overshoot_interpolator差值器

![这里写图片描述](https://github.com/zhuguohui/ScaleSelector/blob/master/gif/2.gif?raw=true)

#使用

###1.在xml中定义

![这里写图片描述](http://img.blog.csdn.net/20160811094045916)

###2.可用的属性

![这里写图片描述](http://img.blog.csdn.net/20160811094213643)

###3.设置监听器
我把这个卡尺选择器封装了一个自定义的popupwindow，大家可以直接使用这个popuwindow。
使用方法如下：

```
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
		//构造函数有三个参数，分别是context，要显示的数据（如果直接在xml中设置了，就传一个null就行了）
		//默认选中的index，选中的监听器。
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
```
