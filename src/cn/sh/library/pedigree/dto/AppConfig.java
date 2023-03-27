package cn.sh.library.pedigree.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Yi on 2014/11/5 0005.
 */

@Component
public class AppConfig {

    @Value("#{cfg['ns_prefixs']}")
    private String nsPrefixs;

    public String[] getNsPrefixs(){
        return nsPrefixs.split(",");
    }

    @Value("#{cfg['person_same_as']}")
    private String person_same_as;

    public String[] getPersonSameAs() {
        return person_same_as.split(",");
    }
}
