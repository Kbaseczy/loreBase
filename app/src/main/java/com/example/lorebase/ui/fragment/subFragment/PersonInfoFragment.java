package com.example.lorebase.ui.fragment.subFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.R;
import com.example.lorebase.ui.fragment.SpaceFragment;
import com.example.lorebase.bean.UserInfo;
import com.example.lorebase.util.Constant;
import com.example.lorebase.util.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Request;

/*
    in the spaceFragment
 */
////todo  info & adapter & fragment(界面文件)，三位一体
public class PersonInfoFragment extends Fragment {
    View view;
    private ImageButton back;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ProgressDialog dialog;
    private TextView userId,userName,pwd,email,phone,createTime,updateTime,account;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_person_info, container, false);
        initView();
        getInfo();
        return view;
    }

    private void initView(){
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        back = view.findViewById(R.id.imageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout,new SpaceFragment()); //replace实现返回上一层效果
                //addToBackStack(null);   当前界面加入回退栈
                transaction.commit();
            }
        });
        //todo 注意id名不能为id,大小写无区别。不能是id,大小写无区别
        userId = view.findViewById(R.id.deId);
        userName = view.findViewById(R.id.uName);
        pwd = view.findViewById(R.id.pwd);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        createTime = view.findViewById(R.id.cTime);
        updateTime = view.findViewById(R.id.uTime);
        account = view.findViewById(R.id.account);
    }
    private void getInfo(){
        String url = Constant.BaseUrl+"/user/get_information.do";
        OkHttpUtils
                .get()
                .url(url) //TODO  没有参数
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        dialog = new ProgressDialog(getActivity());
                        dialog.setTitle("提示");
                        dialog.setMessage("正在加载...");
                        dialog.show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        L.e(response);
                        Gson gson  = new Gson();
                        UserInfo userInfo = gson.fromJson(response ,UserInfo.class);
                        account.setText(userInfo.getData().getAccount());
                        //todo res.Resources$NotFoundException: String resource ID #0x4,数据类型转换问题
                        userId.setText(String.valueOf(userInfo.getData().getId()));
                        userName.setText(userInfo.getData().getUsername());
                        pwd.setText(userInfo.getData().getPassword());
                        phone.setText(userInfo.getData().getPhone());
                        email.setText(userInfo.getData().getEmail());
                        createTime.setText(userInfo.getData().getCreateTime());
                        updateTime.setText(userInfo.getData().getUpdateTime());

                        //todo test : 通过此处获取用户个人信息，并将用户id传到AddMBaseFragment---最终作为参数提交至数据库
                        new AddMBaseFragment().setUser_addId(String.valueOf(userInfo.getData().getId()));
                        //todo test : 通过此处获取用户个人信息，并将用户Name传到AddMBaseFragment---最终显示在Add界面
                        new AddMBaseFragment().setUser_addName(userInfo.getData().getUsername());
                    }
                });
    }
}
