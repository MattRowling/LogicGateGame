package com.example.logicgategame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
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
    private boolean goal;

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

    private final int[][] levels = {
            {and, and, and, 1}, // 1
            {nand, and, and, 1}, // 2
            {nand, nand, nand, 0}, // 3
            {nand, nand, and, 1}, // 4
            {and, nand, nand, 1}, // 5
            {and, and, nand, 0}, // 6
            {or, or, or, 1}, // 7
            {or, and, and, 1}, // 8
            {nand, or, nand, 0}, // 9
            {nor, nand, nand, 1}, // 10
            {or, nor, nor, 0}, // 11
            {and, nor, nor, 0}, // 12
            {xor, and, and, 1}, // 13
            {xnor, or, nor, 1}, // 14
            {and, xnor, nor, 1}, // 15
            {xor, or, xnor, 1}, // 16
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_level);
        this.getWindow().setEnterTransition(new Explode());

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

    public void toGates(View view) {
        String tag = (String) view.getTag();
        int gateNum = Integer.parseInt(tag);
        int gate = levels[currentLevel-1][gateNum-1];
        Intent intent = new Intent(this, GatesActivity.class);
        Bundle b = new Bundle();
        b.putInt("level", currentLevel);
        b.putInt("gate", gate);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void validate(View view) {
        if (checkMatch()) {
            if (currentLevel >=16) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                correct();
            }
        } else {
            incorrect();
        }
    }

    private void togglePower(int index){
        powers[index] = !powers[index];
    }

    private void correct() {
        mImageView.setBackgroundColor(Color.parseColor("#C7DBA0"));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mImageView.setBackgroundColor(Color.parseColor("#B5C1DB"));
                currentLevel++;
                updateLevelReached();
                nextLevel();
            }
        }, 1000);
    }

    public void updateLevelReached() {
        ((MyApplication) this.getApplication()).updateLevelReached(currentLevel);
    }

    private void incorrect() {
        mImageView.setBackgroundColor(Color.parseColor("#DBAAA0"));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mImageView.setBackgroundColor(Color.parseColor("#B5C1DB"));
            }
        }, 1000);
    }

    private void nextLevel() {
        Intent intent = new Intent(this, LevelActivity.class);
        Bundle b = new Bundle();
        b.putInt("level", currentLevel+1);
        intent.putExtras(b);
        startActivity(intent);
    }

    private boolean checkMatch() {
        return (goal && gate0()) || (!goal && !gate0());
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
        gates = new int[] {levels[currentLevel-1][0], levels[currentLevel-1][1], levels[currentLevel-1][2]};
        TextView textView = findViewById(R.id.goal);
        if (levels[currentLevel-1][3] == 1) {
            goal = true;
            textView.setText("1");
        } else {
            goal = false;
            textView.setText("0");
        }
        ImageView gate1 = findViewById(R.id.gate1);
        gate1.setImageResource(gates[0]);
        ImageView gate2 = findViewById(R.id.gate2);
        gate2.setImageResource(gates[1]);
        ImageView gate3 = findViewById(R.id.gate3);
        gate3.setImageResource(gates[2]);
    }

    private void drawBackground(View view) {
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
        int goalBottom = findViewById(R.id.goal).getBottom();

        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(9);
        mCanvas.drawLine(view.getWidth()/2, goalBottom-4, topGateLoc[0]+gateWidth/2, topGateLoc[1], mPaint);
        mCanvas.drawLine(topGateLoc[0]+gateWidth/2-wireOffset, topGateLoc[1]+gateHeight, leftGateLoc[0]+gateWidth/2, leftGateLoc[1], mPaint);
        mCanvas.drawLine(topGateLoc[0]+gateWidth/2+wireOffset, topGateLoc[1]+gateHeight, rightGateLoc[0]+gateWidth/2, rightGateLoc[1], mPaint);
        mCanvas.drawLine(leftGateLoc[0]+gateWidth/2-wireOffset, leftGateLoc[1]+gateHeight, button1X, button1Loc[1], mPaint);
        mCanvas.drawLine(leftGateLoc[0]+gateWidth/2+wireOffset, leftGateLoc[1]+gateHeight, button2X, button2Loc[1], mPaint);
        mCanvas.drawLine(rightGateLoc[0]+gateWidth/2-wireOffset, rightGateLoc[1]+gateHeight, button3X, button3Loc[1], mPaint);
        mCanvas.drawLine(rightGateLoc[0]+gateWidth/2+wireOffset, rightGateLoc[1]+gateHeight, button4X, button4Loc[1], mPaint);

        view.invalidate();
    }
}