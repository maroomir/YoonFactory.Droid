package com.yoonfactory.param;

import com.yoonfactory.file.YoonIni;

import java.nio.file.Paths;

public class YoonTemplate implements IYoonTemplate {

    private int m_no;
    private String m_strName;
    private String m_strRootDirectory;
    private IYoonContainer m_pContainer;

    public YoonTemplate() {
        m_no = 0;
        m_strName = "Default";
        m_strRootDirectory = Paths.get("", "YoonFactory").toString();
        m_pContainer = new YoonContainer();
    }

    @Override
    public int getNo() {
        return m_no;
    }

    @Override
    public void setNo(int nNo) {
        m_no = nNo;
    }

    @Override
    public String getName() {
        return m_strName;
    }

    @Override
    public void setName(String strName) {
        m_strName = strName;
    }

    @Override
    public String getRootDirectory() {
        return m_strRootDirectory;
    }

    @Override
    public void setRootDirectory(String strDir) {
        m_strRootDirectory = strDir;
    }

    @Override
    public void copyFrom(IYoonTemplate pTemplate) {
        if (pTemplate instanceof YoonTemplate) {
            YoonTemplate pTempOrigin = (YoonTemplate) pTemplate;
            m_no = pTempOrigin.m_no;
            m_strName = pTempOrigin.m_strName;
            m_strRootDirectory = pTempOrigin.m_strRootDirectory;
            m_pContainer.copyFrom(pTempOrigin.getContainer());
        }
    }

    @Override
    public IYoonTemplate clone() {
        YoonTemplate pTemplate = new YoonTemplate();
        pTemplate.m_no = m_no;
        pTemplate.m_strName = m_strName;
        pTemplate.m_strRootDirectory = m_strRootDirectory;
        pTemplate.m_pContainer.copyFrom(m_pContainer);
        return pTemplate;
    }

    @Override
    public boolean loadTemplate() {
        if (m_strRootDirectory == "" || m_pContainer == null)
            return false;
        boolean bResult = true;
        String strSection = String.format("%d_%s", m_no, m_strName);
        String strIniFilePath = Paths.get(m_strRootDirectory, "YoonTemplate.ini").toString();
        m_pContainer.setRootDirectory(Paths.get(m_strRootDirectory, strSection).toString());
        //// Load the template information for Ini files
        YoonIni pIni = new YoonIni(strIniFilePath);
        int nCount = Integer.parseInt(pIni.getValue(strSection, "Count").toString());
        for (int iParam = 0; iParam < nCount; iParam++) {
            String strKey = pIni.getValue(strSection, Integer.toString(iParam)).toString();
            if (!m_pContainer.loadValue(strKey))
                bResult = false;
        }
        return bResult;
    }

    @Override
    public IYoonContainer getContainer() {
        return m_pContainer;
    }

    @Override
    public void setContainer(IYoonContainer pContainer) {
        m_pContainer = pContainer;
    }

    @Override
    public boolean saveTemplate() {
        if (m_strRootDirectory == "" || m_pContainer == null)
            return false;

        boolean bResult = true;
        int iParam = 0;
        String strSection = String.format("%d_%s", m_no, m_strName);
        String strIniFilePath = Paths.get(m_strRootDirectory, "YoonTemplate.ini").toString();
        m_pContainer.setRootDirectory(Paths.get(m_strRootDirectory, strSection).toString());
        //// Save the Information of templates to Ini files
        YoonIni pIni = new YoonIni(strIniFilePath);
        pIni.setValue(strSection, "Count", Integer.toString(m_pContainer.size()));
        for (Object strKey : m_pContainer.keySet()) {
            pIni.setValue(strSection, Integer.toString(iParam), strKey);
            if (!m_pContainer.saveValue(strKey))
                bResult = false;
            iParam++;
        }
        return bResult;
    }
}