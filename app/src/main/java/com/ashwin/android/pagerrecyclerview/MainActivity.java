package com.ashwin.android.pagerrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements PagerRecyclerView.PagerAdapter.onItemClickListener<User> {
    private static final String SUB_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PagerRecyclerView userPagerRecyclerView = findViewById(R.id.user_pager_recycler_view);

        UserListAdapter userListAdapter = new UserListAdapter();
        userListAdapter.setOnItemClickListener(this);

        // TODO: Support LinearLayoutManager
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        userPagerRecyclerView.setLayoutManager(layoutManager);

        userPagerRecyclerView.setAdapter(userListAdapter);
        userPagerRecyclerView.setOnePageSize(3);

        userListAdapter.updateData(UserDataSource.findAll());

        Button prevButton = findViewById(R.id.prev_button);
        prevButton.setOnClickListener(v -> {
            Log.d(Constant.APP_TAG, SUB_TAG + ": prevButton");
            userPagerRecyclerView.previousPage();
        });

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> {
            Log.d(Constant.APP_TAG, SUB_TAG + ": nextButton");
            userPagerRecyclerView.nextPage();
        });
    }

    @Override
    public void onItemClick(View view, User data) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onItemClick( user: " + data + " )");
    }
}
