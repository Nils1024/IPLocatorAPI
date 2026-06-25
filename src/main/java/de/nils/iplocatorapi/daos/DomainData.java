package de.nils.iplocatorapi.daos;

import java.util.List;
import java.util.Map;

public class DomainData {
    private String domain;
    private IPData ipData;
    private Map<String, List<String>> records;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public IPData getIpData() {
        return ipData;
    }

    public void setIpData(IPData ipData) {
        this.ipData = ipData;
    }

    public Map<String, List<String>> getRecords() {
        return records;
    }

    public void setRecords(Map<String, List<String>> records) {
        this.records = records;
    }
}
