package com.yoonfactory.param;

public interface IYoonTemplate {

    int getNo();

    void setNo(int nNo);

    String getName();

    void setName(String strName);

    String getRootDirectory();

    void setRootDirectory(String strDir);

    void copyFrom(IYoonTemplate pTemplate);

    boolean saveTemplate();

    boolean loadTemplate();

    IYoonContainer getContainer();

    void setContainer(IYoonContainer pContainer);
}
