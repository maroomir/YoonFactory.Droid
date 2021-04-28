package com.yoonfactory.log;

import android.graphics.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogEventHandler {
    private static final int MAX_THREAD_POOL = 5;
    private static final List<IProcessLogEventListener> m_pListProcessLogListener = new CopyOnWriteArrayList<>();

    private static synchronized List<IProcessLogEventListener> getProcessLogListenerList() {
        return m_pListProcessLogListener;
    }

    public static synchronized void addProcessLogListener(IProcessLogEventListener eventListener) {
        if (!getProcessLogListenerList().contains(eventListener)) {
            m_pListProcessLogListener.add(eventListener);
        }
    }

    public static synchronized void removeProcessLogListener(IProcessLogEventListener eventListener) {
        if (getProcessLogListenerList().contains(eventListener)) {
            m_pListProcessLogListener.remove(eventListener);
        }
    }

    public static synchronized void callProcessLogEvent(final Class<?> caller, final String strMessage, final Color pColor) {
        callProcessLogEvent(caller, strMessage, pColor, true);
    }

    public static synchronized void callProcessLogEvent(final Class<?> caller, final String strMessage, final Color pColor, boolean bDoAsync) {
        if (bDoAsync) {
            callProcessLogEventByAsync(caller, strMessage, pColor);
        } else {
            callProcessLogEventBySync(caller, strMessage, pColor);
        }
    }

    private static synchronized void callProcessLogEventByAsync(final Class<?> caller, final String strMessage, final Color pColor) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);
        for (final IProcessLogEventListener listener : m_pListProcessLogListener) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (!listener.getClass().getName().equals(caller.getName())) {
                        listener.onProcessLogEvent(strMessage, pColor);
                    }
                }
            });
        }
    }

    private static synchronized void callProcessLogEventBySync(final Class<?> caller, final String strMessage, final Color pColor) {
        for (final IProcessLogEventListener listener : m_pListProcessLogListener) {
            if (!listener.getClass().getName().equals(caller.getName())) {
                listener.onProcessLogEvent(strMessage, pColor);
            }
        }
    }
}