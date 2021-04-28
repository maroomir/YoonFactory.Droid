package com.yoonfactory.param;

import com.yoonfactory.IYoonSection;

import java.util.Map;

public interface IYoonContainer<T, V> extends IYoonSection<T,V> {

    String getRootDirectory();

    void setRootDirectory(String strDir);

    void copyFrom(IYoonContainer pContainer);

    void clear();

    boolean loadValue(T pKey);

    boolean saveValue(T pKey);
}