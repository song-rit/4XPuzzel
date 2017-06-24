package com.example.awidcha.numbergame.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.awidcha.numbergame.R;
import com.example.awidcha.numbergame.constants.Constant;
import com.example.awidcha.numbergame.utils.CheckNetworkConnection;
import com.example.awidcha.numbergame.utils.OkHttpRequest;

import java.io.IOException;

public class GameFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText mEditText;
    private Button mButtonOk;
    private int mRandomNumber;


    // Declare field http handler
    private String mThreadName = "httpThread";
    private Handler mHttpHandler;
    private Runnable mHttpRunnable;
    private HandlerThread mHttpThread;

    private FragmentActivity mActivity;


    public GameFragment() {
        // Required empty public constructor
    }

    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);;

        infixView(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRandomNumber = getRandomNumber();
        mButtonOk.setOnClickListener(buttonOkOnClickListener());

        sendRequest();
    }


    private int getRandomNumber() {
        int randomNumber = (int) (Math.random() * 10000);
        return randomNumber;
    }

    private View.OnClickListener buttonOkOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText().toString().equals("")) {
                    Toast.makeText(mActivity, "กรุณณาใส่ตัวเลข", Toast.LENGTH_SHORT).show();
                } else {
                    int inputNumber = Integer.parseInt(mEditText.getText().toString());
                    boolean compare = mRandomNumber == inputNumber;

                    if (compare) {
                        Toast.makeText(mActivity, "คุณทายถูก", Toast.LENGTH_SHORT).show();
                    } else {
                        if (inputNumber > mRandomNumber) {
                            Toast.makeText(mActivity, "คุณทายผิด เยอะไป", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, "คุณทายผิด น้อยไป", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };
    }

    private void sendRequest() {

        if (CheckNetworkConnection.isConnectionAvailable(mActivity)) {
            mHttpRunnable = new Runnable() {
                @Override
                public void run() {

                    OkHttpRequest okHttp = new OkHttpRequest();

                    try {
                        Message msg = okHttp.HttpPostMessage(Constant.API_URL, getRandomRequestBody());
                        String responseJson = (String) msg.obj;

                        if (msg.what == 1) {
                            Toast.makeText(mActivity, responseJson, Toast.LENGTH_SHORT).show();

                        } else {
//                            updateViewFail();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            mHttpThread = new HandlerThread(mThreadName) {
                @Override
                public void interrupt() {
                    super.interrupt();
                    mHttpHandler.removeCallbacks(mHttpRunnable);
                    mHttpHandler.removeCallbacksAndMessages(null);
                    this.quit();
                }
            };
            mHttpThread.start();
            mHttpHandler = new Handler(mHttpThread.getLooper());
            mHttpHandler.post(mHttpRunnable);
        }
    }

    public String getRandomRequestBody() {
        return "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"generateIntegers\",\n" +
                "    \"params\": {\n" +
                "        \"apiKey\": \"a8e0614b-0ac2-498f-9a0e-5c054063a5aa\",\n" +
                "        \"n\": 1,\n" +
                "        \"min\": 1000,\n" +
                "        \"max\": 9999,\n" +
                "        \"replacement\": true,\n" +
                "        \"base\": 10\n" +
                "    },\n" +
                "    \"id\": 7573\n" +
                "}";
    }

    private void infixView(View rootView) {
        mButtonOk = (Button) rootView.findViewById(R.id.button_ok);
        mEditText = (EditText) rootView.findViewById(R.id.edit_number);
    }
}
