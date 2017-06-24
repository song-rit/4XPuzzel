package com.example.awidcha.numbergame.ui.fragment;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.awidcha.numbergame.R;

public class GameDialogFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button mButtonRestart;
    private FragmentActivity mActivity;

    private String mParam1;
    private String mParam2;

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


    private View.OnClickListener buttonRestartOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GameDialogFragment", "buttonRestartOnClickListener");
            }
        };
    }
}
