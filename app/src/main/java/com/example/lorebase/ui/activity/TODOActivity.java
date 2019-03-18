package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.ui.fragment.TodoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class TODOActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    ViewPager viewPager;

    TodoFragment todoFragment;
    TodoFragment todoDoneFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        todoFragment = TodoFragment.getInstance(false);
        todoDoneFragment = TodoFragment.getInstance(true);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.todo);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        bottomNavigationView = findViewById(R.id.bottom_navigation_TODO);
        bottomNavigationView.setLayoutMode(BottomNavigationView.MEASURED_HEIGHT_STATE_SHIFT); //可在配置在布局文件中
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        fab = findViewById(R.id.btn_fab_TODO);

        viewPager = findViewById(R.id.viewpager_TODO);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        List<Fragment> list = new ArrayList<>();
        list.add(todoFragment);
        list.add(todoDoneFragment);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

    }

    //load the menu file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_todo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //toolbar menu click_event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                break;
            case R.id.action_add_todo:
                Intent intent = new Intent(this, TodoAddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.go_in, R.animator.go_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //底部导航  点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_todo:
                viewPager.setCurrentItem(0);
                fab.setOnClickListener(v -> Toast.makeText(this, "回到顶部 action_todo", Toast.LENGTH_SHORT).show());
                break;
            case R.id.action_complete:
                viewPager.setCurrentItem(1);
                fab.setOnClickListener(v -> Toast.makeText(this, "回到顶部 action_complete", Toast.LENGTH_SHORT).show());
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (todoFragment != null)
            todoFragment = null;
        if (todoDoneFragment != null)
            todoDoneFragment = null;
    }
}


