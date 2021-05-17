package ch.so.agi.healthcheck.check;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.probe.ProbeResult;

public class HasUniqueWmsLayerIdentifiers implements Check {

    @Override
    public void perform(ProbeResult result, CheckVarsDTO checkVars) {
        log.info("HasUniqueWmsLayerIdentifiers");
        
        CheckResult checkResult = new CheckResult();
        checkResult.setClassName(this.getClass().getCanonicalName());
        
        // TODO: Wird eigentlich beim Inputstream der body bereits heruntergeladen?
        // Nein, ich glaube nicht: https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpResponse.BodySubscriber.html
        // Besser wäre, wenn wir das GetCapabilities-Dokument in eine Datei speichern würden. 
        // Aber wo, damit es generisch bleibt?
        InputStream is = (InputStream) result.getResponse().body();
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        DocumentBuilder db;
        List<String> duplicateLayers =  new ArrayList<String>();
        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);  

            doc.getDocumentElement().normalize();  
            NodeList nodeList = doc.getElementsByTagName("Layer"); 

            Map<String,String> wmsLayers = new HashMap<String,String>();
            duplicateLayers = new ArrayList<String>();
            for (int itr = 0; itr < nodeList.getLength(); itr++) {  
                Node node = nodeList.item(itr);            
           
                if (node.getNodeType() == Node.ELEMENT_NODE) {  
                    Element eElement = (Element) node;
                    String name =  eElement.getElementsByTagName("Name").item(0).getTextContent();
                    String title =  eElement.getElementsByTagName("Title").item(0).getTextContent();
                    if (wmsLayers.containsKey(name)) {
                        duplicateLayers.add(name);
                    } else {
                        wmsLayers.put(name, title);
                    }
                }
            }
            
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            checkResult.setSuccess(false);
            checkResult.setMessage(e.getMessage());
        }  

        if (duplicateLayers.size() > 0) {
            checkResult.setSuccess(false);
            checkResult.setMessage("Duplicate layer(s) found: "  + String.join(", ", duplicateLayers));
        }
        
        result.addCheckResult(checkResult);
    }
}
