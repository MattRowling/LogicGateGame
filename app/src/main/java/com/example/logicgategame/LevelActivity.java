package com.example.logicgategame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class LevelActivity extends AppCompatActivity {

    private final String tag = "LevelActivity";

    private final int and = R.drawable.and;
    private final int nand = R.drawable.nand;
    private final int or = R.drawable.or;
    private final int nor = R.drawable.nor;
    private final int xor = R.drawable.xor;
    private final int xnor = R.drawable.xnor;

    private boolean[] powers = {false, false, false, false};

    private int[] gates;
    private int goal;

    private int currentLevel;

    private Canvas mCanvas;
    private Paint mPaint = new Paint();
    private Bitmap mBitmap;
    private ImageView mImageView;
    private int[] topGateLoc = new int[2];
    private int[] leftGateLoc = new int[2];
    private int[] rightGateLoc = new int[2];
    private int[] button1Loc = new int[2];
    private int[] button2Loc = new int[2];
    private int[] button3Loc = new int[2];
    private int[] button4Loc = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_level);

        mPaint.setColor(Color.GREEN);
        mImageView = (ImageView) findViewById(R.id.background);

        Bundle b = getIntent().getExtras();
        currentLevel = b.getInt("level");
        startLevel();

        ToggleButton toggle1 = (ToggleButton) findViewById(R.id.power1);
        toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                togglePower(0);
            }
        });
        ToggleButton toggle2 = (ToggleButton) findViewById(R.id.power2);
        toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                togglePower(1);
            }
        });
        ToggleButton toggle3 = (ToggleButton) findViewById(R.id.power3);
        toggle3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                togglePower(2);
            }
        });
        ToggleButton toggle4 = (ToggleButton) findViewById(R.id.power4);
        toggle4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                togglePower(3);
            }
        });


        ViewTreeObserver observer = mImageView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawBackground(mImageView);
                if(mImageView.getViewTreeObserver().isAlive()) {
                    mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void togglePower(int index){
        powers[index] = !powers[index];
    }

    public void validate(View view) {
        if (checkMatch()) {
            if (currentLevel >=3) {
                currentLevel = 1;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                currentLevel++;
                startLevel();
            }

        }
    }

    private boolean checkMatch() {
        return (goal == 1 && gate0()) || (goal == 0 && !gate0());
    }

    private boolean gate0() {
        return checkGate(gates[0], gate1(), gate2());
    }

    private boolean gate1() {
        return checkGate(gates[1], powers[0], powers[1]);
    }

    private boolean gate2() {
        return checkGate(gates[2], powers[2], powers[3]);
    }

    private boolean checkGate(int gateType, boolean left, boolean right) {
        switch (gateType) {
            case R.drawable.and:
                return left && right;
            case R.drawable.nand:
                return !(left && right);
            case R.drawable.or:
                return left || right;
            case R.drawable.nor:
                return !(left || right);
            case R.drawable.xor:
                return (left && !right) || (!left && right);
            case R.drawable.xnor:
                return !((left && !right) || (!left && right));
        }
        return false;
    }

    private void startLevel() {
        if (currentLevel == 1) {
            gates = new int[]{and, and, and};
            goal = 1;
        } else if (currentLevel == 2) {
            gates = new int[]{and, and, or};
            goal = 0;
        } else if (currentLevel == 3) {
            gates = new int[]{xnor, xnor, or};
            goal = 1;
        }
        TextView textView = findViewById(R.id.goal);
        textView.setText(String.valueOf(goal));
        ImageView gate1 = findViewById(R.id.gate1);
        gate1.setImageResource(gates[0]);
        ImageView gate2 = findViewById(R.id.gate2);
        gate2.setImageResource(gates[1]);
        ImageView gate3 = findViewById(R.id.gate3);
        gate3.setImageResource(gates[2]);
    }

    public void drawBackground(View view) {
        int vWidth = view.getWidth();
        int vHeight = view.getHeight();
        findViewById(R.id.gate1).getLocationInWindow(topGateLoc);
        findViewById(R.id.gate2).getLocationInWindow(leftGateLoc);
        findViewById(R.id.gate3).getLocationInWindow(rightGateLoc);
        int gateHeight = findViewById(R.id.gate1).getHeight();
        int gateWidth = findViewById(R.id.gate1).getWidth();
        int wireOffset = gateWidth*2 / 21;
        findViewById(R.id.power1).getLocationInWindow(button1Loc);
        findViewById(R.id.power2).getLocationInWindow(button2Loc);
        findViewById(R.id.power3).getLocationInWindow(button3Loc);
        findViewById(R.id.power4).getLocationInWindow(button4Loc);
        int button1X = button1Loc[0] + findViewById(R.id.power1).getWidth()/2;
        int button2X = button2Loc[0] + findViewById(R.id.power2).getWidth()/2;
        int button3X = button3Loc[0] + findViewById(R.id.power3).getWidth()/2;
        int button4X = button4Loc[0] + findViewById(R.id.power4).getWidth()/2;

        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(9);
        mCanvas.drawLine(view.getWidth()/2, 16+48, topGateLoc[0]+gateWidth/2, topGateLoc[1], mPaint);
        mCanvas.drawLine(topGateLoc[0]+gateWidth/2-wireOffset, topGateLoc[1]+gateHeight, leftGateLoc[0]+gateWidth/2, leftGateLoc[1], mPaint);
        mCanvas.drawLine(topGateLoc[0]+gateWidth/2+wireOffset, topGateLoc[1]+gateHeight, rightGateLoc[0]+gateWidth/2, rightGateLoc[1], mPaint);
        mCanvas.drawLine(leftGateLoc[0]+gateWidth/2-wireOffset, leftGateLoc[1]+gateHeight, button1X, button1Loc[1], mPaint);
        mCanvas.drawLine(leftGateLoc[0]+gateWidth/2+wireOffset, leftGateLoc[1]+gateHeight, button2X, button2Loc[1], mPaint);
        mCanvas.drawLine(rightGateLoc[0]+gateWidth/2-wireOffset, rightGateLoc[1]+gateHeight, button3X, button3Loc[1], mPaint);
        mCanvas.drawLine(rightGateLoc[0]+gateWidth/2+wireOffset, rightGateLoc[1]+gateHeight, button4X, button4Loc[1], mPaint);

        view.invalidate();
    }
}