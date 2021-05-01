package com.yoonfactory.log;

import com.yoonfactory.eYoonStatus;
import com.yoonfactory.file.FileFactory;

import android.graphics.Color;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.yoonfactory.eYoonStatus.Info;

public class YoonDisplayer implements IYoonLog {
    private final int MAX_SPAN_DAYS = 30;
    private String m_strRootDirectory = Paths.get("", "YoonFactory", "DLM").toString();
    private final int m_nFileExistDays;

    @Override
    public String getRootDirectory() {
        return m_strRootDirectory;
    }

    @Override
    public void setRootDirectory(String strRootDirectory) {
        m_strRootDirectory = strRootDirectory;
    }

    public YoonDisplayer() {
        m_nFileExistDays = 1;
    }

    public YoonDisplayer(int nDays) {
        if (nDays > MAX_SPAN_DAYS)
            m_nFileExistDays = MAX_SPAN_DAYS;
        else m_nFileExistDays = Math.max(nDays, 0);
    }

    public YoonDisplayer(String strDirectory, int nDays) {
        m_strRootDirectory = strDirectory;
        if (nDays > MAX_SPAN_DAYS)
            m_nFileExistDays = MAX_SPAN_DAYS;
        else m_nFileExistDays = Math.max(nDays, 0);
    }

    @Override
    public void write(String strMessage) {
        write(Info, strMessage, true);
    }

    public void write(eYoonStatus nStatus, String strMessage, boolean bSave) {
        SimpleDateFormat pDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strMessageLine = "[" + pDateFormat.format(Calendar.getInstance().getTime()) + "]";
        switch (nStatus) {
            case Normal:
                strMessageLine += strMessage;
                LogEventHandler.callProcessLogEvent(YoonDisplayer.class, strMessageLine, Color.valueOf(Color.WHITE));
                break;
            case Conform:
                strMessageLine += ("CONFIRM : " + strMessage);
                LogEventHandler.callProcessLogEvent(YoonDisplayer.class, strMessageLine, Color.valueOf(Color.YELLOW));
                break;
            case Send:
                strMessageLine += ("SEND : " + strMessage);
                LogEventHandler.callProcessLogEvent(YoonDisplayer.class, strMessageLine, Color.valueOf(Color.GREEN));
                break;
            case Receive:
                strMessageLine += ("RECEIVE : " + strMessage);
                LogEventHandler.callProcessLogEvent(YoonDisplayer.class, strMessageLine, Color.valueOf(Color.GREEN));
                break;
            case User:
                strMessageLine += ("USER : " + strMessage);
                LogEventHandler.callProcessLogEvent(YoonDisplayer.class, strMessageLine, Color.valueOf(Color.WHITE));
                break;
            case Inspect:
                strMessageLine += ("INSPECT : " + strMessage);
                LogEventHandler.callProcessLogEvent(YoonDisplayer.class, strMessageLine, Color.valueOf(Color.GREEN));
                break;
            case Error:
                strMessageLine += ("ERROR : " + strMessage);
                LogEventHandler.callProcessLogEvent(YoonDisplayer.class, strMessageLine, Color.valueOf(Color.RED));
                break;
            case Info:
                strMessageLine += ("INFO : " + strMessage);
                LogEventHandler.callProcessLogEvent(YoonDisplayer.class, strMessageLine, Color.valueOf(Color.GRAY));
                break;
        }
        if (FileFactory.verifyDirectory(m_strRootDirectory) && bSave) {
            writeDisplayLog(strMessageLine);
        }
    }

    private String getCurrentMonthDirectory() {
        Calendar pCalCurrent = Calendar.getInstance();
        return Paths.get(m_strRootDirectory, Integer.toString(pCalCurrent.get(Calendar.YEAR)),
                Integer.toString(pCalCurrent.get(Calendar.MONTH))).toString();
    }

    private String getPreMonthDirectory() {
        Calendar pCal = Calendar.getInstance();
        pCal.add(Calendar.MONTH, -1);
        return Paths.get(m_strRootDirectory, Integer.toString(pCal.get(Calendar.YEAR)),
                Integer.toString(pCal.get(Calendar.MONTH))).toString();
    }

    private void writeDisplayLog(String strMessage) {
        //// Erase the old files
        if (m_nFileExistDays > 0) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    FileFactory.deleteOldFilesInDirectory(getPreMonthDirectory(), m_nFileExistDays);
                    FileFactory.deleteOldFilesInDirectory(getCurrentMonthDirectory(), m_nFileExistDays);
                }
            });
            executorService.shutdown();
        }
        //// Create the new log files
        Calendar pCalCurrent = Calendar.getInstance();
        String strFilePath = Paths.get(getCurrentMonthDirectory(), Integer.toString(pCalCurrent.get(Calendar.DAY_OF_MONTH)),
                String.format("%d.txt", Calendar.getInstance().get(Calendar.HOUR_OF_DAY))).toString();
        if (FileFactory.verifyFilePath(strFilePath, true))
            FileFactory.appendTextToFile(strFilePath, strMessage);
    }
}
