package cn.sh.library.pedigree.common.onlineInfomation;

import java.util.ArrayList;
/**
 * 
 * @author chenss
 *
 */
public class OnLineUtil {

	// 用于判断所有用户中，是否存在当前用户的访问信息
	public static Object getByUserId(ArrayList<OnLineUser> list,
			String sessionId) {

		// if(!list.isEmpty()){
		for (int i = 0; i < list.size(); i++) {
			OnLineUser user = list.get(i);
			if (user.getSessionId().equals(sessionId)) {
				return user;// 所有用户中有，已经存在当前用户的访问信息
			}
		}
		// }
		return null;// 所有用户中有，不存在当前用户的访问信息
	}
}