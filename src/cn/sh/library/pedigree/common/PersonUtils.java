package cn.sh.library.pedigree.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.sh.library.pedigree.sysManager.model.PersonModel;
import cn.sh.library.pedigree.utils.StringUtilC;

public class PersonUtils {

	public static PersonModel getXzName(String nameList, String niandaiList,
			String f) {
		// 姓氏，空处理
		f = StringUtilC.getString(f);
		// *
		// 禎，字餘元，行二
		// 梁御史中丞昭（字晦甫）*謙，字潛翁，號益齋，行文二
		// 臯，字子高*胤，又名商，字吾曾
		// 俣（字文穆）*倓
		// 伯庠*伯序*伯颿
		// 矩，字玄度，號以行
		PersonModel personAlls = new PersonModel();
		List<PersonModel> personList = new ArrayList<PersonModel>();
		nameList = nameList.replace("（", "，");
		nameList = nameList.replace(",", "，");
		nameList = nameList.replace("）", "");
		nameList = nameList.replace("\n", "");
		// 多个始祖
		nameList = nameList.replace("、", "*");
		// 多个时代
		niandaiList = niandaiList.replace("、", "*");
		niandaiList = niandaiList.replace("\n", "");
		// 多个始祖
		if (nameList.contains("*")) {
			String[] names = nameList.split("\\*");
			for (int i = 0; i < names.length; i++) {
				PersonModel _model3 = new PersonModel();
				_model3 = getZhiValue(names[i], f);
				personList.add(_model3);
			}
		}
		// 单个始祖
		else {
			PersonModel _model4 = new PersonModel();
			_model4 = getZhiValue(nameList, f);
			personList.add(_model4);
		}
		personAlls.setPersonList(personList);
		// 设置时代信息
		getNianDai(personAlls, niandaiList);
		return personAlls;
	}

	/**
	 * 设置时代信息
	 * 
	 * @param model
	 * @param niandaiList
	 */
	private static void getNianDai(PersonModel model, String niandaiList) {
		if (!StringUtilC.isEmpty(niandaiList)) {
			String[] niandai = niandaiList.split("\\*");
			if (niandai.length == 1) {
				for (int i = 0; i < model.getPersonList().size(); i++) {
					model.getPersonList().get(i).setShidai(niandai[0]);
				}
			} else {
				for (int i = 0; i < model.getPersonList().size(); i++) {
					if (i < niandai.length) {
						model.getPersonList().get(i).setShidai(niandai[i]);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param Sname
	 * @param f
	 * @return
	 */
	private static PersonModel getZhiValue(String Sname, String f) {
		PersonModel _model3 = new PersonModel();
		if (Sname.contains("，")) {
			String zihang[] = Sname.toString().split("，");
			for (int j = 0; j < zihang.length; j++) {
				if (j == 0) {
					// 如果名字中，不包含姓氏，则为姓氏加名字
					if (!zihang[j].startsWith(f)) {
						_model3.setAllName(f + zihang[j]);
					} else {
						_model3.setAllName(zihang[j]);
					}
					continue;
				}
				if (zihang[j].startsWith("字")) {
					String zi = zihang[j].substring(1);
					_model3.setZhi(zi);
					continue;
				}
				if (zihang[j].startsWith("又字")) {
					String zi = zihang[j].substring(2);
					_model3.setZhi(_model3.getZhi() + "," + zi);
					continue;
				}
				if (zihang[j].startsWith("號")) {
					String zi = zihang[j].substring(1);
					_model3.setHao(zi);
					continue;
				}
				if (zihang[j].startsWith("又號")) {
					String zi = zihang[j].substring(2);
					_model3.setHao(_model3.getHao() + "," + zi);
					continue;
				}
				if (zihang[j].startsWith("行")) {
					String zi = zihang[j].substring(1);
					_model3.setHang(zi);
					continue;
				}

				if (zihang[j].startsWith("谥")) {
					String zi = zihang[j].substring(1);
					_model3.setShi(zi);
					continue;
				}
				if (zihang[j].startsWith("又谥")) {
					String zi = zihang[j].substring(2);
					_model3.setShi(_model3.getShi() + "," + zi);
					continue;
				}
			}

		}// 只有一个名字
		else {
			// 如果名字中，不包含姓氏，则为姓氏加名字
			if (!Sname.startsWith(f)) {
				_model3.setAllName(f + Sname);
			} else {
				_model3.setAllName(Sname);
			}

		}
		return _model3;
	}

	/**
	 * 根据馆藏地，获取括号内内容
	 * 
	 * @param yString
	 * @param cString
	 * @return
	 */
	public static String getDescription(String yString, String cString) {
		yString = yString.replace("(", "（").replace(")", "）");
		yString.replace("\"", "\\\\\"").replace("\n", "").replace(",", "，")
				.trim();
		String[] sczListStr = yString.split("\\*");
		List<String> sczList = Arrays.asList(sczListStr);
		String descption = "";
		for (String this0 : sczList) {
			if (this0.contains(cString)) {
				if (this0.contains("（") && this0.contains("）")) {
					int begIndex = this0.indexOf("（") + 1;
					int endIndex = this0.lastIndexOf("）");
					if (endIndex >= begIndex) {
						descption = this0.substring(begIndex, endIndex);
					}
					// System.out.println(descption);
					return descption;
				}
			}
		}
		return descption;
	}

	public static void main(String[] args) {
		String mm = "江西萍乡人。字道希，号云阁、芗德、罗霄山人，晚号纯常子。光绪进士。";
		String[] nn = mm.split("。");
		for (int i = 0; i < nn.length; i++) {
			if (nn[i].contains("，")) {
				String n[] = nn[i].split("，");
				for (int j = 0; j < n.length; j++) {
					if (n[j].contains("、")) {
						String m[] = n[j].split("、");
						for (int k = 0; k < m.length; k++) {
							if (m[k].startsWith("字")) {
								System.out.println(m[k].substring(1));
							}
							if (m[k].startsWith("号")) {
								System.out.println(m[k].substring(1));
							} else {
								if (n[j].startsWith("字")) {
									System.out.println(n[j].substring(1));
								}
								if (n[j].startsWith("号")) {
									System.out.println(n[j].substring(1));
								}
							}
						}
					} else {
						if (n[j].startsWith("字")) {
							System.out.println(n[j].substring(1));
						}
						if (n[j].startsWith("号")) {
							System.out.println(n[j].substring(1));
						}
					}
				}
			} else {
				if (nn[i].startsWith("字")) {
					System.out.println(nn[i].substring(1));
				}
				if (nn[i].startsWith("号")) {
					System.out.println(nn[i].substring(1));
				}
			}
		}

		// getDescription("", "");
		/*
		 * String nameList = "該，字元仁，號善皆，又號長者"; String name = "清"; String f =
		 * "陈"; PersonModel model = new PersonModel(); model =
		 * PersonUtils.getXzName(nameList, name, f); System.out.println(model);
		 */
	}
}
