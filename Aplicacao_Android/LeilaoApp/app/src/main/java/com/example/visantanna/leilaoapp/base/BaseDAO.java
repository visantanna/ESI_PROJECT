package com.example.visantanna.leilaoapp.base;

import android.app.Activity;
import android.widget.ProgressBar;

import java.util.Observable;

/**
 * Created by vinicius on 05/12/17.
 */

public class BaseDAO extends Observable{
    Activity activity;
    ProgressBar progressBar;

    public BaseDAO(Activity activity, ProgressBar progressBar) {
        this.activity = activity;
        this.progressBar = progressBar;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
