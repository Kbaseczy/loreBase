package com.example.lorebase.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lorebase.ui.activity.LoginActivity;
import com.example.lorebase.R;
import com.example.lorebase.ui.fragment.dialog.DeviceDialogFragment;
import com.example.lorebase.ui.fragment.dialog.ExitDialogFragment;
import com.example.lorebase.ui.fragment.subFragment.LocationFragment;
import com.example.lorebase.ui.fragment.subFragment.PersonInfoFragment;
import com.example.lorebase.ui.fragment.subFragment.PersonalFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpaceFragment extends Fragment implements View.OnClickListener{
    private  View view;
    private TextView exit,backToLogin;
    private LinearLayout goPersonBase,goInfo,goInform,goLocation;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private SoundPool soundPool;
    private int soundID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_space, container, false);
        initView();
        initSound();
        return view;
    }

    private void initSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().build();
        }
        soundID = soundPool.load(getActivity(), R.raw.kiss2, 1);//kiss1加载不进去
    }

    private void playSound() {
        soundPool.play(
                soundID,
                0.1f,      //左耳道音量【0~1】
                0.5f,      //右耳道音量【0~1】
                0,         //播放优先级【0表示最低优先级】
                0,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                1          //播放速度【1是正常，范围从0~2】
        );
    }

    private void initView(){
        manager = getFragmentManager();

        exit = view.findViewById(R.id.exit_txt);
        backToLogin = view.findViewById(R.id.goToLogin);  //goToLogin - backToLogin混乱
        goPersonBase = view.findViewById(R.id.goPerson);
        goInfo = view.findViewById(R.id.goInfo);
        goInform = view.findViewById(R.id.goInform);
        goLocation  = view.findViewById(R.id.go_location);

        exit.setOnClickListener(this);
        backToLogin.setOnClickListener(this);   //NULLPOINTEREXCEPTION--id绑定错误，
        goPersonBase.setOnClickListener(this);
        goInfo.setOnClickListener(this);
        goInform.setOnClickListener(this);
        goLocation.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        transaction = manager.beginTransaction();
        switch (view.getId()){
            case R.id.goToLogin:
                Intent i = new Intent(getActivity(),LoginActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.animator.go_in, R.animator.go_out);
                getActivity().finish();
                android.widget.Toast.makeText(getActivity(), "Logout Successful.",
                        android.widget.Toast.LENGTH_SHORT).show();

                playSound();

                break;
            case R.id.exit_txt:
                ExitDialogFragment ef = new ExitDialogFragment();
                ef.setTargetFragment(SpaceFragment.this,1);
                ef.show(getFragmentManager(),"exit");
                break;
            case R.id.goPerson:
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout,new PersonalFragment()).
                        addToBackStack("spaceFragment");//spaceFragment加入回退栈，返回键会返回上一个界面
                                                        //值为NULL  导致返回时是空白界面？
                //跳转
                break;
            case R.id.goInfo:
                //个人信息的Fragment,可以进行修改（次要）。
                transaction.setCustomAnimations(
                                R.animator.fragment_slide_left_enter,
                                R.animator.fragment_slide_left_exit,
                                R.animator.fragment_slide_right_exit,
                                R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout,new PersonInfoFragment()).
                        addToBackStack("spaceFragment");
                break;
            case R.id.goInform:
                //DialogFragment显示设备信息
                DeviceDialogFragment df = new DeviceDialogFragment();
                df.setTargetFragment(SpaceFragment.this,1);
                df.show(getFragmentManager(),"device");
                break;
            case R.id.go_location:
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout,new LocationFragment()).
                        addToBackStack("spaceFragment");
                break;
        }
        transaction.commit();
    }
}
