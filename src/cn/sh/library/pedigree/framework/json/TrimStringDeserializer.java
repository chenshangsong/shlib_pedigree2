package  cn.sh.library.pedigree.framework.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class TrimStringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) {
        // This is where you can deserialize your value the way you want.
        // Don't know if the following expression is correct, this is just an idea.
    	if(jp.getCurrentToken().asString() == null){
    		return "";
    	}else{
    		return jp.getCurrentToken().asString().trim();
    	}
    }
}