package com.example.logicgategame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
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
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private final String tag = "MainActivity";

    private Canvas mCanvas;
    private Paint mPaint = new Paint();
    private Bitmap mBitmap;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.mainBackground);

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

    public void toLevel(View view) {
        String level = (String) view.getTag();
        int levelNum = Integer.parseInt(level);
        if (levelNum <= 3) { // REMOVE WHEN IMPLEMENTING MORE LEVELS
            Intent intent = new Intent(this, LevelActivity.class);
            Bundle b = new Bundle();
            b.putInt("level", levelNum);
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    public void drawBackground(View view) {
        int vWidth = view.getWidth();
        int vHeight = view.getHeight();

        int[][] buttonLocs = new int[16][2];
        findViewById(R.id.levelButton1).getLocationInWindow(buttonLocs[0]);
        findViewById(R.id.levelButton2).getLocationInWindow(buttonLocs[1]);
        findViewById(R.id.levelButton3).getLocationInWindow(buttonLocs[2]);
        findViewById(R.id.levelButton4).getLocationInWindow(buttonLocs[3]);
        findViewById(R.id.levelButton5).getLocationInWindow(buttonLocs[4]);
        findViewById(R.id.levelButton6).getLocationInWindow(buttonLocs[5]);
        findViewById(R.id.levelButton7).getLocationInWindow(buttonLocs[6]);
        findViewById(R.id.levelButton8).getLocationInWindow(buttonLocs[7]);
        findViewById(R.id.levelButton9).getLocationInWindow(buttonLocs[8]);
        findViewById(R.id.levelButton10).getLocationInWindow(buttonLocs[9]);
        findViewById(R.id.levelButton11).getLocationInWindow(buttonLocs[10]);
        findViewById(R.id.levelButton12).getLocationInWindow(buttonLocs[11]);
        findViewById(R.id.levelButton13).getLocationInWindow(buttonLocs[12]);
        findViewById(R.id.levelButton14).getLocationInWindow(buttonLocs[13]);
        findViewById(R.id.levelButton15).getLocationInWindow(buttonLocs[14]);
        findViewById(R.id.levelButton16).getLocationInWindow(buttonLocs[15]);

        int buttonHeight = findViewById(R.id.levelButton1).getHeight();
        int buttonWidth = findViewById(R.id.levelButton1).getWidth();
        int leftWireOffset = buttonWidth*3/8;
        int rightWireOffset = buttonWidth*5/8;

        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(4);

        mCanvas.drawLine(buttonLocs[15][0]+buttonWidth/2, buttonLocs[15][1]+buttonHeight, vWidth/2, buttonLocs[14][1], mPaint);

        mCanvas.drawLine(buttonLocs[14][0]+leftWireOffset, buttonLocs[14][1]+buttonHeight, buttonLocs[12][0]+buttonWidth/2, buttonLocs[12][1], mPaint);
        mCanvas.drawLine(buttonLocs[14][0]+rightWireOffset, buttonLocs[14][1]+buttonHeight, buttonLocs[13][0]+buttonWidth/2, buttonLocs[13][1], mPaint);

        mCanvas.drawLine(buttonLocs[12][0]+leftWireOffset, buttonLocs[12][1]+buttonHeight, buttonLocs[8][0]+buttonWidth/2, buttonLocs[8][1], mPaint);
        mCanvas.drawLine(buttonLocs[12][0]+rightWireOffset, buttonLocs[12][1]+buttonHeight, buttonLocs[9][0]+buttonWidth/2, buttonLocs[9][1], mPaint);
        mCanvas.drawLine(buttonLocs[13][0]+leftWireOffset, buttonLocs[13][1]+buttonHeight, buttonLocs[10][0]+buttonWidth/2, buttonLocs[10][1], mPaint);
        mCanvas.drawLine(buttonLocs[13][0]+rightWireOffset, buttonLocs[13][1]+buttonHeight, buttonLocs[11][0]+buttonWidth/2, buttonLocs[11][1], mPaint);

        mCanvas.drawLine(buttonLocs[8][0]+leftWireOffset, buttonLocs[8][1]+buttonHeight, buttonLocs[0][0]+buttonWidth/2, buttonLocs[0][1], mPaint);
        mCanvas.drawLine(buttonLocs[8][0]+rightWireOffset, buttonLocs[8][1]+buttonHeight, buttonLocs[1][0]+buttonWidth/2, buttonLocs[1][1], mPaint);
        mCanvas.drawLine(buttonLocs[9][0]+leftWireOffset, buttonLocs[9][1]+buttonHeight, buttonLocs[2][0]+buttonWidth/2, buttonLocs[2][1], mPaint);
        mCanvas.drawLine(buttonLocs[9][0]+rightWireOffset, buttonLocs[9][1]+buttonHeight, buttonLocs[3][0]+buttonWidth/2, buttonLocs[3][1], mPaint);
        mCanvas.drawLine(buttonLocs[10][0]+leftWireOffset, buttonLocs[10][1]+buttonHeight, buttonLocs[4][0]+buttonWidth/2, buttonLocs[4][1], mPaint);
        mCanvas.drawLine(buttonLocs[10][0]+rightWireOffset, buttonLocs[10][1]+buttonHeight, buttonLocs[5][0]+buttonWidth/2, buttonLocs[5][1], mPaint);
        mCanvas.drawLine(buttonLocs[11][0]+leftWireOffset, buttonLocs[11][1]+buttonHeight, buttonLocs[6][0]+buttonWidth/2, buttonLocs[6][1], mPaint);
        mCanvas.drawLine(buttonLocs[11][0]+rightWireOffset, buttonLocs[11][1]+buttonHeight, buttonLocs[7][0]+buttonWidth/2, buttonLocs[7][1], mPaint);

        view.invalidate();
    }
}