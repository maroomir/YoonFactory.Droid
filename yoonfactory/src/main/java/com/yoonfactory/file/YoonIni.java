package com.yoonfactory.file;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class YoonIni implements IYoonFile {
    private String m_strFilePath;
    private File m_pFile = null;

    @Override
    public String getFilePath() {
        return m_strFilePath;
    }

    public YoonIni(String strFilePath) {
        m_strFilePath = strFilePath;
    }

    @Override
    public void copyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonIni) {
            YoonIni pIni = (YoonIni) pFile;
            m_strFilePath = pIni.getFilePath();
        }
    }

    @Override
    public IYoonFile clone() {
        return new YoonIni(m_strFilePath);
    }

    @Override
    public boolean isFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(m_strFilePath);
        return FileFactory.verifyFileExtension(refStrPath, "ini", false, false);
    }

    @Override
    public boolean loadFile() {
        if (!isFileExist()) return false;
        try {
            m_pFile = new File(m_strFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean saveFile() {
        return true;
    }

    public String getValue(String strSection, String strKey) throws NullPointerException {
        if (m_pFile == null) throw new NullPointerException();
        try {
            Ini pIni = new Ini(m_pFile);
            return pIni.get(strSection, strKey);
        } catch (InvalidFileFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean setValue(String strSection, String strKey, Object value) throws NullPointerException {
        if (m_pFile == null) throw new NullPointerException();
        try {
            Wini pIni = new Wini(m_pFile);
            pIni.put(strSection, strKey, value.toString());
            pIni.store();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}