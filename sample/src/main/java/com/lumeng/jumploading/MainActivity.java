package com.lumeng.jumploading;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

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
        array.clear();
        super.onStop();
    }

}
