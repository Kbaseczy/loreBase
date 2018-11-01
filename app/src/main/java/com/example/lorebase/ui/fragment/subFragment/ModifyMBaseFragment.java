package com.example.lorebase.ui.fragment.subFragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 */
public class ModifyMBaseFragment extends Fragment {
    private  View view;
    private ImageView backToMBase;
    private EditText articleName;
    private EditText userId;
    private EditText categoryId;
    private EditText author;
    private EditText content;
    private Button modifyMBase;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_modify_mbase,null);
        initView();
        return view;
    }
    private void initView(){
        backToMBase = view.findViewById(R.id.backToMBase);
        articleName = view.findViewById(R.id.articleName);
        userId = view.findViewById(R.id.user_id);
        categoryId = view.findViewById(R.id.category_id);
        author = view.findViewById(R.id.author);
        content = view.findViewById(R.id.content);
        modifyMBase = view.findViewById(R.id.modifyMBase);
        ModifyMBaseListener modifyMBaseListener = new ModifyMBaseListener();
        backToMBase.setOnClickListener(modifyMBaseListener);
        modifyMBase.setOnClickListener(modifyMBaseListener);
    }

    class ModifyMBaseListener implements View.OnClickListener{
        String url = Constant.BaseUrl+ "/article/update_article.do";
        private FragmentManager manager = getFragmentManager();
        private FragmentTransaction transaction = manager.beginTransaction();
        @Override
        public void onClick(View view) {
            switch (view.getId()){
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
                case R.id.modifyMBase:
                    final String name = articleName.getText().toString().trim();
                    final String user_id = userId.getText().toString().trim();
                    final String category_id = categoryId.getText().toString().trim();
                    final String authors = author.getText().toString().trim();
                    final String contents = content.getText().toString().trim();
                    OkHttpUtils
                            .post()
                            .url(url)
                            .addParams("categoryId",category_id)
                            .addParams("userId",user_id)
                            .addParams("name",name )
                            .addParams("author", authors)
                            .addParams("content",contents)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    e.printStackTrace();
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "modify异常", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onBefore(Request request, int id) {
                                    super.onBefore(request, id);
                                    dialog = new ProgressDialog(getActivity());
                                    dialog.setTitle("Tips");
                                    dialog.setMessage("...modify...");
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
                                                    replace(R.id.content_layout,new CollectFragment()).
                                                    addToBackStack("mBaseFragment");
                                            Toast.makeText(getActivity(), "modify success.", Toast.LENGTH_SHORT).show();
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
