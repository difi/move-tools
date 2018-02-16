package no.difi.move.deploymanager.util;

import java.util.List;

public interface ProcessIdFinder {
    List<String> getPids(String fileName);
}
