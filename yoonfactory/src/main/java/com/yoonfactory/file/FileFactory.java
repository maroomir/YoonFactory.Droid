package com.yoonfactory.file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class FileFactory {

    public static String getCurrentDirectory(){
        return getAbsoluteDirectory("");
    }

    public static String getAbsoluteDirectory(String path){
        Path pPath = Paths.get(path);
        return pPath.toAbsolutePath().toString();
    }

    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    public static boolean verifyDirectory(String path) {
        File pDir = new File(path);
        if (!pDir.exists()) {
            if (!pDir.mkdirs())
                return false;
            return pDir.exists();
        } else return true;
    }

    public static boolean verifyFilePath(String path, Boolean bCreateFile) {
        File pFile = new File(path);
        FileOutputStream pStream = null;
        if (!verifyDirectory(pFile.getParent())) return false;
        try {
            if (!pFile.exists()) {
                if (!bCreateFile) return false;
                pStream = new FileOutputStream(pFile);
                pStream.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStream != null) pStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean verifyFileExtension(AtomicReference<String> refPath, String strExt, boolean bChangeExtension, boolean bCreateFile) {
        if (verifyFilePath(refPath.get(), bCreateFile)) {
            int nExtPos = refPath.get().lastIndexOf(".");
            String strExtPath = refPath.get().substring(nExtPos + 1);
            String strFilePathWithoutExt = refPath.get().substring(0, nExtPos);
            if (!strExtPath.equals(strExt)) {
                if (!bChangeExtension) return false;
                String strFilePath = strFilePathWithoutExt + strExtPath;
                if (!verifyFilePath(strFilePath, bCreateFile)) return false;
                refPath.set(strFilePath);
            }
            return true;
        }
        return false;
    }

    public static boolean isFileExist(String path) {
        File pFile = new File(path);
        return pFile.exists();
    }

    public static ArrayList<String> getFileNamesInDirectory(String rootPath) {
        File pRootPath = new File(rootPath);
        ArrayList<String> pListFile = new ArrayList<>();
        if (pRootPath.isDirectory()) {
            if (!verifyDirectory(rootPath)) return pListFile;
            String[] pArrayFiles = pRootPath.list();
            if (pArrayFiles.length > 0) {
                Collections.addAll(pListFile, pArrayFiles);
            }
        } else if (pRootPath.isFile()) {
            pListFile.add(rootPath);
        }
        return pListFile;
    }

    public static boolean setTextToFile(String path, String data) {
        FileOutputStream pStream = null;
        try {
            pStream = new FileOutputStream(path, false);
            pStream.write(data.getBytes(StandardCharsets.UTF_8));
            pStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStream != null) pStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean appendTextToFile(String path, String data) {
        FileOutputStream pStream = null;
        try {
            pStream = new FileOutputStream(path, true);
            pStream.write(data.getBytes(StandardCharsets.UTF_8));
            pStream.write(getLineSeparator().getBytes(StandardCharsets.UTF_8));
            pStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStream != null) pStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean deleteFilePath(String filePath) {
        File pFile = new File(filePath);
        if (pFile.isFile()) {
            try {

                if (!verifyDirectory(pFile.getParent()))
                    return false;
                if (pFile.exists()) {
                    return pFile.delete();
                } else
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void deleteExtensionFilesInDirectory(String dirPath, String strExt) {
        if (!verifyDirectory(dirPath)) {
            File pDir = new File(dirPath);
            FilenameFilter pFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase(Locale.ROOT).endsWith("." + strExt);
                }
            };
            File[] pArrayFiles = pDir.listFiles(pFilter);
            for (File pArrayFile : pArrayFiles) {
                pArrayFile.delete();
            }
        }
    }

    public static void deleteOldFilesInDirectory(String strDirectory, int nDateSpan) {
        Date pDateNow = Calendar.getInstance().getTime();
        long nMsTimeSpan = (long) nDateSpan * 24 * 60 * 60 * 1000; // 24H * 60M * 60S * 1,000ms
        deleteOldFilesInDirectory(strDirectory, pDateNow, nMsTimeSpan);
    }

    public static void deleteOldFilesInDirectory(String strDirectory, Date pDateCompare, int nDateSpan) {
        long nMsTimeSpan = (long) nDateSpan * 24 * 60 * 60 * 1000; // 24H * 60M * 60S * 1,000ms
        deleteOldFilesInDirectory(strDirectory, pDateCompare, nMsTimeSpan);
    }

    public static void deleteOldFilesInDirectory(String strDirectory, Date pDateCompare, long nTimeSpanMs) {
        File pDirPath = new File(strDirectory);
        if (!pDirPath.isDirectory()) return;
        File[] pArrayFiles = pDirPath.listFiles();
        try {
            for (File pFile : pArrayFiles) {
                if (pFile.isDirectory())
                    deleteOldFilesInDirectory(pFile.getPath(), pDateCompare, nTimeSpanMs);
                if (pFile.isFile()) {
                    Date pDateFile = new Date(pFile.lastModified());
                    if (pDateCompare.getTime() - pDateFile.getTime() >= nTimeSpanMs)
                        pFile.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllFilesInDirectory(String dirPath) {
        if (!verifyDirectory(dirPath)) {
            File pDir = new File(dirPath);
            try {
                while (pDir.exists()) {
                    File[] pArrayFiles = pDir.listFiles();
                    for (File pArrayFile : pArrayFiles) {
                        pArrayFile.delete();
                    }
                    if (pArrayFiles.length == 0 && pDir.isDirectory()) {
                        pDir.delete();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
