package com.example.mypet.action;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mypet.R;
import com.example.mypet.ui.notifications.NotificationsFragment;

public class edit_diary extends AppCompatActivity {

    private TextView title;
    private TextView date;
    private TextView content;
    private Button edit;
    private Button back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_diary);
        title = (TextView)findViewById(R.id.title);
        date = (TextView)findViewById(R.id.date);
        content = (TextView)findViewById(R.id.content);
        Intent intent = getIntent();
        String getTitle = intent.getStringExtra("showTitle");
        String getDate = intent.getStringExtra("showDate");
        String getContent = intent.getStringExtra("showContent");
        title.setText(getTitle);
        date.setText(getDate);
        content.setText(getContent);

        edit = (Button)findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(edit_diary.this);
                final LayoutInflater inflater = getLayoutInflater();
                final View v1 = inflater.inflate(R.layout.edit_diary, null);
                builder.setView(v1).setTitle("修改日志").setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //寻找当前弹出框布局内的组件时，要用当前的view去寻找，此处为v1，与inflater相关联
                        EditText editTitle = v1.findViewById(R.id.editTitle);
                        EditText editDate = v1.findViewById(R.id.editDate);
                        EditText editContent = v1.findViewById(R.id.editContent);
                        Log.e("测试", "执行下一步");

                        String newTitle = editTitle.getText().toString();
                        Log.e("input_title", newTitle);
                        String newDate = editDate.getText().toString();
                        String newContent = editContent.getText().toString();
                        Log.e("input_date", newDate);
                        Log.e("input_content", newContent);

                        title.setText(newTitle);
                        date.setText(newDate);
                        content.setText(newContent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });


    }
}
