package de.nils.iplocatorapi.utils;

import java.net.IDN;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

public class DomainUtils {
    public static boolean isValidDomain(String domain) {
        try {
            String ascii = IDN.toASCII(domain);
            URI uri = new URI("http://" + ascii);
            String host = uri.getHost();

            return host != null && host.contains(".");
        } catch(Exception e) {
            return false;
        }
    }

    public static String resolveIp(String domain) throws UnknownHostException {
        String ascii = IDN.toASCII(domain);
        InetAddress addr = InetAddress.getByName(ascii);
        return addr.getHostAddress();
    }
}
