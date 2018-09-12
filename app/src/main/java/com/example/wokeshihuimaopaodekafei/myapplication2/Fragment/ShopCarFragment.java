package com.example.wokeshihuimaopaodekafei.myapplication2.Fragment;


//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wokeshihuimaopaodekafei.myapplication2.MainFragment;
import com.example.wokeshihuimaopaodekafei.myapplication2.R;

public class ShopCarFragment extends Fragment {

    private Button settle_accounts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  no_cart_item
        View view=inflater.inflate(R.layout.shopcarfragment, container, false);
        settle_accounts = (Button) view.findViewById(R.id.settle_accounts);
        settle_accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ShopCarFragment shopCarFragment = new ShopCarFragment();
//                HomeFragment homeFragment = new HomeFragment();
//                getFragmentManager().beginTransaction().replace(R.id.activity_shop, shopCarFragment).commit();
                Intent intent=new Intent(ShopCarFragment.this.getActivity(),MainFragment.class);
                startActivity(intent);

            }
        });
        return view;
    }


}