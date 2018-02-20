package no.difi.move.deploymanager.util;

import lombok.extern.slf4j.Slf4j;
import no.difi.move.deploymanager.action.DeployActionException;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JpsProcessIdFinder implements ProcessIdFinder {

    @Override
    public List<String> getPids(String fileName) {
        Assert.notNull(fileName, "fileName");
        List<String> fileProcesses = new ArrayList<>();
        try {
            Process jpsProcess = Runtime.getRuntime().exec("jps.exe -l");
            IOUtils.readLines(jpsProcess.getInputStream(), Charset.defaultCharset())
                    .forEach(line ->
                    {
                        if (line.contains(fileName)) {
                            fileProcesses.add(parsePid(line));
                        }
                    });
        } catch (IOException e) {
            throw new DeployActionException(e);
        }
        if (fileProcesses.size() > 1) {
            log.warn("Multiple {} processes detected. Please verify your configuration.", fileName);
        }

        return fileProcesses;
    }

    private String parsePid(String jpsOutputLine) {
        return jpsOutputLine.split("\\s+")[0];
    }
}
