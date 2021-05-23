package ch.so.agi.healthcheck.check;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.interlis2.validator.Validator;

import ch.ehi.basics.settings.Settings;
import ch.so.agi.healthcheck.model.CheckVarsDTO;
import net.bytebuddy.asm.Advice.This;

public class Ilivalidator extends Check {

    @Override
    public String getName() {
        return "Ilivalidator";
    }

    @Override
    public String getDescription() {
        return "Validates an INTERLIS transfer file";
    }

    @Override
    public void perform(CheckVarsDTO checkVars) throws IOException {
        Path tempPath;
        try {
            tempPath = Files.createTempDirectory("ilivalidator");
        } catch (IOException e) {
            e.printStackTrace();
            this.setResult(false, e.getMessage());
            return;
        }

        HttpResponse<InputStream> response = (HttpResponse<InputStream>) this.probe.getResponse();
        String path = response.request().uri().getPath();
        String fileName = path.substring(path.lastIndexOf('/') + 1);
        File targetFile = Paths.get(tempPath.toFile().getAbsolutePath(), fileName).toFile();
        System.out.println(targetFile.getAbsolutePath());

        Files.copy(response.body(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        Settings settings = new Settings();
        settings.setValue(Validator.SETTING_ILIDIRS, Validator.SETTING_DEFAULT_ILIDIRS);
        
        Path logfile = Paths.get(tempPath.toFile().getAbsolutePath(), "ilivalidator.log");
        settings.setValue(Validator.SETTING_LOGFILE, logfile.toFile().getAbsolutePath());

        boolean valid = Validator.runValidation(targetFile.getAbsolutePath(), settings);

        if (!valid) {
            String message = fileName + " is not valid.";
            if (logfile.toFile().length() / 1024 < 50) {
                String content = new String(Files.readAllBytes(logfile));
                message += "\n" + content;
            }            
            this.setResult(false, message);
        } else {
            this.setResult(true, "OK");
        }
    }

}
