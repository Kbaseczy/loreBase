package com.example.lorebase.ui.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lorebase.R;
import com.example.lorebase.adapter.ArticleAdapter;
import com.example.lorebase.ui.fragment.subFragment.AddMBaseFragment;
import com.example.lorebase.ui.fragment.subFragment.MBItemFragment;
import com.example.lorebase.ui.fragment.subFragment.ModifyMBaseFragment;
import com.example.lorebase.bean.ArticleInfo;
import com.example.lorebase.util.Constant;
import com.example.lorebase.util.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * kbase_article表，简单list显示name,点进去显示content,长按
 */
public class CollectFragment extends Fragment{
    private View view;
    private ListView article_list;
    private ProgressDialog dialog;
    //TODO没有做分页
    private List<ArticleInfo.DataBean.ListBean> list = new ArrayList<>();

    private ArticleAdapter articleAdapter ;
    private int mID;
    private  ArticleInfo.DataBean.ListBean listBean;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mbase,null);
        article_list = view.findViewById(R.id.mBase_list);
        articleAdapter = new ArticleAdapter(getActivity(),list);
        article_list.setAdapter(articleAdapter);
        article_list.setOnCreateContextMenuListener((menu, view, contextMenuInfo) -> {
            menu.setHeaderTitle("Select option");
            menu.add(0, 0, 0, "add New article");
            menu.add(0, 1, 0, "modify this");
            menu.add(0, 2, 0, "delete this");
            menu.add(0, 3, 0, "share for");
        });
        article_list.setOnItemClickListener((adapterView, view, i, l) -> {
            listBean = list.get(i);//todo
            L.e(" listBean Id ==="+listBean.getId());
            if(listBean==null){
                return;
            }
            mID = listBean.getId(); //TODO 这不是当前对象的ID吗
            FragmentManager manager= getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            MBItemFragment mbi = new MBItemFragment( );
            mbi.setMId(mID);
            transaction.setCustomAnimations(
                    R.animator.fragment_slide_left_enter,
                    R.animator.fragment_slide_left_exit,
                    R.animator.fragment_slide_right_exit,
                    R.animator.fragment_slide_right_enter).
                    replace(R.id.content_layout,mbi).
                    addToBackStack(null);
            transaction.commit();
        });
        getData();
        return view;
    }

    private void getData(){
        String url = Constant.BaseUrl+"/article/get_article.do";
        OkHttpUtils
                .get()
                .url(url) //TODO  没有传递参数
                .addParams("pageNum","")
                .addParams("pageSize","")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
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
                        list.clear();
                        Gson gson  = new Gson();
                        ArticleInfo articleInfo = gson.fromJson(response ,ArticleInfo.class);
                        if(articleInfo!=null){
                           list.addAll( articleInfo.getData().getList());
                           articleAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //info.id得到listView中选择的条目绑定的id,当前上下文的id
        //todo 获取当前item的对象的id
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String id = String.valueOf(info.id);
        switch (item.getItemId()) {
            case 0:
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_enter,
                        R.animator.fragment_slide_right_exit).
                        replace(R.id.content_layout,new AddMBaseFragment()).
                        addToBackStack(null);
                break;
            case 1:
                //todo modify：{"status":1,"msg":"更新知识点失败"}
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_enter,
                        R.animator.fragment_slide_right_exit).
                        replace(R.id.content_layout,new ModifyMBaseFragment()).
                        addToBackStack(null);// don't goTo it
                break;
            case 2:
                //todo delete:{"status":1,"msg":"删除知识点失败"}
                delete(String.valueOf(mID));
                getData();
                break;
            case 3:
                share(String.valueOf(mID));
                break;
        }
        transaction.commit();
        return super.onContextItemSelected(item);
    }

    private void delete(String id) {
        String url = Constant.BaseUrl + "/article/del_article.do";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("articleId", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "delete异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        dialog = new ProgressDialog(getActivity());
                        dialog.setTitle("Tips");
                        dialog.setMessage("...deleting...");
                        dialog.show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        L.e(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("status")==0){
                                Toast.makeText(getActivity(),"delete success",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }
    private void share(String id){
        //todo 将当前文章 传至 放入数据库
        //share 异常
        String url = Constant.BaseUrl + "share/share.do";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("articleId",id )
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "share异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        dialog = new ProgressDialog(getActivity());
                        dialog.setTitle("Tips");
                        dialog.setMessage("...sharing...");
                        dialog.show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        L.e(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("status")==0){
                                Toast.makeText(getActivity(),"share success",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }
}
