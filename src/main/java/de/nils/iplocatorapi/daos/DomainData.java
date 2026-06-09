package de.nils.iplocatorapi.daos;

import java.time.LocalDateTime;

public class DomainData {
    private String domain;
    private IPData ipData;
    private LocalDateTime registration_date;
    private String abuse_email;
    private String registrar;

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

    public String getAbuse_email() {
        return abuse_email;
    }

    public void setAbuse_email(String abuse_email) {
        this.abuse_email = abuse_email;
    }

    public String getRegistrar() {
        return registrar;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }
}
