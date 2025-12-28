package com.example.myfirstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GobangView gobangView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🚨 核心修改在这里 🚨
        // 以前是：setContentView(new GobangView(this));
        // 现在改成：加载 XML 布局文件
        setContentView(R.layout.activity_main);

        // 1. 从布局里找到我们在 XML 里定义的棋盘和按钮
        gobangView = findViewById(R.id.my_gobang_view);
        Button btnRestart = findViewById(R.id.btn_restart);

        // 2. 只有找到了视图，才能给它添加点击事件
        // (防止万一 XML id 写错导致闪退)
        if (gobangView != null && btnRestart != null) {
            btnRestart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 调用重置方法
                    gobangView.restartGame();
                }
            });
        }
    }
}