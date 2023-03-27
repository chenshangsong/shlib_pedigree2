package cn.sh.library.pedigree.framework.commom;

/**
 * クラス名 : FWConstants <br>
 * 機能概要 : 定数クラス</br> コピーライト: Copyright © 2011 NC Corporation, All Rights
 * Reserved.</br>
 * 
 * @author chenshangsong
 * @version 1.0
 * 
 */
public class FConstant {

	// 上传文件路径
	public static final String UPLOAD_FULL_PATH = "uploadFullPath";

	// 下载文件路径
	public static final String DOWNLOAD_FULL_PATH = "downloadFullPath";

	// 下载文件路径
	public static final String LOG_FULL_PATH = "logFullPath";

	// 礼金卡导入模版名称
	public static final String GIFT_TEMPNAME = "giftTempName";

	// 团购卡导入模版名称
	public static final String GROUP_TEMPNAME = "groupTempName";

	// 1,非NULL 必须输入
	public static final String MUST_INPUT = "1";

	// 0,NULL 不许输入
	public static final String NOT_INPUT = "0";

	// 后台验证前台jqgrid标注
	public static final String JQGRID_LIST = "list";

	// 0 : 初始金额或张数
	public static final String ZERO = "0";
	// 系统用户：日次处理，自动处理使用
	public static final String SYSTEMUSER = "system";
	//日次处理：自动处理区分
	public static final String auto_daily_batch="1";
	//处理失败
	public static final String batchFailed="-1";
	//团购卡最大发放金额
	public static final String MAX_GROUP_CARD_PROVIDE_AMOUNT = "maxGroupCardProvideAmount";
	//礼金卡最大发放金额
	public static final String MAX_GIFT_CARD_PROVIDE_AMOUNT = "maxGiftCardProvideAmount";
	
	//  
	public static final String CARD_PWD_LEN = "cardPasswordLength";
	// 系统配置表中，商品编码长度名称
		public static final String ProductIdLength = "ProductIdLength";
		
		//购买金额名次最大值
		public static final String	MAXAMOUNTRANK = "MaxAmountRank";
			
		// 购买次数名次最大值	
		public static final String MAXCOUNTRANK = "MaxCountRank";
}
