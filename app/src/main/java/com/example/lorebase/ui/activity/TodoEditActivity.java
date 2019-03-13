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
import android.widget.Toolbar;

import com.example.lorebase.R;
import com.example.lorebase.bean.TodoTodo;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.util.L;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Request;

public class TodoEditActivity extends Activity implements View.OnClickListener {
    TodoTodo.DataBean.DatasBean datasBean;
    Toolbar toolbar;
    EditText todo_name, todo_desc;
    Button save;
    TextView s_date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Bundle bundle = getIntent().getBundleExtra(ConstName.TODO_BEAN);
        if (bundle == null)
            return;
        datasBean = (TodoTodo.DataBean.DatasBean) bundle.getSerializable(ConstName.TODO_BEAN);
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
        String uri = UrlContainer.TODO_UPDATE + id + "/json";
        OkHttpUtils
                .post()
                .addHeader("Cookie", String.valueOf(OkHttpUtils.getInstance().getOkHttpClient().cookieJar()))
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
                                Intent i = new Intent(TodoEditActivity.this, TODOActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                                finish();
                            } else {
                                Toast.makeText(TodoEditActivity.this, jsonObject.getString("errorMsg"), Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
