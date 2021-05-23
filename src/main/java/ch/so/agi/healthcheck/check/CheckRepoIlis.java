package ch.so.agi.healthcheck.check;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
       
        // Sehr langsam in Eclipse. Sehr schnell als Jar lokal ausgeführt mit ARM-Java. Ursachen?
        // - Rosetta (Eclipse mit JRE)?
        // - Java 15?
        // -> Mit nativen Zulu Java 11 ist es normal schnell.
                
        UserSettings settings = new UserSettings();
        ch.interlis.ili2c.Main.setDefaultIli2cPathMap(settings);
        // TODO: Proxy wären wohl sinnvoll.
//        settings.setHttpProxyHost(httpProxyHost);
//        settings.setHttpProxyPort(httpProxyPort);
        settings.setIlidirs(ch.interlis.ili2c.Main.DEFAULT_ILIDIRS);

        String repository = this.probe.getResponse().request().uri().toASCIIString();
        Configuration config = new Configuration();
        FileEntry file = new FileEntry(repository, FileEntryKind.ILIMODELFILE);
        config.addFileEntry(file);
        config.setGenerateWarnings(true);
        // TODO: für was ist das gut?
        //config.setOutputKind(GenerateOutputKind.NOOUTPUT);
        // TODO: was macht das?
        config.setAutoCompleteModelList(true);
        
        Path ilicachePath;
        try {
            ilicachePath = Files.createTempDirectory("ilicache");
        } catch (IOException e) {
            e.printStackTrace();
            this.setResult(false, e.getMessage());
            return;
        }
        
        Path logfile = Paths.get(ilicachePath.toFile().getAbsolutePath(), "output.log");
        boolean failed = false;
        // Der Cache muss immer leer sein für die Prüfung. Weil ja beliebig viele
        // solche Checks existieren und gleichzeitig laufen können, müssen wir das synchronisieren.
        synchronized(this) {
            // Um bessere Fehlermeldungen ("was ist falsch?") machen zu können, lesen wir System.err
            // aus.
            PrintStream console = System.err;
            try {
                FileOutputStream f = new FileOutputStream(logfile.toFile().getAbsolutePath());
                System.setErr(new PrintStream(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            System.setProperty("user.home", ilicachePath.toFile().getAbsolutePath().toString());
            failed = new CheckReposIlis().checkRepoIlis(config, settings);
            System.setErr(console);
        }

        if (failed) {
            List<String> list = new ArrayList<>();
            try (Stream<String> stream = Files.lines(logfile)) {
                list = stream
                .filter(s -> {
                    if (s.contains("not found") || s.contains("compile failed with files")) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setResult(false, "Check of INTERLIS models in repository "+repository+" failed.\n"+String.join(",", list));
        } else {
            this.setResult(true, "OK");
        }
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
