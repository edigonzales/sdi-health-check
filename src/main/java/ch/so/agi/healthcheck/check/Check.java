package ch.so.agi.healthcheck.check;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.so.agi.healthcheck.model.CheckVarsDTO;
import ch.so.agi.healthcheck.probe.Probe;
import ch.so.agi.healthcheck.probe.ProbeResult;

public interface Check {
    final Logger log = LoggerFactory.getLogger(Probe.class);

    public void perform(ProbeResult result, CheckVarsDTO checkVars);

}
