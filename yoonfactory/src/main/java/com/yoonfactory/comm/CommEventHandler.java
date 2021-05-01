package com.yoonfactory.comm;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.yoonfactory.eYoonStatus;

public class CommEventHandler {

    private static final int MAX_THREAD_POOL = 5;

    private static final List<IShowMessageEventListener> m_pListShowMessageListener = new CopyOnWriteArrayList<IShowMessageEventListener>();
    private static final List<IReceiveMessageEventListener> m_pListReceiveMessageListener = new CopyOnWriteArrayList<IReceiveMessageEventListener>();
    private static final List<IRetryOpenEventListener> m_pListRetryOpenListener = new CopyOnWriteArrayList<IRetryOpenEventListener>();

    private static synchronized List<IShowMessageEventListener> getShowMessageListenerList() {
        return m_pListShowMessageListener;
    }

    private static synchronized List<IReceiveMessageEventListener> getReceiveMessageListenerList() {
        return m_pListReceiveMessageListener;
    }

    public static List<IRetryOpenEventListener> getRetryOpenListenerList() {
        return m_pListRetryOpenListener;
    }

    public static synchronized void addShowMessageListener(IShowMessageEventListener eventListener) {
        if (!getShowMessageListenerList().contains(eventListener)) {
            m_pListShowMessageListener.add(eventListener);
        }
    }

    public static synchronized void addReceiveMessageListener(IReceiveMessageEventListener eventListener){
        if(!getReceiveMessageListenerList().contains(eventListener)){
            m_pListReceiveMessageListener.add(eventListener);
        }
    }

    static synchronized void addRetryOpenListener(IRetryOpenEventListener eventListener) {
        if (!getRetryOpenListenerList().contains(eventListener)) {
            m_pListRetryOpenListener.add(eventListener);
        }
    }

    public static synchronized void removeShowMessageListener(IShowMessageEventListener eventListener) {
        if (getShowMessageListenerList().contains(eventListener)) {
            m_pListShowMessageListener.remove(eventListener);
        }
    }

    public static synchronized void removeReceiveMessageListener(IReceiveMessageEventListener eventListener) {
        if (getReceiveMessageListenerList().contains(eventListener)) {
            m_pListReceiveMessageListener.remove(eventListener);
        }
    }

    static synchronized void removeRetryOpenListener(IRetryOpenEventListener eventListener) {
        if (getRetryOpenListenerList().contains(eventListener)) {
            m_pListRetryOpenListener.remove(eventListener);
        }
    }

    public static synchronized void callShowMessageEvent(final Class<?> caller, final eYoonStatus nStatus, final String strMessage) {
        callShowMessageEvent(caller, nStatus, strMessage, true);
    }

    public static synchronized void callShowMessageEvent(final Class<?> caller, final eYoonStatus nStatus, final String strMessage, boolean bDoAsync) {
        if (bDoAsync) {
            callShowMessageEventByAsync(caller, nStatus, strMessage);
        } else {
            callShowMessageEventBySync(caller, nStatus, strMessage);
        }
    }

    public static synchronized void callReceiveMessageEvent(final Class<?> caller, final String strReceiveMessage) {
        callReceiveMessageEvent(caller, strReceiveMessage, true);
    }

    public static synchronized void callReceiveMessageEvent(final Class<?> caller, final String strReceiveMessage, boolean bDoAsync) {
        if (bDoAsync) {
            callReceiveMessageEventByAsync(caller, strReceiveMessage);
        } else {
            callReceiveMessageEventBySync(caller, strReceiveMessage);
        }
    }

    static synchronized void callRetryOpenEvent(final Class<?> caller){
        callRetryOpenEvent(caller, false);
    }

    static synchronized void callRetryOpenEvent(final Class<?> caller, boolean bDoAsync){
        if(bDoAsync) {
            callRetryOpenEventByAsync(caller);
        }else{
            callRetryOpenEventBySync(caller);
        }
    }

    private static synchronized void callShowMessageEventByAsync(final Class<?> caller, final eYoonStatus nStatus, final String strMessage) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);
        for (final IShowMessageEventListener listener : m_pListShowMessageListener) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (!listener.getClass().getName().equals(caller.getName())) {
                        listener.onShowMessageEvent(nStatus, strMessage);
                    }
                }
            });
        }
        executorService.shutdown();
    }

    private static synchronized void callReceiveMessageEventByAsync(final Class<?> caller, final String strReceiveMessage) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);
        for (final IReceiveMessageEventListener listener : m_pListReceiveMessageListener) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (!listener.getClass().getName().equals(caller.getName())) {
                        listener.onReceiveMessageEvent(strReceiveMessage);
                    }
                }
            });
        }
        executorService.shutdown();
    }

    private static synchronized void callRetryOpenEventByAsync(final Class<?> caller) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);
        for (final IRetryOpenEventListener listener : m_pListRetryOpenListener) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (!listener.getClass().getName().equals(caller.getName())) {
                        listener.onRetryOpenEvent();
                    }
                }
            });
        }
        executorService.shutdown();
    }

    private static synchronized void callShowMessageEventBySync(final Class<?> caller, final eYoonStatus nStatus, final String strMessage) {
        for (final IShowMessageEventListener listener : m_pListShowMessageListener) {
            if (!listener.getClass().getName().equals(caller.getName())) {
                listener.onShowMessageEvent(nStatus, strMessage);
            }
        }
    }

    private static synchronized void callReceiveMessageEventBySync(final Class<?> caller, final String strReceiveMessage) {
        for (final IReceiveMessageEventListener listener : m_pListReceiveMessageListener) {
            if (!listener.getClass().getName().equals(caller.getName())) {
                listener.onReceiveMessageEvent(strReceiveMessage);
            }
        }
    }

    private static synchronized void callRetryOpenEventBySync(final Class<?> caller) {
        for (final IRetryOpenEventListener listener : m_pListRetryOpenListener) {
            if (!listener.getClass().getName().equals(caller.getName())) {
                listener.onRetryOpenEvent();
            }
        }
    }
}