package com.yoonfactory.file;

public interface IYoonFile {
    String getFilePath();

    void copyFrom(IYoonFile pFile);

    boolean isFileExist();

    boolean loadFile();

    boolean saveFile();
}
