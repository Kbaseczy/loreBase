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
import com.example.lorebase.R;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.L;
import com.example.lorebase.util.TimeUtils;
import com.example.lorebase.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import okhttp3.Call;
import okhttp3.Request;

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
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void postData() {
        String uri = UrlContainer.TODO_ADD;

        OkHttpUtils
                .post()
                .url(uri)
                .addParams("title", todo_name.getText().toString())
                .addParams("content", todo_desc.getText().toString())
                .addParams("date", s_date.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        L.e(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("errorCode") == 0) {
                                Intent i = new Intent(TodoAddActivity.this, TODOActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                                ToastUtil.showShortToastCenter("添加成功");
                                finish();
                            } else {
                                Toast.makeText(TodoAddActivity.this, jsonObject.getString("errorMsg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
