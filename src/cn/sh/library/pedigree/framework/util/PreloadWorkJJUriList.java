package cn.sh.library.pedigree.framework.util;

import java.util.ArrayList;
import java.util.List;

import cn.sh.library.pedigree.framework.interfaces.IfPreloadBean;
import cn.sh.library.pedigree.utils.XMLReader;

/**
 * 上图胶卷
 * @author think
 *
 */
public class PreloadWorkJJUriList implements IfPreloadBean {

	public static List<String> Urilist = new ArrayList<String>();

	@Override
	public void loadInfo() {
		String path = FileUtil.WEB_INF_PATH + "/files/uris.xml";
		Urilist = XMLReader.getXMLList(path, "Row");
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
	public static boolean IsSTJJWork(String workUri) {
		boolean flg= Urilist.contains(workUri);
		return flg;
	}
}
