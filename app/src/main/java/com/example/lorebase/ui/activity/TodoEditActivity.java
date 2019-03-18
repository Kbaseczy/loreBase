package com.example.lorebase.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.TodoTodo;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.util.L;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoEditActivity extends Activity implements View.OnClickListener {
    TodoTodo.DataBean.DatasBean datasBean;
    Toolbar toolbar;
    EditText todo_name, todo_desc;
    Button save;
    TextView s_date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Bundle bundle = getIntent().getBundleExtra(ConstName.TODO_BEAN_NAME);
        if (bundle == null)
            return;
        datasBean = (TodoTodo.DataBean.DatasBean) bundle.getSerializable(ConstName.TODO_BEAN);
        L.v("bundle不为空");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.todoEdit);
        todo_name = findViewById(R.id.todo_edit_name);
        todo_desc = findViewById(R.id.todo_edit_des);
        s_date = findViewById(R.id.todo_edit_date);
        save = findViewById(R.id.save_todo_edit);
        if (!TextUtils.isEmpty(datasBean.getContent()))
            todo_desc.setText(datasBean.getContent());
        s_date.setText(datasBean.getDateStr());

        if (datasBean.getStatus() == 1) {
            save.setVisibility(View.GONE);
            todo_name.setEnabled(false);
            todo_name.setFocusable(false);
            todo_desc.setEnabled(false);
            todo_desc.setFocusable(false);
            s_date.setEnabled(false);
            s_date.setFocusable(false);
        }

        s_date.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.todo_edit_date:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth)
                        -> s_date.setText(String.format("%d-%d-%d", year, month + 1, dayOfMonth)),
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                datePickerDialog.show();
                break;
            case R.id.save_todo_edit:
                todo_name.setError(null);
                if (TextUtils.isEmpty(todo_name.getText())) {
                    todo_name.setError(getString(R.string.input_todo_name_toast));
                    todo_name.setFocusable(true);
                    todo_name.setFocusableInTouchMode(true);
                    todo_name.requestFocus();
                    return;
                }
                updateData(datasBean.getId());
                break;
        }
    }

    private void updateData(int id) {

        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        Map<String,String> map = new HashMap<>();
        map.put("title",todo_name.getText().toString());
        map.put("content",todo_desc.getText().toString());
        map.put("date",s_date.getText().toString());
        retrofit2.Call<TodoTodo> todoEditCall = api.postEditTodo(id,map);
        todoEditCall.enqueue(new Callback<TodoTodo>() {
            @Override
            public void onResponse(retrofit2.Call<TodoTodo> call, Response<TodoTodo> response) {
                if(response.body()!=null){
                    Intent i = new Intent(TodoEditActivity.this, TODOActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.animator.go_in, R.animator.go_out);
                    finish();
                }else {
                    assert response.body() != null;
                    Toast.makeText(TodoEditActivity.this, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TodoTodo> call, Throwable t) {

            }
        });
    }
}
