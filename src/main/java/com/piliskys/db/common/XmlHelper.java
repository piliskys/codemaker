package com.piliskys.db.common;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.net.URL;
import java.util.Iterator;

/**
 * ========================================================
 *
 * @author : <a href="piliskys@163.com">liuquanbing</a>
 * @version 1.0
 *          Date: 2007-12-17
 *          Time: 14:56:15
 *          功能说明： xml解析帮助类
 *          ========================================================
 */
public class XmlHelper {
    public static void main(String[] fileName) throws Exception {

        try {
            XmlHelper aa = new XmlHelper();
            URL urll = Thread.currentThread().getContextClassLoader().getResource("./db.config.xml");
            aa.openXML(urll.getPath());
            ;
            Element root = aa.document.getRootElement();
            Element foo;
            for (Iterator i = root.elementIterator("db-config"); i.hasNext();) {
                foo = (Element) i.next();

                String af = foo.attribute("id").getStringValue();


                System.out.println("===2=" + af);
                Element foof;
                for (Iterator it = foo.elementIterator(); it.hasNext();) {

                    foof = (Element) it.next();
                    System.out.println("=== foof.getName()=" + foof.getName()
                            + "========" + foof.getText());
                }

            }

        } catch (Exception ex) {
            throw new Exception
                    ("无法读取指定的配置文件:" + fileName);
        }
    }

    /**
     * XML文档
     */
    private Document document = null;

    public Document getDocument() {
        return document;
    }

    public XmlHelper() {
    }

    /**
     * 打开文档
     */
    public void openXML(String XMLPath) {
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(XMLPath);
            System.out.println("DOM4J  openXML() successful ..."+XMLPath);
        } catch (Exception e) {
            System.out.println("DOM4J  openXML() Exception:" + e.getMessage());
        }
    }
    public  static URL gerPathUrl(String path){
    	 URL urll = Thread.currentThread().getContextClassLoader().getResource(path);
    //	System.out.println("配制文件路径获取："+urll.getPath());
    	 return   urll;
    }

    /**
     * 获得某个节点的值
     *
     * @param nodeName 节点名称
     */
    public String getElementValue(String nodeName) {
        try {
            Node node = document.selectSingleNode("//" + nodeName);

            return node.getText();
        } catch (Exception e1) {
            System.out.println("getElementValue() Exception：" + e1.getMessage());
            return null;
        }
    }

    /**
     * 获得某个节点的子节点的值
     *
     * @param nodeName
     * @param childNodeName
     * @return
     */
    public String getElementValue(String nodeName, String childNodeName) {
        try {
            Node node = document.selectSingleNode("//" + nodeName + "/" + childNodeName);
            return node.getText();
        } catch (Exception e1) {
            System.out.println("getElementValue() Exception：" + e1.getMessage());
            return null;
        }
    }
}


