package com.meitu.meitutietie;

import android.database.ContentObserver;
import android.os.Handler;

/**
 * 监听媒体库更新
 */
public class ChangeObserver extends ContentObserver {

    private boolean contentChanged = false;

    public boolean contentChanged() {
        return contentChanged;
    }

    public void setContentChanged(boolean contentChanged) {
        this.contentChanged = contentChanged;
    }

    public ChangeObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        contentChanged = true;
    }
}