package com.yoonfactory.camera;

import com.yoonfactory.image.YoonImage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraEventHandler {

    private static final int MAX_THREAD_POOL = 5;

    private static final List<IReceiveImageEventListener> m_pListReceiveImageListener = new CopyOnWriteArrayList<IReceiveImageEventListener>();
    private static final List<IReceiveDepthEventListener> m_pListReceiveDepthListener = new CopyOnWriteArrayList<IReceiveDepthEventListener>();

    private static synchronized List<IReceiveImageEventListener> getReceiveImageListenerList() {
        return m_pListReceiveImageListener;
    }

    private static synchronized List<IReceiveDepthEventListener> getReceiveDepthListenerList() {
        return m_pListReceiveDepthListener;
    }

    public static synchronized void addReceiveImageListener(IReceiveImageEventListener eventListener) {
        if (!getReceiveImageListenerList().contains(eventListener)) {
            m_pListReceiveImageListener.add(eventListener);
        }
    }

    public static synchronized void addReceiveDepthListener(IReceiveDepthEventListener eventListener){
        if(!getReceiveDepthListenerList().contains(eventListener)){
            m_pListReceiveDepthListener.add(eventListener);
        }
    }

    public static synchronized void removeReceiveImageListener(IReceiveImageEventListener eventListener) {
        if (getReceiveImageListenerList().contains(eventListener)) {
            m_pListReceiveImageListener.remove(eventListener);
        }
    }

    public static synchronized void removeReceiveDepthListener(IReceiveDepthEventListener eventListener) {
        if (getReceiveDepthListenerList().contains(eventListener)) {
            m_pListReceiveDepthListener.remove(eventListener);
        }
    }

    public static synchronized void callReceiveImageEvent(final Class<?> caller, final YoonImage pImage) {
        callReceiveImageEvent(caller, pImage, true);
    }

    public static synchronized void callReceiveImageEvent(final Class<?> caller, final YoonImage pImage, boolean bDoAsync) {
        if (bDoAsync) {
            callReceiveImageEventByAsync(caller, pImage);
        } else {
            callReceiveImageEventBySync(caller, pImage);
        }
    }

    public static synchronized void callReceiveDepthEvent(final Class<?> caller, final YoonImage pImage) {
        callReceiveDepthEvent(caller, pImage, true);
    }

    public static synchronized void callReceiveDepthEvent(final Class<?> caller, final YoonImage pImage, boolean bDoAsync) {
        if (bDoAsync) {
            callReceiveDepthEventByAsync(caller, pImage);
        } else {
            callReceiveDepthEventBySync(caller, pImage);
        }
    }

    private static synchronized void callReceiveImageEventByAsync(final Class<?> caller, final YoonImage pImage) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);
        for (final IReceiveImageEventListener listener : m_pListReceiveImageListener) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (!listener.getClass().getName().equals(caller.getName())) {
                        listener.onReceiveImageEvent(pImage);
                    }
                }
            });
        }
        executorService.shutdown();
    }

    private static synchronized void callReceiveDepthEventByAsync(final Class<?> caller, final YoonImage pImage) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);
        for (final IReceiveDepthEventListener listener : m_pListReceiveDepthListener) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (!listener.getClass().getName().equals(caller.getName())) {
                        listener.onReceiveDepthEvent(pImage);
                    }
                }
            });
        }
        executorService.shutdown();
    }

    private static synchronized void callReceiveImageEventBySync(final Class<?> caller, final YoonImage pImage) {
        for (final IReceiveImageEventListener listener : m_pListReceiveImageListener) {
            if (!listener.getClass().getName().equals(caller.getName())) {
                listener.onReceiveImageEvent(pImage);
            }
        }
    }

    private static synchronized void callReceiveDepthEventBySync(final Class<?> caller, final YoonImage pImage) {
        for (final IReceiveDepthEventListener listener : m_pListReceiveDepthListener) {
            if (!listener.getClass().getName().equals(caller.getName())) {
                listener.onReceiveDepthEvent(pImage);
            }
        }
    }
}
