package org.example.model;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.Writer;
import java.io.StringWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;



//Свет давали по 2 часа, иногда, а у меня пк, поэтому так(
public class FirstTask {

    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    Document document ;

    public FirstTask() throws Exception {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
    }

    public void concatNameAndSurname(String pathToXmlFile) throws Exception {
        document = builder.parse(new File(pathToXmlFile));
        NamedNodeMap nnm = null;
        NodeList nodeList = document.getElementsByTagName("person");
        for(int i = 0;i < nodeList.getLength();i++){
            nnm = nodeList.item(i).getAttributes();
            nnm.getNamedItem("name").setNodeValue(nnm.getNamedItem("name").getNodeValue() + " " + nnm.getNamedItem("surname").getNodeValue());
            nnm.removeNamedItem("surname");
        }
        printToXmlFile(document);
    }


    private void printToXmlFile(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/result/firstTaskResult.xml"));
        writer.write(out.toString());
        writer.close();
        System.out.println(out.toString());
    }

}
