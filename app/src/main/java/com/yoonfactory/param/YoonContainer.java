package com.yoonfactory.param;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Paths;
import java.util.*;

public class YoonContainer implements IYoonContainer<String, YoonParameter> {
    private String m_strDirRoot;
    private Map<String, YoonParameter> m_pMapObject;
    private List<String> m_pListKeyOrdered;

    static Comparator<String> DefaultComparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return compare(o1, o2);
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    };

    @Override
    public String getRootDirectory() {
        return m_strDirRoot;
    }

    @Override
    public void setRootDirectory(String strDir) {
        m_strDirRoot = strDir;
    }

    @Override
    public void copyFrom(IYoonContainer pContainer) {
        if (pContainer instanceof YoonContainer) {
            YoonContainer pParamContainer = (YoonContainer) pContainer;
            m_pMapObject.clear();
            m_strDirRoot = pParamContainer.m_strDirRoot;
            m_pMapObject = new HashMap<>(pParamContainer.m_pMapObject);
            m_pListKeyOrdered = new ArrayList<>(pParamContainer.m_pListKeyOrdered);
        }
    }

    @Override
    public IYoonContainer clone() {
        YoonContainer pContainer = new YoonContainer(m_pMapObject);
        pContainer.m_strDirRoot = m_strDirRoot;
        return pContainer;
    }

    public YoonContainer() {
        m_pMapObject = new HashMap<>();
        m_pListKeyOrdered = new ArrayList<>();
        m_strDirRoot = Paths.get("", "YoonFactory").toString();
    }

    public YoonContainer(Map<String, YoonParameter> pMap) {
        m_pMapObject = new HashMap<>(pMap);
        m_pListKeyOrdered = new ArrayList<>(pMap.keySet());
        m_strDirRoot = Paths.get("", "YoonFactory").toString();
    }

    @Override
    public int size() {
        return m_pMapObject.size();
    }

    @Override
    public boolean isEmpty() {
        return m_pMapObject.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return m_pMapObject.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return m_pMapObject.containsValue(value);
    }

    @Override
    public YoonParameter get(int index) {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        if (index < 0 || index > m_pListKeyOrdered.size())
            throw new IndexOutOfBoundsException();
        return m_pMapObject.get(m_pListKeyOrdered.get(index));
    }

    @Override
    public YoonParameter get(Object key) {
        return m_pMapObject.get(key);
    }

    @Nullable
    @Override
    public YoonParameter put(String key, YoonParameter value) {
        if (m_pListKeyOrdered != null)
            m_pListKeyOrdered.add(key);
        return m_pMapObject.put(key, value);
    }

    @Override
    public YoonParameter remove(Object key) {
        if (m_pListKeyOrdered != null)
            m_pListKeyOrdered.remove(key);
        return m_pMapObject.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends YoonParameter> m) {
        if (m_pListKeyOrdered != null)
            m_pListKeyOrdered.addAll(m.keySet());
        m_pMapObject.putAll(m);
    }

    @Override
    public void clear() {
        if (m_pListKeyOrdered != null)
            m_pListKeyOrdered.clear();
        m_pMapObject.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return m_pMapObject.keySet();
    }

    @NotNull
    @Override
    public Collection<YoonParameter> values() {
        return m_pMapObject.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, YoonParameter>> entrySet() {
        return m_pMapObject.entrySet();
    }

    @Override
    public boolean loadValue(String strKey) {
        if (m_strDirRoot == "" || strKey == "")
            return false;
        YoonParameter pParam = m_pMapObject.get(strKey);
        pParam.RootDirectory = m_strDirRoot;
        if (!containsKey(strKey))
            put(strKey, new YoonParameter());
        if (pParam.loadParameter(strKey, true)) {
            m_pMapObject.replace(strKey, pParam);
            return true;
        } else return false;
    }

    @Override
    public boolean saveValue(String strKey) {
        if (m_strDirRoot == "" || strKey == "" || !m_pMapObject.containsKey(strKey))
            return false;
        YoonParameter pParam = m_pMapObject.get(strKey);
        pParam.RootDirectory = m_strDirRoot;
        return pParam.saveParameter(strKey);
    }

    @Override
    public int index(String pKey) throws NullPointerException {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        return index(pKey, 0, m_pListKeyOrdered.size());
    }

    @Override
    public int index(String pKey, int nIndex) throws NullPointerException {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        return index(pKey, nIndex, m_pListKeyOrdered.size() - nIndex);
    }

    @Override
    public int index(String pKey, int nIndex, int nCount) {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        if (nIndex < 0 || nIndex > m_pListKeyOrdered.size())
            throw new IndexOutOfBoundsException();
        if (nCount < 0)
            throw new IndexOutOfBoundsException();
        int nEnd = nIndex + nCount;
        for (int i = nIndex; i < nEnd; i++) {
            if (pKey.equals(m_pListKeyOrdered.get(i)))
                return i;
        }
        return -1;
    }

    @Override
    public int lastIndex(String pKey) {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        return lastIndex(pKey, 0, m_pListKeyOrdered.size());
    }

    @Override
    public int lastIndex(String pKey, int nIndex) {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        return lastIndex(pKey, nIndex, m_pListKeyOrdered.size() - nIndex);
    }

    @Override
    public int lastIndex(String pKey, int nIndex, int nCount) {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        if (nIndex < 0 || nIndex > m_pListKeyOrdered.size())
            throw new IndexOutOfBoundsException();
        if (nCount < 0)
            throw new IndexOutOfBoundsException();
        int nEnd = nIndex + nCount;
        for (int i = nEnd - 1; i >= nIndex; i--) {
            if (pKey.equals(m_pListKeyOrdered.get(i)))
                return i;
        }
        return -1;
    }

    @Override
    public void insert(int nIndex, String pKey, YoonParameter pValue) {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        if (nIndex < 0 || nIndex >= m_pListKeyOrdered.size())
            throw new IndexOutOfBoundsException();
        m_pMapObject.put(pKey, pValue);
        m_pListKeyOrdered.add(nIndex, pKey);
    }

    @Override
    public void insertRange(int nIndex, Map<String, YoonParameter> pCollection) {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        if (pCollection == null)
            throw new NullPointerException();
        if (nIndex < 0 || nIndex >= m_pListKeyOrdered.size())
            throw new IndexOutOfBoundsException();
        if (nIndex + pCollection.size() > m_pListKeyOrdered.size())
            throw new IndexOutOfBoundsException();
        for (Entry<String, YoonParameter> entry : pCollection.entrySet()) {
            insert(nIndex, entry.getKey(), entry.getValue());
            nIndex++;
        }
    }

    @Override
    public void remove(int nIndex) {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        if (nIndex < 0 || nIndex >= m_pListKeyOrdered.size())
            throw new IndexOutOfBoundsException();
        String key = m_pListKeyOrdered.get(nIndex);
        m_pListKeyOrdered.remove(nIndex);
        m_pMapObject.remove(key);
    }

    @Override
    public void removeRange(int nIndex, int nCount) {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        if (nIndex < 0 || nIndex > m_pListKeyOrdered.size())
            throw new IndexOutOfBoundsException();
        if (nCount < 0)
            throw new IndexOutOfBoundsException();
        if (nIndex + nCount > m_pListKeyOrdered.size())
            throw new IndexOutOfBoundsException();
        for (int i = 0; i < nCount; i++)
            remove(nIndex + i);
    }

    @Override
    public void reverse() {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        Collections.reverse(m_pListKeyOrdered);
    }

    @Override
    public void sort() {
        if (m_pListKeyOrdered == null)
            throw new NullPointerException();
        Collections.sort(m_pListKeyOrdered, DefaultComparator);
    }
}