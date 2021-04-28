package com.yoonfactory.file;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

public class YoonJson implements IYoonFile {
    private String m_strFilePath;
    private Type m_pType;
    private Object m_pDocument;

    @Override
    public String getFilePath() {
        return m_strFilePath;
    }


    public Type getType() {
        return m_pType;
    }

    public void setType(Type pType) {
        m_pType = pType;
    }

    public Object getDocument() {
        return m_pDocument;
    }

    public void setDocument(Object pObject) {
        m_pDocument = pObject;
    }

    public YoonJson(String strFilePath) {
        m_strFilePath = strFilePath;
    }

    @Override
    public void copyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonJson) {
            YoonJson pJson = (YoonJson) pFile;
            m_strFilePath = pJson.getFilePath();
        }
    }

    @Override
    public IYoonFile clone() {
        return new YoonJson(m_strFilePath);
    }

    @Override
    public boolean isFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(m_strFilePath);
        return FileFactory.verifyFileExtension(refStrPath, "json", false, false);
    }

    @Override
    public boolean loadFile() {
        try {
            m_pDocument = loadFile(m_pType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (m_pDocument == null) return false;
            else return true;
        }
    }

    @Override
    public boolean saveFile() {
        return saveFile(m_pDocument, m_pType);
    }

    public Object loadFile(Type pType) throws FileNotFoundException {
        if (!isFileExist()) throw new FileNotFoundException();
        try {
            Gson pGson = new Gson();
            FileReader pReader = new FileReader(m_strFilePath);
            m_pType = pType;
            return pGson.fromJson(pReader, m_pType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveFile(Object pObject, Type pType) {
        try {
            Gson pGson = new Gson();
            m_pType = pType;
            String strJsonData = pGson.toJson(pObject, m_pType);
            return FileFactory.setTextToFile(m_strFilePath, strJsonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}