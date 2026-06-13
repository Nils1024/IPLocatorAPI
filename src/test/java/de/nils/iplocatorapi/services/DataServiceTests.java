package de.nils.iplocatorapi.services;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class DataServiceTests {
    @Test
    public void extractAbuseEmail_test() {
        String json = """
            {
                      "handle" : "86.54.11.0 - 86.54.11.255",
                      "startAddress" : "86.54.11.0",
                      "endAddress" : "86.54.11.255",
                      "ipVersion" : "v4",
                      "name" : "CZ-WHALEBONEDNS-20050512",
                      "type" : "ALLOCATED-ASSIGNED PA",
                      "country" : "DE",
                      "parentHandle" : "0.0.0.0 - 255.255.255.255",
                      "cidr0_cidrs" : [ {
                        "v4prefix" : "86.54.11.0",
                        "length" : 24
                      } ],
                      "status" : [ "active" ],
                      "entities" : [ {
                        "handle" : "lir-cz-whalebonedns-1-MNT",
                        "vcardArray" : [ "vcard", [ [ "version", { }, "text", "4.0" ], [ "fn", { }, "text", "lir-cz-whalebonedns-1-MNT" ], [ "kind", { }, "text", "individual" ] ] ],
                        "roles" : [ "registrant" ],
                        "links" : [ {
                          "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                          "rel" : "self",
                          "href" : "https://rdap.db.ripe.net/entity/lir-cz-whalebonedns-1-MNT"
                        }, {
                          "value" : "http://www.ripe.net/data-tools/support/documentation/terms",
                          "rel" : "copyright",
                          "href" : "http://www.ripe.net/data-tools/support/documentation/terms"
                        } ],
                        "objectClassName" : "entity"
                      }, {
                        "handle" : "ORG-WS274-RIPE",
                        "vcardArray" : [ "vcard", [ [ "version", { }, "text", "4.0" ], [ "fn", { }, "text", "Whalebone, s.r.o." ], [ "kind", { }, "text", "org" ], [ "adr", {
                          "label" : "Jezuitská 14/13\\n60200\\nBrno\\nCZECH REPUBLIC"
                        }, "text", [ "", "", "", "", "", "", "" ] ], [ "tel", {
                          "type" : "voice"
                        }, "text", "+420608737930" ] ] ],
                        "roles" : [ "registrant" ],
                        "links" : [ {
                          "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                          "rel" : "self",
                          "href" : "https://rdap.db.ripe.net/entity/ORG-WS274-RIPE"
                        }, {
                          "value" : "http://www.ripe.net/data-tools/support/documentation/terms",
                          "rel" : "copyright",
                          "href" : "http://www.ripe.net/data-tools/support/documentation/terms"
                        } ],
                        "objectClassName" : "entity"
                      }, {
                        "handle" : "RIPE-NCC-HM-MNT",
                        "vcardArray" : [ "vcard", [ [ "version", { }, "text", "4.0" ], [ "fn", { }, "text", "RIPE-NCC-HM-MNT" ], [ "kind", { }, "text", "individual" ], [ "org", { }, "text", "ORG-NCC1-RIPE" ] ] ],
                        "roles" : [ "registrant" ],
                        "links" : [ {
                          "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                          "rel" : "self",
                          "href" : "https://rdap.db.ripe.net/entity/RIPE-NCC-HM-MNT"
                        }, {
                          "value" : "http://www.ripe.net/data-tools/support/documentation/terms",
                          "rel" : "copyright",
                          "href" : "http://www.ripe.net/data-tools/support/documentation/terms"
                        } ],
                        "objectClassName" : "entity"
                      }, {
                        "handle" : "TS40827-RIPE",
                        "vcardArray" : [ "vcard", [ [ "version", { }, "text", "4.0" ], [ "fn", { }, "text", "Tech support" ], [ "kind", { }, "text", "group" ], [ "adr", {
                          "label" : "Krenová 19\\n60200\\nBrno\\nCZECH REPUBLIC"
                        }, "text", [ "", "", "", "", "", "", "" ] ], [ "tel", {
                          "type" : "voice"
                        }, "text", "+420608737930" ] ] ],
                        "roles" : [ "administrative", "technical" ],
                        "links" : [ {
                          "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                          "rel" : "self",
                          "href" : "https://rdap.db.ripe.net/entity/TS40827-RIPE"
                        }, {
                          "value" : "http://www.ripe.net/data-tools/support/documentation/terms",
                          "rel" : "copyright",
                          "href" : "http://www.ripe.net/data-tools/support/documentation/terms"
                        } ],
                        "objectClassName" : "entity"
                      }, {
                        "handle" : "AR62896-RIPE",
                        "vcardArray" : [ "vcard", [ [ "version", { }, "text", "4.0" ], [ "fn", { }, "text", "Abuse-C Role" ], [ "kind", { }, "text", "group" ], [ "adr", {
                          "label" : "Krenová 19\\n60200\\nBrno\\nCZECH REPUBLIC"
                        }, "text", [ "", "", "", "", "", "", "" ] ], [ "email", {
                          "type" : "abuse"
                        }, "text", "abuse@whalebone.io" ] ] ],
                        "roles" : [ "abuse" ],
                        "entities" : [ {
                          "handle" : "lir-cz-whalebonedns-1-MNT",
                          "vcardArray" : [ "vcard", [ [ "version", { }, "text", "4.0" ], [ "fn", { }, "text", "lir-cz-whalebonedns-1-MNT" ], [ "kind", { }, "text", "individual" ] ] ],
                          "roles" : [ "registrant" ],
                          "links" : [ {
                            "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                            "rel" : "self",
                            "href" : "https://rdap.db.ripe.net/entity/lir-cz-whalebonedns-1-MNT"
                          }, {
                            "value" : "http://www.ripe.net/data-tools/support/documentation/terms",
                            "rel" : "copyright",
                            "href" : "http://www.ripe.net/data-tools/support/documentation/terms"
                          } ],
                          "objectClassName" : "entity"
                        } ],
                        "objectClassName" : "entity"
                      } ],
                      "links" : [ {
                        "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                        "rel" : "rdap-up",
                        "href" : "https://rdap.db.ripe.net/ips/rirSearch1/rdap-up/86.54.11.0%20-%2086.54.11.255",
                        "type" : "application/rdap+json"
                      }, {
                        "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                        "rel" : "rdap-up rdap-active",
                        "href" : "https://rdap.db.ripe.net/ips/rirSearch1/rdap-up/86.54.11.0%20-%2086.54.11.255?status=active",
                        "type" : "application/rdap+json"
                      }, {
                        "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                        "rel" : "rdap-down",
                        "href" : "https://rdap.db.ripe.net/ips/rirSearch1/rdap-down/86.54.11.0%20-%2086.54.11.255",
                        "type" : "application/rdap+json"
                      }, {
                        "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                        "rel" : "rdap-top",
                        "href" : "https://rdap.db.ripe.net/ips/rirSearch1/rdap-top/86.54.11.0%20-%2086.54.11.255",
                        "type" : "application/rdap+json"
                      }, {
                        "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                        "rel" : "rdap-top rdap-active",
                        "href" : "https://rdap.db.ripe.net/ips/rirSearch1/rdap-top/86.54.11.0%20-%2086.54.11.255?status=active",
                        "type" : "application/rdap+json"
                      }, {
                        "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                        "rel" : "rdap-bottom",
                        "href" : "https://rdap.db.ripe.net/ips/rirSearch1/rdap-bottom/86.54.11.0%20-%2086.54.11.255",
                        "type" : "application/rdap+json"
                      }, {
                        "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                        "rel" : "self",
                        "href" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                        "type" : "application/rdap+json"
                      }, {
                        "value" : "http://www.ripe.net/data-tools/support/documentation/terms",
                        "rel" : "copyright",
                        "href" : "http://www.ripe.net/data-tools/support/documentation/terms"
                      } ],
                      "events" : [ {
                        "eventAction" : "registration",
                        "eventDate" : "2025-02-17T13:39:29Z"
                      }, {
                        "eventAction" : "last changed",
                        "eventDate" : "2025-06-10T22:44:59Z"
                      } ],
                      "rdapConformance" : [ "geofeed1", "rirSearch1", "ips", "cidr0", "rdap_level_0", "nro_rdap_profile_0", "redacted" ],
                      "notices" : [ {
                        "title" : "Filtered",
                        "description" : [ "This output has been filtered." ]
                      }, {
                        "title" : "Whois Inaccuracy Reporting",
                        "description" : [ "If you see inaccuracies in the results, please visit:" ],
                        "links" : [ {
                          "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                          "rel" : "inaccuracy-report",
                          "href" : "https://www.ripe.net/contact-form?topic=ripe_dbm&show_form=true",
                          "type" : "text/html"
                        } ]
                      }, {
                        "title" : "Source",
                        "description" : [ "Objects returned came from source", "RIPE" ]
                      }, {
                        "title" : "Terms and Conditions",
                        "description" : [ "This is the RIPE Database query service. The objects are in RDAP format." ],
                        "links" : [ {
                          "value" : "https://rdap.db.ripe.net/ip/86.54.11.1",
                          "rel" : "terms-of-service",
                          "href" : "http://www.ripe.net/db/support/db-terms-conditions.pdf",
                          "type" : "application/pdf"
                        } ]
                      } ],
                      "port43" : "whois.ripe.net",
                      "objectClassName" : "ip network",
                      "redacted" : [ {
                        "name" : {
                          "description" : "Personal e-mail information"
                        },
                        "reason" : {
                          "description" : "Personal data"
                        },
                        "prePath" : "$.entities[?(@.handle=='ORG-WS274-RIPE')].vcardArray[1][?(@[0]=='e-mail')]",
                        "method" : "removal"
                      }, {
                        "name" : {
                          "description" : "Personal e-mail information"
                        },
                        "reason" : {
                          "description" : "Personal data"
                        },
                        "prePath" : "$.entities[?(@.handle=='TS40827-RIPE')].vcardArray[1][?(@[0]=='e-mail')]",
                        "method" : "removal"
                      }, {
                        "name" : {
                          "description" : "Personal e-mail information"
                        },
                        "reason" : {
                          "description" : "Personal data"
                        },
                        "prePath" : "$.entities[?(@.handle=='AR62896-RIPE')].vcardArray[1][?(@[0]=='e-mail')]",
                        "method" : "removal"
                      } ]
                    }
        """;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        DataService service = new DataService(null);

        String email = service.extractAbuseEmail(root);

        assertThat(email).isNotNull();
        assertThat(email).isEqualTo("abuse@whalebone.io");
    }
}
