package com.example.awidcha.numbergame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mButtonOk;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infixView();

        final int randomNumber = (int) (Math.random() * 10000);
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditText.getText().toString().equals("") ){
                    Toast.makeText(getApplicationContext(), "กรุณณาใส่ตัวเลข", Toast.LENGTH_SHORT).show();
                } else {
                    int inputNumber = Integer.parseInt(mEditText.getText().toString());
                    boolean compare = randomNumber == inputNumber;

                    if(compare){
                        Toast.makeText(getApplicationContext(), "คุณทายถูก", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(inputNumber > randomNumber) {
                            Toast.makeText(getApplicationContext(), "คุณทายผิด เยอะไป", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "คุณทายผิด น้อยไป", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void infixView() {
        mButtonOk = (Button) findViewById(R.id.button_ok);
        mEditText = (EditText) findViewById(R.id.edit_number);
    }
}
