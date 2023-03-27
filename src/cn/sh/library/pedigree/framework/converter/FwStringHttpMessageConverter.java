package  cn.sh.library.pedigree.framework.converter;

import java.io.IOException;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.StringHttpMessageConverter;

public class FwStringHttpMessageConverter extends StringHttpMessageConverter {
	@Override  
    protected void writeInternal(String o, HttpOutputMessage outputMessage)  
            throws IOException, HttpMessageNotWritableException {  
        outputMessage.getHeaders().set("Cache-Control", "no-cache");  
        super.writeInternal(o, outputMessage);  
    }  
	

	
	@Override  
    protected String readInternal(Class<? extends String> clazz,  
            HttpInputMessage inputMessage) throws IOException,  
            HttpMessageNotReadableException {
				return null;
		
	}
}
