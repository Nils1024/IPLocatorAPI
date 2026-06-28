package de.nils.iplocatorapi.daos;

import java.util.List;

/**
 * Autonomous System Number related data response data access object
 */
public class ASNData {
    /**
     * Autonomous System Number
     */
    private int asn;
    private String name;
    private String organization;
    private List<String> networks;
    /**
     * Regional Internet Registry
     */
    private String rir;

    public int getAsn() {
        return asn;
    }

    public void setAsn(int asn) {
        this.asn = asn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public List<String> getNetworks() {
        return networks;
    }

    public void setNetworks(List<String> networks) {
        this.networks = networks;
    }

    public String getRir() {
        return rir;
    }

    public void setRir(String rir) {
        this.rir = rir;
    }
}
