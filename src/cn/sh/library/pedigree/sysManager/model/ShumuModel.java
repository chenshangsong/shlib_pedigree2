package cn.sh.library.pedigree.sysManager.model;

import java.io.Serializable;
import java.util.List;

public class ShumuModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String identifier;
	private String judi;
	private String zhengshuming;
	private String zherenzheshidai;
	private String zherenzhe;
	private String zherenfangshi;
	private String qitazherenzheshidai;
	private String qitazherenzhe;
	private String qitazherenzhezherenfangshi;
	private String tanghao;
	private String juankezhe;
	private String banbenleixing;
	private String shuliang;
	private String zhuangding;
	private String fuzhu;
	private String yishuminglaiyuan;
	private String yishuming;
	private String qianxixinxi;
	private String shizhu;
	private String shizhushidai;
	private String shizhuyuanjudi;
	private String shizhuqianjudi;
	private String shiqianzhu;
	private String shiqianzhushidai;
	private String shiqianzhuyuanjudi;
	private String shiqianzhuqianjudi;
	private String zhizhufangzhushidai;
	private String zhizhufangzhu;
	private String sanjudi;
	private String mingrenshidai;
	private String mingren;
	private String puzhaineirong;
	private String shouchangzhe;
	private String others;
	private String selfUri;
	private String instanceUri;
	private String itemUri;
	private String banbenleixingUri;
	private String zhuangdingUri;
	private String banbenniandai;
	private String banbenniandaiYear;
	private String description;
	private String jigoujianchengT;
	private String jigoujianchengS;
	//doi信息
	private String workUri;
	private String callno;
	private String doi;
	private String jdS;
	private String jdT;
	/**
	 * 谱名URI
	 */
	private String pumingUri;
	/**
	 * 书目uri
	 */
	private String shumuUri;
	/*
	 * 谱名List
	 */
	private List<FamilyNameModel> pumingList;
	/**
	 * 其他数据List
	 */
	private List<ShumuModel> othersList;
	/**
	 * 外链标识符
	 */
	private String refIdentifier;
	// ////////////////////////////////////uri信息
	/**
	 * 居地URI
	 */
	private String judiUri;
	/**
	 * 责任者时代，对应纂修者URI
	 */
	private String zxzUri;
	/**
	 * 责任方式，对应URI
	 */
	private String refUri;
	
	/**
	 * 外链朝代URI
	 */
	private String refChaodaiUri;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getJudi() {
		return judi;
	}

	public void setJudi(String judi) {
		this.judi = judi;
	}

	public String getZhengshuming() {
		return zhengshuming;
	}

	public void setZhengshuming(String zhengshuming) {
		this.zhengshuming = zhengshuming;
	}

	public String getZherenzheshidai() {
		return zherenzheshidai;
	}

	public void setZherenzheshidai(String zherenzheshidai) {
		this.zherenzheshidai = zherenzheshidai;
	}

	public String getZherenzhe() {
		return zherenzhe;
	}

	public void setZherenzhe(String zherenzhe) {
		this.zherenzhe = zherenzhe;
	}

	public String getZherenfangshi() {
		return zherenfangshi;
	}

	public void setZherenfangshi(String zherenfangshi) {
		this.zherenfangshi = zherenfangshi;
	}

	public String getQitazherenzheshidai() {
		return qitazherenzheshidai;
	}

	public void setQitazherenzheshidai(String qitazherenzheshidai) {
		this.qitazherenzheshidai = qitazherenzheshidai;
	}

	public String getQitazherenzhe() {
		return qitazherenzhe;
	}

	public void setQitazherenzhe(String qitazherenzhe) {
		this.qitazherenzhe = qitazherenzhe;
	}

	public String getQitazherenzhezherenfangshi() {
		return qitazherenzhezherenfangshi;
	}

	public void setQitazherenzhezherenfangshi(String qitazherenzhezherenfangshi) {
		this.qitazherenzhezherenfangshi = qitazherenzhezherenfangshi;
	}

	public String getTanghao() {
		return tanghao;
	}

	public void setTanghao(String tanghao) {
		this.tanghao = tanghao;
	}

	public String getJuankezhe() {
		return juankezhe;
	}

	public void setJuankezhe(String juankezhe) {
		this.juankezhe = juankezhe;
	}

	public String getBanbenleixing() {
		return banbenleixing;
	}

	public void setBanbenleixing(String banbenleixing) {
		this.banbenleixing = banbenleixing;
	}

	public String getShuliang() {
		return shuliang;
	}

	public void setShuliang(String shuliang) {
		this.shuliang = shuliang;
	}

	public String getZhuangding() {
		return zhuangding;
	}

	public void setZhuangding(String zhuangding) {
		this.zhuangding = zhuangding;
	}

	public String getFuzhu() {
		return fuzhu;
	}

	public void setFuzhu(String fuzhu) {
		this.fuzhu = fuzhu;
	}

	public String getYishuminglaiyuan() {
		return yishuminglaiyuan;
	}

	public void setYishuminglaiyuan(String yishuminglaiyuan) {
		this.yishuminglaiyuan = yishuminglaiyuan;
	}

	public String getYishuming() {
		return yishuming;
	}

	public void setYishuming(String yishuming) {
		this.yishuming = yishuming;
	}

	public String getQianxixinxi() {
		return qianxixinxi;
	}

	public void setQianxixinxi(String qianxixinxi) {
		this.qianxixinxi = qianxixinxi;
	}

	public String getShizhu() {
		return shizhu;
	}

	public void setShizhu(String shizhu) {
		this.shizhu = shizhu;
	}

	public String getShizhushidai() {
		return shizhushidai;
	}

	public void setShizhushidai(String shizhushidai) {
		this.shizhushidai = shizhushidai;
	}

	public String getShizhuyuanjudi() {
		return shizhuyuanjudi;
	}

	public void setShizhuyuanjudi(String shizhuyuanjudi) {
		this.shizhuyuanjudi = shizhuyuanjudi;
	}

	public String getShizhuqianjudi() {
		return shizhuqianjudi;
	}

	public void setShizhuqianjudi(String shizhuqianjudi) {
		this.shizhuqianjudi = shizhuqianjudi;
	}

	public String getShiqianzhu() {
		return shiqianzhu;
	}

	public void setShiqianzhu(String shiqianzhu) {
		this.shiqianzhu = shiqianzhu;
	}

	public String getShiqianzhushidai() {
		return shiqianzhushidai;
	}

	public void setShiqianzhushidai(String shiqianzhushidai) {
		this.shiqianzhushidai = shiqianzhushidai;
	}

	public String getShiqianzhuyuanjudi() {
		return shiqianzhuyuanjudi;
	}

	public void setShiqianzhuyuanjudi(String shiqianzhuyuanjudi) {
		this.shiqianzhuyuanjudi = shiqianzhuyuanjudi;
	}

	public String getShiqianzhuqianjudi() {
		return shiqianzhuqianjudi;
	}

	public void setShiqianzhuqianjudi(String shiqianzhuqianjudi) {
		this.shiqianzhuqianjudi = shiqianzhuqianjudi;
	}

	public String getZhizhufangzhushidai() {
		return zhizhufangzhushidai;
	}

	public void setZhizhufangzhushidai(String zhizhufangzhushidai) {
		this.zhizhufangzhushidai = zhizhufangzhushidai;
	}

	public String getZhizhufangzhu() {
		return zhizhufangzhu;
	}

	public void setZhizhufangzhu(String zhizhufangzhu) {
		this.zhizhufangzhu = zhizhufangzhu;
	}

	public String getSanjudi() {
		return sanjudi;
	}

	public void setSanjudi(String sanjudi) {
		this.sanjudi = sanjudi;
	}

	public String getMingrenshidai() {
		return mingrenshidai;
	}

	public void setMingrenshidai(String mingrenshidai) {
		this.mingrenshidai = mingrenshidai;
	}

	public String getMingren() {
		return mingren;
	}

	public void setMingren(String mingren) {
		this.mingren = mingren;
	}

	public String getPuzhaineirong() {
		return puzhaineirong;
	}

	public void setPuzhaineirong(String puzhaineirong) {
		this.puzhaineirong = puzhaineirong;
	}

	public String getShouchangzhe() {
		return shouchangzhe;
	}

	public void setShouchangzhe(String shouchangzhe) {
		this.shouchangzhe = shouchangzhe;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getSelfUri() {
		return selfUri;
	}

	public void setSelfUri(String selfUri) {
		this.selfUri = selfUri;
	}

	public String getJudiUri() {
		return judiUri;
	}

	public void setJudiUri(String judiUri) {
		this.judiUri = judiUri;
	}

	public String getRefIdentifier() {
		return refIdentifier;
	}

	public void setRefIdentifier(String refIdentifier) {
		this.refIdentifier = refIdentifier;
	}

	public List<FamilyNameModel> getPumingList() {
		return pumingList;
	}

	public void setPumingList(List<FamilyNameModel> pumingList) {
		this.pumingList = pumingList;
	}

	public String getPumingUri() {
		return pumingUri;
	}

	public void setPumingUri(String pumingUri) {
		this.pumingUri = pumingUri;
	}

	public String getShumuUri() {
		return shumuUri;
	}

	public void setShumuUri(String shumuUri) {
		this.shumuUri = shumuUri;
	}

	public String getZxzUri() {
		return zxzUri;
	}

	public void setZxzUri(String zxzUri) {
		this.zxzUri = zxzUri;
	}

	public List<ShumuModel> getOthersList() {
		return othersList;
	}

	public void setOthersList(List<ShumuModel> othersList) {
		this.othersList = othersList;
	}

	public String getRefUri() {
		return refUri;
	}

	public void setRefUri(String refUri) {
		this.refUri = refUri;
	}

	public String getItemUri() {
		return itemUri;
	}

	public void setItemUri(String itemUri) {
		this.itemUri = itemUri;
	}

	public String getInstanceUri() {
		return instanceUri;
	}

	public void setInstanceUri(String instanceUri) {
		this.instanceUri = instanceUri;
	}

	public String getBanbenleixingUri() {
		return banbenleixingUri;
	}

	public void setBanbenleixingUri(String banbenleixingUri) {
		this.banbenleixingUri = banbenleixingUri;
	}

	public String getBanbenniandai() {
		return banbenniandai;
	}

	public void setBanbenniandai(String banbenniandai) {
		this.banbenniandai = banbenniandai;
	}

	public String getZhuangdingUri() {
		return zhuangdingUri;
	}

	public void setZhuangdingUri(String zhuangdingUri) {
		this.zhuangdingUri = zhuangdingUri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJigoujianchengT() {
		return jigoujianchengT;
	}

	public void setJigoujianchengT(String jigoujianchengT) {
		this.jigoujianchengT = jigoujianchengT;
	}

	public String getRefChaodaiUri() {
		return refChaodaiUri;
	}

	public void setRefChaodaiUri(String refChaodaiUri) {
		this.refChaodaiUri = refChaodaiUri;
	}

	public String getJigoujianchengS() {
		return jigoujianchengS;
	}

	public void setJigoujianchengS(String jigoujianchengS) {
		this.jigoujianchengS = jigoujianchengS;
	}

	public String getBanbenniandaiYear() {
		return banbenniandaiYear;
	}

	public void setBanbenniandaiYear(String banbenniandaiYear) {
		this.banbenniandaiYear = banbenniandaiYear;
	}

	public String getWorkUri() {
		return workUri;
	}

	public void setWorkUri(String workUri) {
		this.workUri = workUri;
	}

	public String getCallno() {
		return callno;
	}

	public void setCallno(String callno) {
		this.callno = callno;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getJdS() {
		return jdS;
	}

	public void setJdS(String jdS) {
		this.jdS = jdS;
	}

	public String getJdT() {
		return jdT;
	}

	public void setJdT(String jdT) {
		this.jdT = jdT;
	}

}
