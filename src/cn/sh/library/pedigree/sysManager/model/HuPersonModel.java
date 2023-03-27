package cn.sh.library.pedigree.sysManager.model;

import java.util.ArrayList;
import java.util.List;

public class HuPersonModel {
	protected int seqid;
	protected int id;
	protected String fname;
	protected String fnameUri;
	protected String mingzi;
	protected String puming;
	protected String bei;
	protected String zi;
	protected String hao;
	protected String hang;
	protected String shihao;
	protected String shengyu;
	protected String zuyu;
	protected String fuqin;
	protected String muqin;
	protected String peiou;
	protected String shuoming;
	protected String shidaidaima;
	protected String fuqinyema;
	protected String releatedWork;
	protected String role;//角色
	protected String sex;//性别
	protected String selfUri;
	protected String laogongID;
	protected String laogongUri;
	protected String laogongSeqid;
	protected String fuqinUri;
	protected List<HuPersonModel> peiouList = new ArrayList<HuPersonModel>();
	public int getSeqid() {
		return seqid;
	}

	public void setSeqid(int seqid) {
		this.seqid = seqid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getFnameUri() {
		return fnameUri;
	}

	public void setFnameUri(String fnameUri) {
		this.fnameUri = fnameUri;
	}

	public String getMingzi() {
		return mingzi;
	}

	public void setMingzi(String mingzi) {
		this.mingzi = mingzi;
	}

	public String getPuming() {
		return puming;
	}

	public void setPuming(String puming) {
		this.puming = puming;
	}

	public String getBei() {
		return bei;
	}

	public void setBei(String bei) {
		this.bei = bei;
	}

	public String getZi() {
		return zi;
	}

	public void setZi(String zi) {
		this.zi = zi;
	}

	public String getHao() {
		return hao;
	}

	public void setHao(String hao) {
		this.hao = hao;
	}

	public String getHang() {
		return hang;
	}

	public void setHang(String hang) {
		this.hang = hang;
	}

	public String getShihao() {
		return shihao;
	}

	public void setShihao(String shihao) {
		this.shihao = shihao;
	}

	public String getShengyu() {
		return shengyu;
	}

	public void setShengyu(String shengyu) {
		this.shengyu = shengyu;
	}

	public String getZuyu() {
		return zuyu;
	}

	public void setZuyu(String zuyu) {
		this.zuyu = zuyu;
	}

	public String getFuqin() {
		return fuqin;
	}

	public void setFuqin(String fuqin) {
		this.fuqin = fuqin;
	}

	public String getMuqin() {
		return muqin;
	}

	public void setMuqin(String muqin) {
		this.muqin = muqin;
	}

	public String getPeiou() {
		return peiou;
	}

	public void setPeiou(String peiou) {
		this.peiou = peiou;
	}

	public String getShuoming() {
		return shuoming;
	}

	public void setShuoming(String shuoming) {
		this.shuoming = shuoming;
	}

	public String getShidaidaima() {
		return shidaidaima;
	}

	public void setShidaidaima(String shidaidaima) {
		this.shidaidaima = shidaidaima;
	}

	public String getFuqinyema() {
		return fuqinyema;
	}

	public void setFuqinyema(String fuqinyema) {
		this.fuqinyema = fuqinyema;
	}

	public String getReleatedWork() {
		return releatedWork;
	}

	public void setReleatedWork(String releatedWork) {
		this.releatedWork = releatedWork;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSelfUri() {
		return selfUri;
	}

	public void setSelfUri(String selfUri) {
		this.selfUri = selfUri;
	}

	public String getLaogongID() {
		return laogongID;
	}

	public void setLaogongID(String laogongID) {
		this.laogongID = laogongID;
	}

	public String getLaogongUri() {
		return laogongUri;
	}

	public void setLaogongUri(String laogongUri) {
		this.laogongUri = laogongUri;
	}

	public String getLaogongSeqid() {
		return laogongSeqid;
	}

	public void setLaogongSeqid(String laogongSeqid) {
		this.laogongSeqid = laogongSeqid;
	}

	public String getFuqinUri() {
		return fuqinUri;
	}

	public void setFuqinUri(String fuqinUri) {
		this.fuqinUri = fuqinUri;
	}

	public List<HuPersonModel> getPeiouList() {
		return peiouList;
	}

	public void setPeiouList(List<HuPersonModel> peiouList) {
		this.peiouList = peiouList;
	}
}
