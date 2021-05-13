package ch.so.agi.healthcheck.check;

import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.probe.ProbeResult;

public class HttpStatusNoError implements Check {

    @Override
    public void perform(ProbeResult result, CheckVarsDTO checkVars) {
        log.info("do something checker");

    }

}
