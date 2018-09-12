package com.example.wokeshihuimaopaodekafei.myapplication2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends Activity {

    String sex="0";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RadioGroup n_sex=(RadioGroup)findViewById(R.id.et_sex);
        Button n_btn=(Button)findViewById(R.id.et_rbutton);
        n_sex.setOnCheckedChangeListener(new sexSelectListener());
        n_btn.setOnClickListener(new ClickListener());
        handler=new RespHandler();
    }


    class ClickListener implements View.OnClickListener{
        EditText n_username=(EditText)findViewById(R.id.et_username);
        EditText n_password=(EditText)findViewById(R.id.et_password);
        //EditText n_password2=(EditText)findViewById(R.id.et_passwordAgain);
        EditText n_age=(EditText)findViewById(R.id.et_age);
        EditText n_email=(EditText)findViewById(R.id.et_email);
        @Override
        public void onClick(View view) {
            String name=n_username.getText().toString();
            String password=n_password.getText().toString();
            //String password2=n_password2.getText().toString();
            String age=n_age.getText().toString();
            String email=n_email.getText().toString();

            LoginProcess lp=new LoginProcess();
            lp.execute("http://10.0.2.2:8080/vivo/reg.action",name,password,age,sex,email);
        }
    }
    class sexSelectListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {
            switch (checkId){
                case R.id.et_male:
                    sex="0";
                    break;
                case R.id.et_female:
                    sex="1";
                    break;

            }
        }
    }

    class RespHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                    SharedPreferences pref=getSharedPreferences("setting", Context.MODE_PRIVATE);
                    if(msg.obj!=null) {
                        SharedPreferences.Editor editor = pref.edit();
                        // 记录JSESSIONID
                        editor.putString("Cookie", msg.obj.toString());
                        editor.commit();
                    }
                    finish();
                    break;
                case 0:
                    Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_LONG).show();
                    break;
            }

        }
    }
    class LoginProcess extends AsyncTask<String, Void, Map> {

        @Override
        protected Map doInBackground(String... params) {
            Map res=new HashMap();
            try {
                URL url=new URL(params[0]);
                String name=params[1];
                String password=params[2];
                String age=params[3];
                String n_sex=params[4];
                String email=params[5];

                String data=
                        "userName="+ URLEncoder.encode(name,"UTF-8")+
                                "&userPass="+URLEncoder.encode(password,"UTF-8")+
                                "&userAge="+URLEncoder.encode(age,"UTF-8")+
                                "&userSex="+URLEncoder.encode(n_sex,"UTF-8")+
                                "&userEmail="+URLEncoder.encode(email,"UTF-8");
                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
                SharedPreferences pref=getSharedPreferences("setting", Context.MODE_PRIVATE);
                String cookie=pref.getString("Cookie",null);
                if(cookie!=null){
                    conn.setRequestProperty("Cookie",cookie);
                }
                OutputStream out = conn.getOutputStream();
                out.write(data.getBytes());
                out.flush();
                out.close();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String line="";
                    String resp="";
                    while((line=br.readLine())!=null){
                        resp=resp+line;
                    }
                    res.put("body",new JSONObject(resp));
                    Map<String,List<String>> headers=conn.getHeaderFields();
                    List<String> cookies=headers.get("Set-Cookie");
                    if(cookies!=null){
                        String header="";
                        for(int i=0;i<cookies.size();i++){
                            String c=cookies.get(i);
                            if(i!=cookies.size()-1){
                                header=header+c.substring(0,c.indexOf(";"))+";";
                            }else{
                                header=header+c.substring(0,c.indexOf(";"));
                            }
                        }
                        res.put("header",header);
                    }
                    in.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
            return res;
        }
        protected void onPostExecute(Map map) {
            JSONObject jsonObject=(JSONObject)map.get("body");
            String header=(String)map.get("header");
            Message msg=handler.obtainMessage();
            if(jsonObject!=null){
                if(jsonObject.optBoolean("reg")==true){
                    msg.what=1;
                    msg.obj=header;
                }else{
                    msg.what=0;
                    msg.obj="wrong";
                }}else{
                msg.what=0;
                msg.obj="wrong";
            }
            handler.sendMessage(msg);
        }
    }
}