package edu.temple.dmhelper;

import android.app.Application;

public class DmhelperApplication extends Application {
    private boolean dm = false;

    public void setDm(boolean dm) {
        this.dm = dm;
    }

    public boolean isDm() {
        return dm;
    }
}
