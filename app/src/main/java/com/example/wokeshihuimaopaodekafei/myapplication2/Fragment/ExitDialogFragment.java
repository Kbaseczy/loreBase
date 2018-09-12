package com.example.wokeshihuimaopaodekafei.myapplication2.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.wokeshihuimaopaodekafei.myapplication2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExitDialogFragment extends DialogFragment implements View.OnClickListener{

    private Button exit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_exit_dialog,null);
        exit = (Button)view.findViewById(R.id.exit_dialog);
//        cancel = view.findViewById(R.id.neglect_dialog);
        exit.setOnClickListener(this);
//        cancel.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.exit_dialog:
                getActivity().finish(); // 这里只退出当前的Activity --> Fragment所在的Activity-MainActivity
                //  new LoginActivity().finish();// 退出LoginActivity
                //清除登录信息，用户名，密码

                Toast.makeText(getActivity(), "Exit Successful.", Toast.LENGTH_SHORT).show();
                break;
//                case R.id.neglect_dialog:
//                    new ExitDialogFragment().onStop();
        }
    }
}
