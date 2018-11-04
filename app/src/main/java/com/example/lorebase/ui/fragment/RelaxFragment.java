package com.example.lorebase.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lorebase.R;
import com.example.lorebase.adapter.ShareAdapter;
import com.example.lorebase.ui.fragment.subFragment.MBItemFragment;
import com.example.lorebase.bean.ShareInfo;
import com.example.lorebase.util.Constant;
import com.example.lorebase.util.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class RelaxFragment extends Fragment {
    private ProgressDialog dialog;
    private ListView kBaseList;
    private List<ShareInfo.DataBean> list = new ArrayList<>();
    private ShareAdapter ShareAdapter ;
    private int Mid;
    private ShareInfo.DataBean dataBean;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relax,null);
        kBaseList = view.findViewById(R.id.kBase_list);
        ShareAdapter = new ShareAdapter(getActivity(),list);
        kBaseList.setAdapter(ShareAdapter);
        kBaseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager manager= getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                MBItemFragment mbi = new MBItemFragment();
                dataBean = list.get(i);
                if(dataBean == null){
                    return;
                }
                Mid = dataBean.getId();
                mbi.setMId(Mid);  //todo 将item当前对象的ID传到文章详情
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout,mbi).
                        addToBackStack(null);
                transaction.commit();
            }
        });
        getData();
        return view;
    }
    private void getData(){
        String url = Constant.BaseUrl+"/share/share.do";
        OkHttpUtils
                .get()
                .url(url) //TODO  没有传递参数
                .addParams("articleId","")
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
                        //todo {"status":0,"msg":"分享成功"}

                        //todo 'java.util.List com.example.kbase.model.ShareInfo$DataBean.getList()' on a null object reference
                        dialog.dismiss();
                        L.e(response);
                        list.clear();
                        Gson gson  = new Gson();
                        String res = " {data: [{\"articleId\": 0, \"createTime\": \"2018-09-06T14:20:12.045Z\", \"id\": 0,\"updateTime\":\"2018-09-06T14:20:12.045Z\",\"userId\": 0}]}";
                        ShareInfo shareInfo = gson.fromJson(res ,ShareInfo.class);

                        L.e(String.valueOf(shareInfo));
                        if(shareInfo!=null){
                            list.addAll( shareInfo.getData());
                            ShareAdapter.notifyDataSetChanged();
                        }
                        Log.e("center",shareInfo.toString());
                        Log.e("center",shareInfo.getData().get(0).getCreateTime());
                    }
                });
    }
}
