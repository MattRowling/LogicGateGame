package com.example.logicgategame;

import android.app.Application;

public class MyApplication extends Application {

    private int levelReached = 1;

    public int getLevelReached(){
        return levelReached;
    }

    public void updateLevelReached(int currentLevel) {
        if (currentLevel > levelReached) {
            this.levelReached = currentLevel;
        }
    }
}
