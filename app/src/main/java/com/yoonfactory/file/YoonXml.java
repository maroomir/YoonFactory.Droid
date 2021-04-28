package com.yoonfactory.file;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

public class YoonXml implements IYoonFile {
    private String m_strFilePath;
    private Document m_pDocument;

    @Override
    public String getFilePath() {
        return m_strFilePath;
    }

    public Document getDocument() {
        return m_pDocument;
    }

    public void setDocument(Document pDocument) {
        m_pDocument = pDocument;
    }

    @Override
    public void copyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonXml) {
            YoonXml pXml = (YoonXml) pFile;
            m_strFilePath = pXml.getFilePath();
        }
    }

    public YoonXml(String strFilePath) {
        m_strFilePath = strFilePath;
    }

    @Override
    public IYoonFile clone() {
        return new YoonXml(m_strFilePath);
    }

    @Override
    public boolean isFileExist() {
        AtomicReference<String> refStrPath = new AtomicReference<>(m_strFilePath);
        return FileFactory.verifyFileExtension(refStrPath, "xml", false, false);
    }

    @Override
    public boolean loadFile() {
        if (!isFileExist()) return false;
        FileInputStream pStream = null;
        try {
            //// Parsing XML to Document
            pStream = new FileInputStream(new File(m_strFilePath));
            DocumentBuilderFactory pFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder pBuilder = pFactory.newDocumentBuilder();
            m_pDocument = pBuilder.parse(pStream);
            pStream.close();
            return true;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStream != null)
                    pStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean saveFile() {
        FileOutputStream pStream = null;
        try {
            pStream = new FileOutputStream(new File(m_strFilePath), false);
            TransformerFactory pFactory = TransformerFactory.newInstance();
            Transformer pTransform = pFactory.newTransformer();
            DOMSource pSource = new DOMSource(m_pDocument);
            pTransform.transform(pSource, new StreamResult(pStream));
            pStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}