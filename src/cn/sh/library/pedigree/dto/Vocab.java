package cn.sh.library.pedigree.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Yi on 2014/10/27 0027.
 */
public class Vocab implements Serializable{

    private String uri;
    private String label;
    private List<Map<String, String>> attributes = new ArrayList<>();

    public Vocab(){}

    public Vocab(String uri, String label) {
        this.uri = uri;
        this.label = label;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void addAttribute(Map<String, String> attribute){
        this.attributes.add(attribute);
    }

    public List<Map<String, String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Map<String, String>> attributes) {
        this.attributes = attributes;
        Collections.sort(attributes, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                String name1 = StringUtils.trimToEmpty(o1.get("name"));
                String name2 = StringUtils.trimToEmpty(o2.get("name"));
                return name1.compareTo(name2);
            }
        });
    }
}
