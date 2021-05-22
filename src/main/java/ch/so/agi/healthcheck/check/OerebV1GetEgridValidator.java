package ch.so.agi.healthcheck.check;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import ch.so.agi.healthcheck.model.CheckVarsDTO;

public class OerebV1GetEgridValidator extends Check {

    @Override
    public String getName() {
        return "OEREB V1 GetEgrid Validator";
    }

    @Override
    public String getDescription() {
        return "Validates a OEREB V1 GetEgrid response.";
    }

    @Override
    public void perform(CheckVarsDTO checkVars) throws IOException {
        log.info("Check: " + this.getClass().getCanonicalName());

        try {
            // TODO: Das dauert relativ lange. Unschön wenn wir viele gleiche XSD-Validierungen
            // machen. Genau diesen Validator resp. dieses Schema kann es nur einmal geben, das 
            // ändert sich nicht. Singleton?
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File("src/main/xsd/OeREB/1.0/Extract.xsd")); // dieser Schritt dauert lange
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource((InputStream)this.probe.getResponse().body()));
        } catch (SAXException e) {
            log.error(e.getMessage());
            this.setResult(false, "Response not schema compliant: " + e.getMessage());

            // FIXME
            throw new IOException(e.getMessage());
        }
        
        // Was kann man noch testen? Mit/ohne Jaxb?
        // - Länge des EGRID
        // - ...

        this.setResult(true, "OK");
    }
}
