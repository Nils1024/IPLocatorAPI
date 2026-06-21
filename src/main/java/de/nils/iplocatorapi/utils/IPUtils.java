package de.nils.iplocatorapi.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtils {
    public static boolean isValidIp(String ip) {
        try {
            InetAddress.getByName(ip);
            return true;
        } catch(UnknownHostException e) {
            return false;
        }
    }
}
