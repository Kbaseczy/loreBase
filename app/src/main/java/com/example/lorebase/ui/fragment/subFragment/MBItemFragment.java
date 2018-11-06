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
import com.example.lorebase.ui.fragment.CollectFragment;
import com.example.lorebase.bean.DetailInfo;
import com.example.lorebase.util.Constant;
import com.example.lorebase.util.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * 文章详情
 */
public class MBItemFragment extends Fragment {
    private View view;
    private ImageButton back;
    private ProgressDialog dialog;
    private TextView author,categoryId,content,userId,createTime,updateTime;
    private int MId;
    public int getMId() {
        return MId;
    }
    public void setMId(int MId) {
        this.MId = MId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mbitem,null);
        initView();
        getDetail(String.valueOf(MId)); //todo MId(当前item对象的ID->articeID)由MBase传入
        return view;
    }

    private void initView() {
        back = view.findViewById(R.id.mbi_back_mBase);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout,new CollectFragment()). //replace(=remove+add)实现返回上一层效果
                        addToBackStack(null); //  当前界面加入回退栈
                transaction.commit();
            }
        });
        //todo NullExceptionPointer:on a null object reference  ID没绑定正确
        //todo  特别注意id绑定，不能有重复的id，必须唯一性，否则出现上述异常
        author = view.findViewById(R.id.deAuthor);
        categoryId = view.findViewById(R.id.categoryId);
        userId = view.findViewById(R.id.userId);
        content = view.findViewById(R.id.content);
        createTime =view.findViewById(R.id.creatTime);
        updateTime = view.findViewById(R.id.updateTime);
    }
    private void getDetail(String id){
        String url = Constant.BaseUrl+"/article/get_article_detail.do";
        OkHttpUtils
                .post()
                .url(url) //TODO  没有传递参数
                .addParams("articleId",id)
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
                        DetailInfo detailInfo = gson.fromJson(response ,DetailInfo.class);
                        author.setText(String.valueOf(detailInfo.getData().getAuthor()));
                        categoryId.setText(String.valueOf(detailInfo.getData().getCategoryId()));
                        content.setText(String.valueOf(detailInfo.getData().getContent()));
                        userId.setText(String.valueOf(detailInfo.getData().getUserId()));
                        createTime.setText(String.valueOf(detailInfo.getData().getCreateTime()));
                        updateTime.setText(String.valueOf(detailInfo.getData().getUpdateTime()));

//                        if(detailInfo!=null){
//                            list.addAll( detailInfo.getData().getList());
//                            detailAdapter.notifyDataSetChanged();
//                        }
                    }

                });
    }
}
