import de.nils.iplocatorapi.logging.SLF4JLoggerFinder;

module IPLocatorAPI.main {
    requires java.naming;
    requires java.net.http;
    requires java.sql;
    requires org.aspectj.weaver;
    requires org.slf4j;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;
    requires tools.jackson.databind;

    provides System.LoggerFinder with SLF4JLoggerFinder;
}