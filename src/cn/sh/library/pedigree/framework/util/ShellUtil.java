package  cn.sh.library.pedigree.framework.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * クラス名 : ShellUtil <br>
 * 機能概要 : </br> コピーライト: Copyright © 2011 NC Corporation, All Rights
 * Reserved.</br>
 *
 * @author chenshangsong
 * @version 1.0
 *
 */
public class ShellUtil {
	private static final Logger logger = Logger.getLogger(ShellUtil.class);

	// result
	private static String execLog = "";

	public static void createPDF(String templatePath,String dateFilePath,String pdfFilePath) throws Exception {
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = null;

		// 格式化日期时间，记录日志时使用
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS ");
		// 生成pdf文件
		String createPdf = "prprint -grpdelimit | -assetsdir "
				+ templatePath + " -f " + dateFilePath
				+ " -atdirect file -keeppdf " + pdfFilePath;
		stringBuffer.append(dateFormat.format(new Date()))
				.append("prepare to excute shell").append(createPdf)
				.append(" \r\n");
		// String doc="cd D:/Documents and Settings/chen  "+shellCommand;
		Process pid = null;
		// String[] cmd = { doc, shellCommand };
		// String[] cmd = { "cmd"," /C ", shellCommand };
		// 执行Shell命令
		pid = Runtime.getRuntime().exec(createPdf);
		if (pid != null) {
			stringBuffer.append("process id：").append(pid.toString())
					.append("\r\n");
			// bufferedReader用于读取Shell的输出内容
			bufferedReader = new BufferedReader(new InputStreamReader(
					pid.getInputStream()), 1024);
			pid.waitFor();
		} else {
			stringBuffer.append("no pid\r\n");
		}
		stringBuffer.append(dateFormat.format(new Date())).append(
				"excute shell finish\r\n results：\r\n");
		logger.info(stringBuffer.toString());

		// clear buffer
		stringBuffer.setLength(0);
		execLog = "";
		String line = null;
		// 读取Shell的输出内容，并添加到stringBuffer中
		while (bufferedReader != null
				&& (line = bufferedReader.readLine()) != null) {
			stringBuffer.append(line).append("\r\n");
		}
		if (bufferedReader != null) {
			bufferedReader.close();
			execLog = stringBuffer.toString();
			logger.info(execLog);
		}
	}
}
