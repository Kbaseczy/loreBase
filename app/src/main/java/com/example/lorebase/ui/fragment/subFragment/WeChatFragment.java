package com.example.lorebase.ui.fragment.subFragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lorebase.R;
import com.example.lorebase.bean.WeChat;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.ui.fragment.HomeFragment;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * homeFragment中的第二级Fragment
 * 获取对应目录下的文章题目list，本级获取数据库表的题目、作者信息。
 * bundle 传递数据
 */

public class WeChatFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private View view;
    private ListView listView;
    private ImageButton back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mitem1,null);
        initView();
        return view;
    }

    private void initView() {
        listView = view.findViewById(R.id.mif1_lv);   //题目list
        back = view.findViewById(R.id.mit1_back_home);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,getData());
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
        back.setOnClickListener(this) ;
    }

    public List<String> getData() {
        List<String> data = new ArrayList<>();
        for(int i =0;i<5;i++){
            data.add(i+"article_name");
        }
        return data;
    }

    @Override
    public void onClick(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(
                R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_exit,
                R.animator.fragment_slide_right_enter).
                replace(R.id.content_layout,new HomeFragment()). //replace实现返回上一层效果
                addToBackStack(null); //  当前界面加入回退栈
        transaction.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FragmentManager manager= getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MItem2Fragment mif2 = new MItem2Fragment();
//                TextView tv1 = (TextView) view;
//                Bundle bundle = new Bundle();
//                bundle.putCharSequence("1",tv1.getText().toString());
//                mif.setArguments(bundle);

        transaction.setCustomAnimations(
                R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_exit,
                R.animator.fragment_slide_right_enter).
                replace(R.id.content_layout,mif2).
                addToBackStack(null);
        transaction.commit();
    }

    //被搁置的weChat  因为才发现根据ID无法获取更多有价值的信息
//    private void getWeChat(){
//        //开始获取不到数据，嘻嘻~~URL地址写混淆
//        String url = UrlContainer.baseUrl+UrlContainer.WE_CHAT;
//        OkHttpUtils
//                .get()
//                .url(url)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        e.printStackTrace();
//                    }
//                    @Override
//                    public void onBefore(Request request, int id) {
//                        super.onBefore(request, id);
//                    }
//                    @Override
//                    public void onResponse(String response, int id) {
//                        Log.v("WE_CHAT", response);
////                        list_weChat.clear();
//                        Gson gson = new Gson();
//                        //TODO :Expected Object but Array
//                        list_weChat = gson.fromJson(response, WeChat.class).getData();
//
//
//                        /*
//                        添加图片 IndexOutOfBoundsException
//                        1.新建list存储image  中途改为 增加list_weChat存储大小（比较死）--失败 outOfMemoryErr
//                            manifest中添加android:hardwareAccelerated="false"
//                            android:largeHeap="true"  --依然报错
//                            TODO 暂时搁置
//                        2.新建数组存储图片资源，
//                        TODO 遍历list_weChat取出数据，遍历新数组weChat_array存储name,id,imageId.
//                        todo 遍历新list_full存储数据(同时遍历新数组weChat_array取出数据)
//                        中间瓶子的思路依然崩盘，暂时放置了，先做其他部分
//                        ★ list_weChat --> weChat_array --> list_full
//                        新增了中间的瓶子，中间瓶子作用：重新组装数据-本地数据+网络数据（存储/取出）
//                        经过try catch 处理数组越界异常通过，但是图片资源只存储了一个.
//
//                        最后：图片资源id始终越界，好像以前做梦遇见过这种事情，interesting.
//                         */
////                        list_weChat.add(R.drawable.hong_yang,new WeChat.DataBean());
////                        list_weChat.add(R.drawable.guo_lin,new WeChat.DataBean());
////                        list_weChat.add(R.drawable.yu_gang,new WeChat.DataBean());
////                        list_weChat.add(R.drawable.cheng_xiang_mo_ying,new WeChat.DataBean());
////                        list_weChat.add(R.drawable.android_qun_ying,new WeChat.DataBean());
////                        list_weChat.add(R.drawable.code_xiao_sheng,new WeChat.DataBean());
////                        list_weChat.add(R.drawable.google_develop,new WeChat.DataBean());
////                        list_weChat.add(R.drawable.qi_zhuo_she,new WeChat.DataBean());
////                        list_weChat.add(R.drawable.mei_tuan,new WeChat.DataBean());
////                        list_weChat.add(R.drawable.gcs_sloop,new WeChat.DataBean());
//
////                        WeChat.DataBean[] weChat_array;
//                        for (WeChat.DataBean weChat : list_weChat) {
//
//                            for(int i = 0;i<list_weChat.size();i++) {
//                                WeChat.DataBean[] weChat_array = {
//                                        //遍历list_weChat 把数据取出存储到新的数组中。遍历weChat_array存储数据
//                                        new WeChat.DataBean(weChat.getId(), weChat.getName(), imageWeChat[i]),
//                                };
//
//                                //java.lang.ArrayIndexOutOfBoundsException: length=1; index=1 下面
////                                 Log.v("weChat_array",weChat_array[i].getName()+ " " + weChat_array[i].getId()
////                                         + "||test_add_image" + weChat_array[i].getImageId());
//                                //同样地，遍历weChat_array取出数据存储到list_full
//                                try{ //下表越界，如上
//                                    list_full.add(weChat_array[i]);
//                                }catch (ArrayIndexOutOfBoundsException e){
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                        initWeChat();
//                    }
//                });
//    }
}
