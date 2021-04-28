package com.yoonfactory.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class YoonProperties implements IYoonFile {
    private String m_strFilePath;
    private Properties m_pProp;

    @Override
    public String getFilePath() {
        return m_strFilePath;
    }

    public YoonProperties(String strFilePath) {
        m_strFilePath = strFilePath;
    }

    @Override
    public void copyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonProperties) {
            YoonProperties pProperties = (YoonProperties) pFile;
            m_strFilePath = pProperties.getFilePath();
        }
    }

    @Override
    public IYoonFile clone() {
        return new YoonProperties(m_strFilePath);
    }

    @Override
    public boolean isFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(m_strFilePath);
        return FileFactory.verifyFileExtension(refStrPath, "properties", false, false);
    }

    @Override
    public boolean loadFile() {
        if (!isFileExist()) return false;
        try {
            m_pProp = new Properties();
            m_pProp.load(new FileInputStream(m_strFilePath));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean saveFile() {
        try {
            m_pProp.store(new FileOutputStream(m_strFilePath), null);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getValue(String strKey) throws NullPointerException {
        if (m_pProp == null) throw new NullPointerException();
        return m_pProp.getProperty(strKey);
    }

    public boolean setValue(String strKey, String strValue) throws NullPointerException {
        if (m_pProp == null) throw new NullPointerException();
        if (m_pProp.setProperty(strKey, strValue) != null) return true;
        else return false;
    }
}