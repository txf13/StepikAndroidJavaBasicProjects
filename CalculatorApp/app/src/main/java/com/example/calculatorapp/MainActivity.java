package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText Num1, Num2, Operation;
    TextView ResultText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Num1=findViewById(R.id.editText1);
        Num2=findViewById(R.id.editText2);
        Operation=findViewById(R.id.editTextOperation);
        ResultText=findViewById(R.id.textViewResult);
        button=findViewById(R.id.button);
        button.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        float num1, num2, result=0;
        num1=Float.parseFloat(Num1.getText().toString());
        num2=Float.parseFloat(Num2.getText().toString());
        String task=Operation.getText().toString();
        switch (task){
            case "+": result=num1+num2; break;
            case "-": result=num1-num2; break;
            case "*": result=num1*num2; break;
            case "/": result=num1/num2; break;
            default: break;
        }
        ResultText.setText(num1+" "+task+" "+num2+"="+result);
    }
}
