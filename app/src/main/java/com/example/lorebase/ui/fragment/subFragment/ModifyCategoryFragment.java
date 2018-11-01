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
import com.example.lorebase.ui.fragment.HomeFragment;
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
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class ModifyCategoryFragment extends Fragment {
    View view;
    private EditText cateName,cateId;
    private ImageView backHome;
    private Button modifyCate;
    private ProgressDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_show_kbase, container, false);
        initView();
        return view;
    }
    private void initView(){
        cateName = view.findViewById(R.id.modifyCateName);
        cateId = view.findViewById(R.id.modifyCateId);
        backHome = view.findViewById(R.id.mBack);
        modifyCate = view.findViewById(R.id.modifyCate);
        ModifyCateListener modifyCateListener = new ModifyCateListener();
        backHome.setOnClickListener(modifyCateListener);
        modifyCate.setOnClickListener(modifyCateListener);
    }

    class ModifyCateListener implements View.OnClickListener{
        private String url = Constant.BaseUrl+"/category/set_category_name.do";
        private FragmentManager manager = getFragmentManager();
        private FragmentTransaction transaction = manager.beginTransaction();
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.mBack:
                    transaction.setCustomAnimations(
                            R.animator.fragment_slide_left_enter,
                            R.animator.fragment_slide_left_exit,
                            R.animator.fragment_slide_right_exit,
                            R.animator.fragment_slide_right_enter).
                            replace(R.id.content_layout,new HomeFragment()); //replace实现返回上一层效果
                    //addToBackStack(null);   当前界面加入回退栈
                    transaction.commit();
                    break;
                case R.id.modifyCate:
                    final String name = cateName.getText().toString().trim();
                    final String id = cateId.getText().toString().trim();
                    OkHttpUtils
                            .post()
                            .url(url)
                            .addParams("categoryId ",id)
                            .addParams("categoryName",name)
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
                                                    replace(R.id.content_layout,new HomeFragment()).
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
