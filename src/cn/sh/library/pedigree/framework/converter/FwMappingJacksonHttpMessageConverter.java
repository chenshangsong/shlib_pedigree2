package cn.sh.library.pedigree.framework.converter;

import java.io.IOException;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class FwMappingJacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {
	@Override  
    protected void writeInternal(Object o, HttpOutputMessage outputMessage)  
            throws IOException, HttpMessageNotWritableException {  
        outputMessage.getHeaders().set("Cache-Control", "no-cache");  
        super.writeInternal(o, outputMessage);  
    }  
	
	@Override  
    protected Object readInternal(Class<? extends Object> clazz,  
            HttpInputMessage inputMessage) throws IOException,  
            HttpMessageNotReadableException {
			return super.readInternal(clazz, inputMessage);  
	}
}
