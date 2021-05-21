package ch.so.agi.healthcheck.check;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import ch.so.agi.healthcheck.model.CheckVarsDTO;

public class XmlParse extends Check {

    @Override
    public String getName() {
        return "Valid XML response";
    }

    @Override
    public String getDescription() {
        return "HTTP response contains valid XML";
    }

    @Override
    public void perform(CheckVarsDTO checkVars) throws IOException {
        log.info("Check: " + this.getClass().getCanonicalName());

        InputStream is = (InputStream) this.probe.getResponse().body();
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        DocumentBuilder db;
          
        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            this.setResult(false, e.getMessage());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            this.setResult(false, e.getMessage());
        } 

        this.setResult(true, "OK");
    }
}
