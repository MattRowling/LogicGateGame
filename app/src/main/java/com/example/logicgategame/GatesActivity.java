package com.example.logicgategame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class GatesActivity extends AppCompatActivity {

    private final int and = R.drawable.and;
    private final int nand = R.drawable.nand;
    private final int or = R.drawable.or;
    private final int nor = R.drawable.nor;
    private final int xor = R.drawable.xor;
    private final int xnor = R.drawable.xnor;

    private final String[] names = {
            "AND",
            "NAND",
            "OR",
            "NOR",
            "XOR",
            "XNOR"
    };
    private final int[] gates = {
            R.drawable.and,
            R.drawable.nand,
            R.drawable.or,
            R.drawable.nor,
            R.drawable.xor,
            R.drawable.nor
    };
    private final String[] explanations = {
            "Output is TRUE if and only if both inputs are TRUE",
            "Output is TRUE if and only if at least one input is FALSE",
            "Output is TRUE if and only if at least one input is TRUE",
            "Output is TRUE if and only if both inputs are FALSE",
            "Output is TRUE if and only if both inputs are different",
            "Output is TRUE if and only if both inputs are the same"
    };
    private final String[][] outputs = {
            {"1", "0", "0", "0"},
            {"0", "1", "1", "1"},
            {"1", "1", "1", "0"},
            {"0", "0", "0", "1"},
            {"0", "1", "0", "0"},
            {"1", "0", "0", "1"}
    };

    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gates);

        Bundle b = getIntent().getExtras();
        currentLevel = b.getInt("level");
        int gate = b.getInt("gate");

        showDetails(gate);
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, LevelActivity.class);
        Bundle b = new Bundle();
        b.putInt("level", currentLevel);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void showDetails(int gate) {
        int gateNum = -1;
        switch (gate) {
            case and:
                gateNum = 0;
                break;
            case nand:
                gateNum = 1;
                break;
            case or:
                gateNum = 2;
                break;
            case nor:
                gateNum = 3;
                break;
            case xor:
                gateNum = 4;
                break;
            case xnor:
                gateNum = 5;
                break;
        }
        TextView name = findViewById(R.id.name);
        name.setText(names[gateNum]);
        ImageView gateView = findViewById(R.id.gate);
        gateView.setImageResource(gates[gateNum]);
        TextView explanation = findViewById(R.id.explanation);
        explanation.setText(explanations[gateNum]);
        TextView output1 = findViewById(R.id.output1);
        output1.setText(outputs[gateNum][0]);
        TextView output2 = findViewById(R.id.output2);
        output2.setText(outputs[gateNum][1]);
        TextView output3 = findViewById(R.id.output3);
        output3.setText(outputs[gateNum][2]);
        TextView output4 = findViewById(R.id.output4);
        output4.setText(outputs[gateNum][3]);
    }
}