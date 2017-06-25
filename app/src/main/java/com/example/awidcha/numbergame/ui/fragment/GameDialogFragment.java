package com.example.awidcha.numbergame.ui.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.awidcha.numbergame.R;

public class GameDialogFragment extends DialogFragment {
    private static final String ARG_NUMBER = "number";
    private static final String ARG_LEAST_POINT = "leastPoint";
    private static final String ARG_TOTAL_POINT = "totalPoint";

    private Button mButtonRestart;
    private TextView mTextNumber;
    private TextView mTextLeastPoint;
    private TextView mTextTotalPoint;
    private FragmentActivity mActivity;

    private int number;
    private int leastPoint;
    private int totalPoint;

    public GameDialogFragment() {
        // Required empty public constructor
    }

    public static GameDialogFragment newInstance(int number, int leastPoint, int totalPoint) {

        Log.d("GameDialogFragment", "newInstance:" + String.valueOf(number) + ", " + String.valueOf(leastPoint) + ", " + String.valueOf(totalPoint));

        GameDialogFragment fragment = new GameDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, number);
        args.putInt(ARG_LEAST_POINT, leastPoint);
        args.putInt(ARG_TOTAL_POINT, totalPoint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            this.number = savedInstanceState.getInt(ARG_NUMBER);
            this.leastPoint = savedInstanceState.getInt(ARG_LEAST_POINT);
            this.totalPoint = savedInstanceState.getInt(ARG_TOTAL_POINT);
            Log.d("GameDialogFragment", "onCreate:" + String.valueOf(number) + ", " + String.valueOf(leastPoint) + ", " + String.valueOf(totalPoint));

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("GameDialogFragment", "onCreateDialog");

        mActivity = getActivity();

        if (savedInstanceState != null) {
            this.number = savedInstanceState.getInt(ARG_NUMBER);
            this.leastPoint = savedInstanceState.getInt(ARG_LEAST_POINT);
            this.totalPoint = savedInstanceState.getInt(ARG_TOTAL_POINT);
        } else {
            Bundle args = getArguments();
            this.number = args.getInt(ARG_NUMBER);
            this.leastPoint = args.getInt(ARG_LEAST_POINT);
            this.totalPoint = args.getInt(ARG_TOTAL_POINT);
        }

        Log.d("GameDialogFragment", "onCreateDialog:" + String.valueOf(number) + ", " + String.valueOf(leastPoint) + ", " + String.valueOf(totalPoint));


        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.fragment_game_dialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        infixView(dialog);

        updateView();
        mButtonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private void updateView() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextNumber.setText(String.valueOf(number));
                mTextLeastPoint.setText(String.valueOf(leastPoint));
                mTextTotalPoint.setText(String.valueOf(totalPoint));
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

// Solve problem call back not active when dismiss dialog
        FragmentManager fm = getFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        Fragment fragment = GameFragment.newInstance();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_main, fragment);
        transaction.addToBackStack(MenuFragment.S_TAG);
        transaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_NUMBER, number);
        outState.putInt(ARG_LEAST_POINT, leastPoint);
        outState.putInt(ARG_TOTAL_POINT, totalPoint);
    }

    private void infixView(Dialog dialog) {
        mButtonRestart = (Button) dialog.findViewById(R.id.button_restart);
        mTextLeastPoint = (TextView) dialog.findViewById(R.id.text_view_least);
        mTextNumber = (TextView) dialog.findViewById(R.id.text_view_number);
        mTextTotalPoint = (TextView) dialog.findViewById(R.id.text_view_total);
    }
}
