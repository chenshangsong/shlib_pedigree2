package  cn.sh.library.pedigree.framework.json;

import com.fasterxml.jackson.databind.ObjectMapper;



//import org.codehaus.jackson.Version;
//import org.codehaus.jackson.map.DeserializationConfig.Feature;
//import org.codehaus.jackson.map.module.SimpleModule;

@SuppressWarnings("serial")
public class CustomObjectMapper extends ObjectMapper {

	public CustomObjectMapper() {
		super();
//		this.configure(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
//		   SimpleModule testModule = new SimpleModule("MyModule", new Version(1, 0, 0, null));
//		   testModule.addDeserializer(String.class, new TrimStringDeserializer());
//		   this._createDeserializationContext(jp, cfg)
//		   this.registerModule(testModule);
//		 this.getDeserializationConfig().a
//		 this.registerSubtypes(classes)
	}

}