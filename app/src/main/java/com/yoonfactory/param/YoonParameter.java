package com.yoonfactory.param;

import com.yoonfactory.file.YoonJson;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.nio.file.Paths;

public class YoonParameter {

    public IYoonParameter Parameter;
    public String RootDirectory;
    private Type m_pTypeParam;

    public Type getType() {
        return m_pTypeParam;
    }

    public YoonParameter() {
        Parameter = null;
        m_pTypeParam = Object.class;
        RootDirectory = Paths.get("", "YoonFactory").toString();
    }

    public YoonParameter(IYoonParameter pParam, Type pType) {
        Parameter = pParam;
        m_pTypeParam = pType;
        RootDirectory = Paths.get("", "YoonFactory").toString();
    }

    public void setParameter(IYoonParameter pParam, Type pType) {
        if (pParam != null) {
            Parameter.copyFrom(pParam);
            m_pTypeParam = pType;
        }
    }

    public boolean isEqual(YoonParameter pParam) {
        return pParam.Parameter.eqauls(Parameter) && pParam.getType() == m_pTypeParam && pParam.RootDirectory == RootDirectory;
    }

    public boolean saveParameter() {
        return saveParameter(m_pTypeParam.getTypeName());
    }

    public boolean saveParameter(String strFileName) {
        if (RootDirectory == "" || Parameter == null) return false;

        String strFilePath = Paths.get(RootDirectory, strFileName + ".json").toString();
        YoonJson pJson = new YoonJson(strFilePath);
        return pJson.saveFile(Parameter, m_pTypeParam);
    }

    public boolean loadParameter(boolean bSaveIfFalse) {
        return loadParameter(m_pTypeParam.getTypeName(), bSaveIfFalse);
    }

    public boolean loadParameter(String strFileName, boolean bSaveIfFalse) {
        if (RootDirectory == "" || Parameter == null) return false;

        String strFilePath = Paths.get(RootDirectory, strFileName + ".json").toString();
        IYoonParameter pParamBk = Parameter;
        YoonJson pJson = new YoonJson(strFilePath);
        try {
            Object pParam = pJson.loadFile(m_pTypeParam);
            Parameter = (IYoonParameter) pParam;
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bSaveIfFalse) return saveParameter();
        return false;
    }
}
