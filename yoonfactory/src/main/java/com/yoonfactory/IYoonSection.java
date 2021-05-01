package com.yoonfactory;

import java.security.InvalidKeyException;
import java.util.Collection;
import java.util.Map;

public interface IYoonSection<T,V> extends Map<T, V> {
    V get(int index);

    int index(T pKey);

    int index(T pKey, int nIndex);

    int index(T pKey, int nIndex, int nCount);

    int lastIndex(T pKey);

    int lastIndex(T pKey, int nIndex);

    int lastIndex(T pKey, int nIndex, int nCount);

    void insert(int nIndex, T pKey, V pValue);

    void insertRange(int nIndex, Map<T, V> pCollection);

    void remove(int nIndex);

    void removeRange(int nIndex, int nCount);

    void reverse();

    void sort();
}
