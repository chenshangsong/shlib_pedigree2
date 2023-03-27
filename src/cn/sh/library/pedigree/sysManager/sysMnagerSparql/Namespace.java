package cn.sh.library.pedigree.sysManager.sysMnagerSparql;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Namespaces known in CBA
 *
 * @author Chan Tom
 */
public enum Namespace {
    RDF("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"),
    RDFS("rdfs", "http://www.w3.org/2000/01/rdf-schema#"),
    DC("dc","http://purl.org/dc/elements/1.1/"),
    DCT("dct","http://purl.org/dc/terms/"),
    SHL("shl","http://www.library.sh.cn/ontology/"),
    OWL("owl","http://www.w3.org/2002/07/owl#"),
    XSD("xsd", "http://www.w3.org/2001/XMLSchema#"),
    FOAF("foaf", "http://xmlns.com/foaf/0.1/"),
    GOV("gov", "http://chinagoverment.org/"),
    DCAT("dcat", "http://www.w3.org/ns/dcat#"),
    SCHEMA("schema", "http://schema.org/"),
    ORG("ORG", "http://www.w3.org/ns/org#");

    private static Map<String, Namespace> typesByNames = new HashMap<String, Namespace>();

    static {
        for (Namespace type : values()) {
            typesByNames.put(type.name_, type);
        }
    }

    /**
     * Abbreviated name of the namespace
     */
    private String name_;

    /**
     * URI of the namespace
     */
    private String uri_;
    private static String ns_str = "";

    Namespace(String name, String uriString) {
        this.name_ = name;
        this.uri_ = uriString;
    }

    public String getName() {
        return name_;
    }

    public String getUri() {
        return uri_;
    }

    public static Namespace getNamespaceByName(String name) {
        return typesByNames.get(name);
    }

    //获取namespace的缩写
    public static String getNameByURI(String uriString) {
        for (Map.Entry<String, Namespace> entry : typesByNames.entrySet()) {
            String name = entry.getKey();

            if (uriString.equals(entry.getValue().getUri())) {
                return name;
            }
        }

        return null;
    }

    //获取namespace的地址
    public static String getURIByName(String name) {
        return getNamespaceByName(name).getUri().toString();
    }

    //获取所有namespace字符串，用于sparql查询
    public static String getNsPrefixString() {
        if (StringUtils.isEmpty(ns_str)) {
            for (Map.Entry<String, Namespace> entry : typesByNames.entrySet()) {
                ns_str += "PREFIX " + entry.getKey() + ":<" + entry.getValue().getUri() + ">\n";
            }
        }

        return ns_str;
    }
}