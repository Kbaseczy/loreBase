package com.example.lorebase.ui.fragment.subFragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lorebase.R;
import com.example.lorebase.ui.fragment.CollectFragment;
import com.example.lorebase.util.Constant;
import com.example.lorebase.util.L;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;
/**
 * A simple {@link Fragment} subclass.
 * version1.0:用户需要编辑知识点内容，名字，作者，类别，用户ID
 * version1.1：用户只需要添加知识点内容，名字，作者，类别  --test
 * version1.2：用户只需要添加知识点内容，名字，类别
 * 其中作者和用户ID即当前登录用户，不需要再作编辑。
 */
public class AddMBaseFragment extends Fragment {
    private View view;
    private ImageView backToMBase;
    private EditText articleName;
    private TextView userName;
    private EditText categoryId;
    private EditText author;
    private EditText content;
    private Button add;
    private ProgressDialog dialog;
    private String user_addId;
    private String user_addName;

    public String getUser_addName() {
        return user_addName;
    }

    public void setUser_addName(String user_addName) {
        this.user_addName = user_addName;
    }


    public String getUser_addId() {
        return user_addId;
    }

    public void setUser_addId(String user_addId) {
        this.user_addId = user_addId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_mbase,null);
        initView();
        AddMBaseListener addMBaseListener = new AddMBaseListener();
        backToMBase.setOnClickListener(addMBaseListener);
        add.setOnClickListener(addMBaseListener);
        return view;
    }
    private void initView(){
        backToMBase = view.findViewById(R.id.backToMBase);
        articleName = view.findViewById(R.id.articleName);
        userName = view.findViewById(R.id.amUserName);
        categoryId = view.findViewById(R.id.category_id);
        author = view.findViewById(R.id.author);
        content = view.findViewById(R.id.content);
        add = view.findViewById(R.id.addA);
    }
    class AddMBaseListener implements View.OnClickListener{
        String url = Constant.BaseUrl+"/article/add_article.do";
        private FragmentManager manager  = getFragmentManager();
        private FragmentTransaction transaction = manager.beginTransaction();
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.backToMBase:
                    transaction.setCustomAnimations(
                            R.animator.fragment_slide_left_enter,
                            R.animator.fragment_slide_left_exit,
                            R.animator.fragment_slide_right_exit,
                            R.animator.fragment_slide_right_enter).
                            replace(R.id.content_layout,new CollectFragment()); //replace实现返回上一层效果
                    //addToBackStack(null);   当前界面加入回退栈
                    transaction.commit();
                    break;
                case R.id.addA:
                    final String name = articleName.getText().toString().trim();
//                    final String user_id =userId.getText().toString().trim();
                    final String category_id = categoryId.getText().toString().trim();
                    final String authors = author.getText().toString().trim();
                    final String contents = content.getText().toString().trim();
                    //todo userName 通过PersonInfoFragment 传过来
                    userName.setText(user_addName);
                    OkHttpUtils.post()
                            .addParams("categoryId",category_id)
                            //todo test:userId用过PersonInfoFragment 传过来
                            .addParams("userId",user_addId)
                            .addParams("name",name )
                            .addParams("author", authors)
                            .addParams("content",contents)
                            .url(url)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    e.printStackTrace();
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "添加异常", Toast.LENGTH_SHORT).show();
                                    Log.e("CENTER",e.toString());
                                }

                                @Override
                                public void onBefore(Request request, int id) {
                                    super.onBefore(request, id);
                                    dialog = new ProgressDialog(getActivity());
                                    dialog.setTitle("Tips");
                                    dialog.setMessage("...add...");
                                    dialog.show();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    dialog.dismiss();
                                    L.e(response);

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if(jsonObject.getInt("status")==0){
                                            transaction.setCustomAnimations(
                                                    R.animator.fragment_slide_left_enter,
                                                    R.animator.fragment_slide_left_exit,
                                                    R.animator.fragment_slide_right_exit,
                                                    R.animator.fragment_slide_right_enter).
                                                    replace(R.id.content_layout,new CollectFragment());
                                            transaction.commit();
                                            Toast.makeText(getActivity(), "add success.", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    break;
            }
        }
    }
}


