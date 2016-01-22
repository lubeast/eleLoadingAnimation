package com.lumeng.jumploading;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import com.lumeng.jump_loading.LoadingView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.loadingView)
    LoadingView loadingView;

    SparseArray<Integer> array = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initRes();

        loadingView.setResource(array);
    }

    private void initRes() {
        array.append(array.size(), R.mipmap.loading_group_11);
        array.append(array.size(), R.mipmap.loading_group_12);
        array.append(array.size(), R.mipmap.loading_group_13);
        array.append(array.size(), R.mipmap.loading_group_21);
        array.append(array.size(), R.mipmap.loading_group_22);
        array.append(array.size(), R.mipmap.loading_group_23);
        array.append(array.size(), R.mipmap.loading_group_31);
        array.append(array.size(), R.mipmap.loading_group_32);
        array.append(array.size(), R.mipmap.loading_group_33);
    }

    @Override
    protected void onStop() {
        loadingView.stopAnimation();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about){
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
