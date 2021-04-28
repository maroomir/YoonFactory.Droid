package com.yoonfactory.log;

import com.yoonfactory.file.FileFactory;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YoonConsoler implements IYoonLog {
    private final int MAX_SPAN_DAYS = 30;
    private String m_strRootDirectory = Paths.get("", "YoonFactory", "CLM").toString();
    private final int m_nFileExistDays;

    @Override
    public String getRootDirectory() {
        return m_strRootDirectory;
    }

    @Override
    public void setRootDirectory(String strRootDirectory) {
        this.m_strRootDirectory = strRootDirectory;
    }

    public YoonConsoler() {
        m_nFileExistDays = 1;
    }

    public YoonConsoler(int nDays) {
        if (nDays > MAX_SPAN_DAYS)
            m_nFileExistDays = MAX_SPAN_DAYS;
        else if (nDays < 0)
            m_nFileExistDays = 0;
        else
            m_nFileExistDays = nDays;
    }

    public YoonConsoler(String strDirectory, int nDays) {
        m_strRootDirectory = strDirectory;
        if (nDays > MAX_SPAN_DAYS)
            m_nFileExistDays = MAX_SPAN_DAYS;
        else if (nDays < 0)
            m_nFileExistDays = 0;
        else
            m_nFileExistDays = nDays;
    }

    @Override
    public void write(String strMessage) {
        write(strMessage, true);
    }

    public void write(String strMessage, boolean bSave) {
        SimpleDateFormat pDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strMessageLine = "[" + pDateFormat.format(Calendar.getInstance().getTime()) + "]" + strMessage;
        System.out.println(strMessageLine);
        if (FileFactory.verifyDirectory(m_strRootDirectory) && bSave) {
            writeConsoleLog(strMessageLine);
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

    private void writeConsoleLog(String strMessage) {
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