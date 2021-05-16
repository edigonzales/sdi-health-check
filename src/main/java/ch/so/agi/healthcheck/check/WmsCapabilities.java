package ch.so.agi.healthcheck.check;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WmsCapabilities {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    String ROOT_LAYER_NAME = "somap";
    
    String url;
    File file;
    
    public WmsCapabilities(String url) throws IOException {
        this.url = url;
        Path tempFile = Files.createTempFile("temp-", ".xml");
        file = tempFile.toFile();
        ReadableByteChannel rbc = Channels.newChannel(new URL(url).openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }
    
    // TODO: exception handling
    public List<WmsLayer> getNamedLayers() throws IOException, SAXException, ParserConfigurationException {        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        DocumentBuilder db = dbf.newDocumentBuilder();  
        Document doc = db.parse(file);  
        
        doc.getDocumentElement().normalize();  
        NodeList nodeList = doc.getElementsByTagName("Layer"); 

        Map wmsLayers = new HashMap<String,WmsLayer>();
        
        // Logik geht nicht ganz auf, da AGDI die Web GIS Client Logik
        // nicht 1:1 im GetCapabilities abbildet.
        for (int itr = 0; itr < nodeList.getLength(); itr++) {  
            Node node = nodeList.item(itr);                 
            if (node.getNodeType() == Node.ELEMENT_NODE) {  
                Element eElement = (Element) node;
                String name =  eElement.getElementsByTagName("Name").item(0).getTextContent();
                String title =  eElement.getElementsByTagName("Title").item(0).getTextContent();
                        
                if (name.equalsIgnoreCase(ROOT_LAYER_NAME)) continue;

                WmsLayer wmsLayer = new WmsLayer();
                wmsLayer.setName(name);
                wmsLayer.setTitle(title);
                                
                NodeList childLayerNodes = eElement.getElementsByTagName("Layer");

                // Falls der Knoten Kinder hat, soll der Knoten
                // nicht in der Liste erscheinen.
                if (childLayerNodes.getLength() > 0) continue;
                
                String layerGroupTitle = "";                
                
                Node parentNode = node.getParentNode();

                // TODO: Abbruchkriterium, z.B. max 10
                while (parentNode.getNodeName().equalsIgnoreCase("Layer")) {
                    Element pElement = (Element) parentNode;
                    
                    String pName = pElement.getElementsByTagName("Name").item(0).getTextContent();
                    if (!pName.equals(ROOT_LAYER_NAME)) {
                        layerGroupTitle = pName;
                        
                        wmsLayer.setParentName(pName);
                        wmsLayer.setParentTitle(pElement.getElementsByTagName("Title").item(0).getTextContent());
                    }
                    
                    parentNode = parentNode.getParentNode();
                }
                           
                // Falls es einen Layer mit identischem Namen gibt,
                // dieser jedoch auf der Root-Ebene ist, wird
                // dieser ersetzt mit dem Layer in einer Gruppe.
                if (wmsLayers.containsKey(wmsLayer.getName())) {
                    WmsLayer layer = (WmsLayer) wmsLayers.get(wmsLayer.getName());
                    if (layer.getParentName() == null && wmsLayer.getParentName() != null) {
                        wmsLayers.put(wmsLayer.getName(), wmsLayer);
                    }
                } else {
                    wmsLayers.put(wmsLayer.getName(), wmsLayer);
                }
            }
        }
        
        return new ArrayList<WmsLayer>(wmsLayers.values());
    }
}
