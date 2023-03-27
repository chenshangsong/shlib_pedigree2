package cn.sh.library.pedigree.dto.sysmanager.dataDto;

import cn.sh.library.pedigree.dto.sysmanager.DataEditViewDto;

/**
 * 谱名导入Dto
 * @author chenss
 *
 */
public class TitleDto  extends DataEditViewDto{

	
	private String titleNameS;
	
	private String titleNameT;
	/**
	 * 
	 */
	private String graphUri;
	public String getTitleNameS() {
		return titleNameS;
	}
	public void setTitleNameS(String titleNameS) {
		this.titleNameS = titleNameS;
	}
	public String getTitleNameT() {
		return titleNameT;
	}
	public void setTitleNameT(String titleNameT) {
		this.titleNameT = titleNameT;
	}
	public String getGraphUri() {
		return graphUri;
	}
	public void setGraphUri(String graphUri) {
		this.graphUri = graphUri;
	}

}