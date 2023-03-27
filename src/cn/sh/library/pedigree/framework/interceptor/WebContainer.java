package  cn.sh.library.pedigree.framework.interceptor;

import java.util.HashMap;
import java.util.Map;

import cn.sh.library.pedigree.framework.interfaces.IfWebContainer;

public class WebContainer implements IfWebContainer {
	private Map<String, Object> containerMap = new HashMap<String, Object>();

	public WebContainer() {
	}

	public Map<String, Object> getContainerMap() {
		return containerMap;
	}

	public void setContainerMap(Map<String, Object> containerMap) {
		this.containerMap = containerMap;
	}

}
