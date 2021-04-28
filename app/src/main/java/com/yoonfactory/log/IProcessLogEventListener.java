package com.yoonfactory.log;

import android.graphics.Color;

public interface IProcessLogEventListener {
    void onProcessLogEvent(String strMessage, Color pColor);
}