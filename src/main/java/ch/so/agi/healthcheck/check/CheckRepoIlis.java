package ch.so.agi.healthcheck.check;

import java.util.ArrayList;
import java.util.Iterator;

import ch.interlis.ili2c.CheckReposIlis;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.config.GenerateOutputKind;
import ch.interlis.ili2c.gui.Main;
import ch.interlis.ili2c.gui.UserSettings;
import ch.so.agi.healthcheck.model.CheckVarsDTO;

public class CheckRepoIlis extends Check {

    @Override
    public void perform(CheckVarsDTO checkVars) {
        log.info("Check: " + this.getClass().getCanonicalName());
        
        int statusCode = this.probe.getResponse().statusCode();
        if (statusCode >= 400 && statusCode < 600) {            
            this.setResult(false, "HTTP Error status="+String.valueOf(statusCode));
        } else {
            this.setResult(true, null);
        }
        
        // Sehr langsam in Eclipse. Sehr schnell als Jar lokal ausgeführt mit ARM-Java. Ursachen?
        // - Rosetta (Eclipse mit JRE)?
        // - Java 15?
        // - ...
        
        
        // TODO: nicht Main verwenden. Nur für Default-Values o.ä.
        
//        UserSettings settings = new UserSettings();
//        // TODO: Proxy wären wohl sinnvoll.
////        setDefaultIli2cPathMap(settings);
//        ch.interlis.ili2c.Main.setDefaultIli2cPathMap(settings);
////        settings.setHttpProxyHost(httpProxyHost);
////        settings.setHttpProxyPort(httpProxyPort);
//        settings.setIlidirs("%ILI_DIR;http://models.interlis.ch/;%JAR_DIR");
//        
//        ArrayList<String> ilifilev = new ArrayList<>();
//        ilifilev.add("https://geo.so.ch/models");
//        Configuration config = new Configuration();
//        Iterator ilifilei = ilifilev.iterator();
//        while (ilifilei.hasNext()) {
//            String ilifile = (String) ilifilei.next();
//            FileEntry file = new FileEntry(ilifile, FileEntryKind.ILIMODELFILE);
//            config.addFileEntry(file);
//        }
//        config.setGenerateWarnings(true);
//        //config.setOutputKind(GenerateOutputKind.NOOUTPUT);
//
//        // ??
//        config.setAutoCompleteModelList(true);
//        
//        
//        boolean failed = new CheckReposIlis().checkRepoIlis(config, settings);

        //String[] args = {"--trace", "--check-repo-ilis", "https://geo.so.ch/models"};
        //ch.interlis.ili2c.Main.main(args);
        
    }

    @Override
    public String getName() {
        return "Check Repo Ilis";
    }

    @Override
    public String getDescription() {
        return "Check all INTERLIS files in the given repository.";
    }
}
