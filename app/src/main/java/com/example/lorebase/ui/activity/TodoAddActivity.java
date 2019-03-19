package com.example.lorebase.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.TodoTodo;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.util.TimeUtils;
import com.example.lorebase.util.ToastUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAddActivity extends BaseActivity implements View.OnClickListener {

    Toolbar toolbar;
    EditText todo_name, todo_desc;
    Button save;
    TextView s_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.todoAdd);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        todo_name = findViewById(R.id.todo_add_name);
        todo_desc = findViewById(R.id.todo_add_des);
        save = findViewById(R.id.save_todo_add);
        s_date = findViewById(R.id.todo_add_date);
        s_date.setText(TimeUtils.date2String(new Date(), "yyyy-MM-dd"));
        s_date.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    //toolbar menu click_event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, TODOActivity.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void postData() {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        Map<String,String> map = new HashMap<>();
        map.put("title",todo_name.getText().toString());
        map.put("content",todo_desc.getText().toString());
        map.put("date",s_date.getText().toString());
        retrofit2.Call<TodoTodo> todoAddCall = api.postAddTodo(map);
        todoAddCall.enqueue(new Callback<TodoTodo>() {
            @Override
            public void onResponse(retrofit2.Call<TodoTodo> call, Response<TodoTodo> response) {
                if (response.body() != null) {
                    if(response.body().getErrorCode()==0){
                        Intent i = new Intent(TodoAddActivity.this, TODOActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.animator.go_in, R.animator.go_out);
                        Toast.makeText(TodoAddActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        ToastUtil.showShortToastCenter(response.body().getErrorMsg());
                        Toast.makeText(TodoAddActivity.this, response.body().getErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TodoTodo> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.save_todo_add) {
            todo_name.setError(null);
            if (TextUtils.isEmpty(todo_name.getText())) {
                todo_name.setError(getString(R.string.input_todo_name_toast));
                todo_name.setFocusable(true);
                todo_name.setFocusableInTouchMode(true);
                todo_name.requestFocus();
                return;
            }
            postData();
        } else if (v.getId() == R.id.todo_add_date) {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth)
                    -> s_date.setText(String.format("%d-%d-%d", year, month + 1, dayOfMonth)),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
            datePickerDialog.show();
        }
    }
}
