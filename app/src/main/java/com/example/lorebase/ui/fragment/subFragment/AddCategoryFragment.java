package com.example.lorebase.ui.fragment.subFragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lorebase.R;
import com.example.lorebase.ui.fragment.HomeFragment;
import com.example.lorebase.ui.fragment.CollectFragment;
import com.example.lorebase.util.Constant;
import com.example.lorebase.util.L;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.FragmentTransaction;
import android.app.FragmentManager;
import okhttp3.Call;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AddCategoryFragment extends Fragment {
    private ProgressDialog dialog;
    private Button addC;
    private EditText categoryName,parentId;
    private View view;
    private ImageView back;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_category, container, false);
        initView();
        return view;
    }
    private void initView(){
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        addC = view.findViewById(R.id.addC);
        categoryName = view.findViewById(R.id.editCategoryName);
        back = view.findViewById(R.id.backToHome);
        parentId = view.findViewById(R.id.editParent);

        AddCategoryListener listener = new AddCategoryListener();
        addC.setOnClickListener(listener);
        back.setOnClickListener(listener);
    }
    private class AddCategoryListener implements View.OnClickListener{
        String url = Constant.BaseUrl+"/category/add_category.do";
        final String cateName = categoryName.getText().toString().trim();
        final String parent = parentId.getText().toString().trim();
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.backToHome:
                    transaction.setCustomAnimations(
                            R.animator.fragment_slide_left_enter,
                            R.animator.fragment_slide_left_exit,
                            R.animator.fragment_slide_right_exit,
                            R.animator.fragment_slide_right_enter).
                            replace(R.id.content_layout,new HomeFragment()); //replace实现返回上一层效果
                    //addToBackStack(null);   当前界面加入回退栈
                    transaction.commit();
                    break;
                case R.id.addC:
                    OkHttpUtils.post()
                            .addParams("categoryName ",cateName)
                            .addParams("parentId",parent)
                            .url(url)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    e.printStackTrace();
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "添加失败", Toast.LENGTH_SHORT).show();
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
                                                    replace(R.id.content_layout,new CollectFragment()).
                                                    addToBackStack("mBaseFragment");
                                            transaction.commit();
                                            Toast.makeText(getActivity(), "add cate success.", Toast.LENGTH_SHORT).show();
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
