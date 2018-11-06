package com.example.lorebase.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.example.lorebase.R;
import com.example.lorebase.ui.fragment.subFragment.SearchListFragment;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar_search;
    private String key_word;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar_search = findViewById(R.id.toolbar_search);

        toolbar_search.inflateMenu(R.menu.menu_search);
        toolbar_search.setNavigationOnClickListener(v->
            startActivity(new Intent().setClass(SearchActivity.this, MainActivity.class))
        );

//        toolbar_search.setOnMenuItemClickListener(item -> {
//            if(item .getItemId() == R.id.action_search){
//                Toast.makeText(SearchActivity.this, "Search action", Toast.LENGTH_SHORT).show();
//            }
//            return false;
//        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_action);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.onActionViewExpanded();
        searchView.setQueryHint("search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                return false;
            }
        });
        searchView.setSubmitButtonEnabled(true);

        return super.onCreateOptionsMenu(menu);
    }

    private void click(){
        //点击事件触发：1.跳转searchListFragment 2.传递数据-搜索关键词 - 创建SearchListFragment实例，携带String参数
        toolbar_search.setTitle(key_word);
        SearchListFragment searchListFragment = new SearchListFragment().instance(key_word);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.search_content,searchListFragment);

    }

}
