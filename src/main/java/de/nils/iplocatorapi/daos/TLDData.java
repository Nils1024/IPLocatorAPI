package de.nils.iplocatorapi.daos;

import java.util.List;

/**
 * Top Level Domain related data response data access object
 */
public class TLDData {
    /**
     * Top Level Domain
     */
    private String tld;
    private String type;
    private String registry;
    private String countryCode;
    private String country;
    private String whoisServer;
    private String rdapServerUrl;
    private String ianaId;
    private String createdDate;
    private String updatedDate;
    private String status;
    private String description;
    private List<String> nameservers;
    private List<String> dnssecKeys;
    private boolean isDnssecSigned;
    private String punycode;

    public String getTld() {
        return tld;
    }

    public void setTld(String tld) {
        this.tld = tld;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWhoisServer() {
        return whoisServer;
    }

    public void setWhoisServer(String whoisServer) {
        this.whoisServer = whoisServer;
    }

    public String getRdapServerUrl() {
        return rdapServerUrl;
    }

    public void setRdapServerUrl(String rdapServerUrl) {
        this.rdapServerUrl = rdapServerUrl;
    }

    public String getIanaId() {
        return ianaId;
    }

    public void setIanaId(String ianaId) {
        this.ianaId = ianaId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getNameservers() {
        return nameservers;
    }

    public void setNameservers(List<String> nameservers) {
        this.nameservers = nameservers;
    }

    public List<String> getDnssecKeys() {
        return dnssecKeys;
    }

    public void setDnssecKeys(List<String> dnssecKeys) {
        this.dnssecKeys = dnssecKeys;
    }

    public boolean isDnssecSigned() {
        return isDnssecSigned;
    }

    public void setDnssecSigned(boolean dnssecSigned) {
        isDnssecSigned = dnssecSigned;
    }

    public String getPunycode() {
        return punycode;
    }

    public void setPunycode(String punycode) {
        this.punycode = punycode;
    }
}
