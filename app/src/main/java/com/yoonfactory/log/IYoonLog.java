package com.yoonfactory.log;

public interface IYoonLog {
    String getRootDirectory();

    void setRootDirectory(String strRootDirectory);

    void write(String strMessage);
}
