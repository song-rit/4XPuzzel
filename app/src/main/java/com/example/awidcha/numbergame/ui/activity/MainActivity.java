package com.example.awidcha.numbergame.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.awidcha.numbergame.R;
import com.example.awidcha.numbergame.ui.fragment.GameFragment;
import com.example.awidcha.numbergame.ui.fragment.MenuFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = MenuFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
//            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        clearBackStack();
        FragmentTransaction transaction = manager.openTransaction();
        transaction.replace(R.id.fragment_main, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
