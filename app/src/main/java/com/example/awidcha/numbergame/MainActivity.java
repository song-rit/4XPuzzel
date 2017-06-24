package com.example.awidcha.numbergame;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.awidcha.numbergame.constants.Constant;
import com.example.awidcha.numbergame.utils.CheckNetworkConnection;
import com.example.awidcha.numbergame.utils.OkHttpRequest;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button mButtonOk;
    private EditText mEditText;
    private int mRandomNumber;


    // Declare field http handler
    private String mThreadName = "httpThread";
    private Handler mHttpHandler;
    private Runnable mHttpRunnable;
    private HandlerThread mHttpThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infixView();

        mRandomNumber = getRandomNumber();
        mButtonOk.setOnClickListener(buttonOkClickListener());

        sendRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private int getRandomNumber() {
        int randomNumber = (int) (Math.random() * 10000);
        return randomNumber;
    }

    private View.OnClickListener buttonOkClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "กรุณณาใส่ตัวเลข", Toast.LENGTH_SHORT).show();
                } else {
                    int inputNumber = Integer.parseInt(mEditText.getText().toString());
                    boolean compare = mRandomNumber == inputNumber;

                    if (compare) {
                        Toast.makeText(getApplicationContext(), "คุณทายถูก", Toast.LENGTH_SHORT).show();
                    } else {
                        if (inputNumber > mRandomNumber) {
                            Toast.makeText(getApplicationContext(), "คุณทายผิด เยอะไป", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "คุณทายผิด น้อยไป", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };
    }

    private void sendRequest() {

        if (CheckNetworkConnection.isConnectionAvailable(this)) {
            mHttpRunnable = new Runnable() {
                @Override
                public void run() {

                    OkHttpRequest okHttp = new OkHttpRequest();

                    try {
                        Message msg = okHttp.HttpPostMessage(Constant.API_URL, getRandomRequestBody());
                        String responseJson = (String) msg.obj;

                        if (msg.what == 1) {

                            Toast.makeText(getApplicationContext(), responseJson, Toast.LENGTH_SHORT).show();

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

    private void infixView() {
        mButtonOk = (Button) findViewById(R.id.button_ok);
        mEditText = (EditText) findViewById(R.id.edit_number);
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
}
