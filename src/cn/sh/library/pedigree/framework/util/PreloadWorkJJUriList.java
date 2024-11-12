package cn.sh.library.pedigree.framework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.sh.library.pedigree.framework.interfaces.IfPreloadBean;
import cn.sh.library.pedigree.utils.StringUtilC;
import cn.sh.library.pedigree.utils.XMLReader;

/**
 * 上图胶卷
 * @author think
 *
 */
public class PreloadWorkJJUriList implements IfPreloadBean {

	private List<String> Urilist = new ArrayList<String>();
	private List<Map<String, String>> ConverImgList = new ArrayList<Map<String, String>>();
	private static PreloadWorkJJUriList instance;
	
	/**
	 * 单例使用
	 * 预加载数据类
	 * @author 陈铭
	 */
	public static synchronized PreloadWorkJJUriList getInstance() {
		if (null == instance){
			instance =new PreloadWorkJJUriList();
		}
		return instance;
	}
	
	@Override
	public void loadInfo() {
		String path = FileUtil.WEB_INF_PATH + "/files/uris.xml";
		String converImgPath = FileUtil.WEB_INF_PATH + "/files/converImg.txt";
		//判断PreloadChaodaiList是否被实例化
				if (instance == null) {
					getInstance();
				}
				
				instance.Urilist = XMLReader.getXMLList(path, "Row");
//				instance.ConverImgList=XMLReader.readFileToMapList(converImgPath); 临时注释 20240718
	}
	
	

	@Override
	public void reloadInfo() {
		loadInfo();
	}
	/**
	 * 是否为上图胶卷的workURI
	 * @param workUri
	 * @return
	 */
	public boolean IsSTJJWork(String workUri) {
		if (instance == null) {
			getInstance();
		}
		boolean flg= instance.Urilist.contains(workUri);
		return flg;
	}
	
	 public String getConvertImgByUri(String workUri) {
		 if (instance == null) {
				getInstance();
			}
	        for (Map<String, String> entry : instance.ConverImgList) {
	            if (StringUtilC.getString(entry.get("uri")).equals(workUri)) {
	                return StringUtilC.getString(entry.get("fileName")) ;
	            }
	        }
	        return null; // Return null if the workUri is not found
	    }
}
