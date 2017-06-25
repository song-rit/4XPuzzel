package com.example.awidcha.numbergame.ui.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.awidcha.numbergame.R;
import com.example.awidcha.numbergame.constants.Constant;
import com.example.awidcha.numbergame.utils.CheckNetworkConnection;
import com.example.awidcha.numbergame.utils.OkHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GameFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText mEditText1;
    private EditText mEditText2;
    private EditText mEditText3;
    private EditText mEditText4;

    //    private EditText mEditText;
    private Button mButtonOk;
    private int mRandomNumber;

    private Dialog mDialog;

    // Declare field http handler
    private String mThreadName = "httpThread";
    private Handler mHttpHandler;
    private Runnable mHttpRunnable;
    private HandlerThread mHttpThread;

    private FragmentActivity mActivity;

    private ProgressDialog mProgressDialog;

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
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
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

        // Get random number from API
        requestNumber();
        mButtonOk.setOnClickListener(buttonOkOnClickListener());

        mEditText1.addTextChangedListener(getAddTextChangedListener(mEditText1, mEditText2));
        mEditText2.addTextChangedListener(getAddTextChangedListener(mEditText2, mEditText3));
        mEditText3.addTextChangedListener(getAddTextChangedListener(mEditText3, mEditText4));
        mEditText4.addTextChangedListener(getAddTextChangedListener(mEditText4, mEditText4));

        mEditText1.setOnFocusChangeListener(getOnFocusChangeListener());
        mEditText2.setOnFocusChangeListener(getOnFocusChangeListener());
        mEditText3.setOnFocusChangeListener(getOnFocusChangeListener());
        mEditText4.setOnFocusChangeListener(getOnFocusChangeListener());

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("GameFragment", "onResume");
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(mActivity, ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("กำลังสุ่มตัวเลข");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }


    private View.OnClickListener buttonOkOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditText1.getText().toString().equals("") && mEditText2.getText().toString().equals("") && mEditText3.getText().toString().equals("") && mEditText4.getText().toString().equals("")) {
                    Toast.makeText(mActivity, "กรุณณาใส่ตัวเลข", Toast.LENGTH_SHORT).show();
                } else {
                    int inputNumber1 = Integer.parseInt(mEditText1.getText().toString()) * 1000;
                    int inputNumber2 = Integer.parseInt(mEditText2.getText().toString()) * 100;
                    int inputNumber3 = Integer.parseInt(mEditText3.getText().toString()) * 10;
                    int inputNumber4 = Integer.parseInt(mEditText4.getText().toString());

                    int number = inputNumber1 + inputNumber2 + inputNumber3 + inputNumber4;

                    boolean compare = mRandomNumber == number;

                    if (compare) {
                        Toast.makeText(mActivity, "คุณทายถูก", Toast.LENGTH_SHORT).show();
                        showDialog();

                    } else {
                        if (number > mRandomNumber) {
                            Toast.makeText(mActivity, "คุณทายผิด เยอะไป", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, "คุณทายผิด น้อยไป", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };
    }

    private void requestNumber() {

        if (CheckNetworkConnection.isConnectionAvailable(mActivity)) {

            showProgressDialog();

            mHttpRunnable = new Runnable() {
                @Override
                public void run() {

                    OkHttpRequest okHttp = new OkHttpRequest();

                    try {
                        Message msg = okHttp.HttpPostMessage(Constant.API_URL, getRandomRequestBody());
                        String responseJson = (String) msg.obj;

                        if (msg.what == 1) {

                            // Parse json random number
                            JSONObject jsonObject = new JSONObject(responseJson);
                            jsonObject = jsonObject.getJSONObject("result");
                            jsonObject = jsonObject.getJSONObject("random");
                            JSONArray data = jsonObject.getJSONArray("data");
                            mRandomNumber = data.getInt(0);

                            // Focus first input number
                            focusKeyBoardInput(mEditText1);

                            Toast.makeText(mActivity, String.valueOf(mRandomNumber), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(mActivity, "Ready", Toast.LENGTH_SHORT).show();

                            // Hide ProgressDialog
                            mProgressDialog.dismiss();

                        } else {
                            requestFail();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        requestFail();
                    } catch (JSONException e) {
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
        } else {
            requestFail();
        }
    }

    private void showDialog() {
        FragmentManager fm = getFragmentManager();
        GameDialogFragment dialog = new GameDialogFragment();

        //Show GameDialogFragment
        dialog.setCancelable(false);
        dialog.show(fm, "End Game");

    }

    private void requestFail() {
        mProgressDialog.dismiss();

        hideKeyBoardInput();

        Toast.makeText(mActivity, "Fail", Toast.LENGTH_SHORT).show();
        FragmentManager manager = mActivity.getSupportFragmentManager();
        manager.popBackStack(MenuFragment.S_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void hideKeyBoardInput() {
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void focusKeyBoardInput(final EditText e) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                e.requestFocus();
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(e, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    private TextWatcher getAddTextChangedListener(final EditText current, final EditText nextFocus) {
        return new TextWatcher() {
            CharSequence before;
            CharSequence changed;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                current.getText().clear();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals("") && nextFocus.getText().toString().equals("")) {
//                    Toast.makeText(getContext(), s.toString(), Toast.LENGTH_SHORT).show();
                    focusKeyBoardInput(nextFocus);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private View.OnFocusChangeListener getOnFocusChangeListener() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                ((EditText) v).getText().clear();
                if (hasFocus)
                    ((EditText) v).setText("");

//                if (mEditText1.getText().toString().equals("")) {
//                    focusKeyBoardInput(mEditText1);
//                } else if (mEditText2.getText().toString().equals("")) {
//                    focusKeyBoardInput(mEditText2);
//                } else if (mEditText3.getText().toString().equals("")) {
//                    focusKeyBoardInput(mEditText3);
//                }
            }
        };
    }

    private String getRandomRequestBody() {
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
//        mEditText = (EditText) rootView.findViewById(R.id.edit_number);

        mEditText1 = (EditText) rootView.findViewById(R.id.edit_number1);
        mEditText2 = (EditText) rootView.findViewById(R.id.edit_number2);
        mEditText3 = (EditText) rootView.findViewById(R.id.edit_number3);
        mEditText4 = (EditText) rootView.findViewById(R.id.edit_number4);
    }
}
