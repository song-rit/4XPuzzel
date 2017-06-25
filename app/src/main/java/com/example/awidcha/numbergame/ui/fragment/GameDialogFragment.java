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

import com.example.awidcha.numbergame.R;
import com.example.awidcha.numbergame.utils.CustomDialogFragment;
import com.example.awidcha.numbergame.utils.MyCallBack;

public class GameDialogFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button mButtonRestart;
    private FragmentActivity mActivity;

    private String mParam1;
    private String mParam2;


    private OnAddFriendListener callback;

    public interface OnAddFriendListener {
        public void onAddFriendSubmit(String friendEmail);
    }


    OnSubmitListener mListener;

    interface OnSubmitListener {
        void setOnSubmitListener(String str);
    }

    ;


    public GameDialogFragment() {
        // Required empty public constructor
    }

    public static GameDialogFragment newInstance() {
        GameDialogFragment fragment = new GameDialogFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        callback = (OnAddFriendListener) getTargetFragment();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("GameDialogFragment", "onCreateDialog");
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.fragment_game_dialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        mButtonRestart = (Button) dialog.findViewById(R.id.button_restart);
        mButtonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        callback = (OnAddFriendListener) getTargetFragment();
//        callback.onAddFriendSubmit("hello world");
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

    private View.OnClickListener buttonRestartOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GameDialogFragment", "buttonRestartOnClickListener");
            }
        };
    }
}
