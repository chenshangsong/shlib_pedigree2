package cn.sh.library.pedigree.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.web.multipart.MultipartFile;

import cn.sh.library.pedigree.framework.commom.FConstant;
import cn.sh.library.pedigree.framework.csv.CsvReader;
import cn.sh.library.pedigree.framework.csv.CsvWriter;
import cn.sh.library.pedigree.framework.model.BaseDto;
/**
 * 类名 : FileUtil <br>
 * 机能概要 : File共通操作工具</br> 版权所有: Copyright © 2011 TES Corporation, All Rights
 * Reserved.</br>
 * 
 * @author fanjj
 * @version 1.0
 * 
 */
public class FileUtil {
	public static String WEB_INF_PATH = "";

	/***
	 * 写byte数据方法.通用方法
	 * 
	 * @param bytes
	 *            要写的byte数组
	 * @param fileName
	 *            文件名
	 */
	public static void writeToBytes(byte bytes[], String fileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName, true);
			fos.write(bytes);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException iex) {
			}
		}
	}

	/***
	 * 读取csv数据
	 * 
	 * @param fullPath
	 *            全路径
	 * @return 数据
	 * @throws Exception
	 */
	public static List<List<String>> readCsvData(String fullPath)
			throws Exception {

		List<List<String>> infoList = new ArrayList<List<String>>();

		// 判断文件夹是否存在
		File tmp = new File(fullPath);
		if (!tmp.exists()) {
			return null;
		}
		// 读取csv数据
		CsvReader csvReader = new CsvReader(fullPath, ',',
				Charset.forName("UTF-8"));

		while (csvReader.readRecord()) {

			List<String> info = new ArrayList<String>();
			for (int i = 0; i < csvReader.getColumnCount(); i++) {
				info.add(csvReader.get(i));
			}
			infoList.add(info);
		}
		csvReader.close();
		return infoList;
	}

	public static void writeCsvData(ArrayList<BaseDto> list, String configPath,
			String fileName, String charset) throws Exception {
		List<String[]> data = new ArrayList<String[]>();
		for (BaseDto dto : list) {
			data.add(dto.getListString());
		}
		writeCsvData(data, FConstant.DOWNLOAD_FULL_PATH, fileName, charset);
	}
	
	public static void writeCsvData(ArrayList<BaseDto> list, String configPath,
			String fileName, String charset, boolean preserveSpaces,boolean formatSpaces) throws Exception {
		List<String[]> data = new ArrayList<String[]>();
		for (BaseDto dto : list) {
			data.add(dto.getListString());
		}
		writeCsvData(data, FConstant.DOWNLOAD_FULL_PATH, fileName, charset,preserveSpaces,formatSpaces);
	}

	public static void writeCsvData(List<String[]> data, String configPath,
			String fileName) throws Exception {
		writeCsvData(data, configPath, fileName, "UTF-8");
	}

	/***
	 * 导出csv数据
	 * 
	 * @param data
	 *            数据
	 * @return fullPath 导出全路径
	 * @throws Exception
	 */
	public static void writeCsvData(List<String[]> data, String configPath,
			String fileName, String charset) throws Exception {
		writeCsvData(data,configPath,fileName,charset,false,true);
	}
	
	/***
	 * 导出csv数据
	 * 
	 * @param data
	 *            数据
	 * @return fullPath 导出全路径
	 * @throws Exception
	 */
	public static void writeCsvData(List<String[]> data, String configPath,
			String fileName, String charset, boolean preserveSpaces,boolean formatSpaces) throws Exception {
		String fullPath = CodeMsgUtil.getConfig(configPath, WEB_INF_PATH,
				fileName);

		// 判断文件夹是否存在
		File tmp = new File(fullPath.replace(fileName, ""));
		if (!tmp.exists()) {
			tmp.mkdir();
		}

		// 创建文件
		CsvWriter writer = new CsvWriter(fullPath, ',',
				Charset.forName(charset));

		for (int i = 0; i < data.size(); i++) {

			String[] strs = data.get(i);
			writer.writeRecord(strs,preserveSpaces,formatSpaces);
		}

		writer.close();
	}

	/**
	 * 保存文件到服务器
	 * 
	 * @param multifile
	 *            spring框架MultipartFile
	 * @param path
	 *            保存路径
	 * @return
	 * @throws IOException
	 */
	public static String saveFileToServer(MultipartFile multifile, String path,
			String fileName) throws IOException {

		path = CodeMsgUtil.getConfig(path, WEB_INF_PATH, "");
		// 创建目录
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		path = path + fileName;
		// 读取文件流并保持在指定路径
		InputStream inputStream = multifile.getInputStream();
		OutputStream outputStream = new FileOutputStream(path);

		byte[] buffer = multifile.getBytes();
		int byteread = 0;
		while ((byteread = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, byteread);
			outputStream.flush();
		}
		outputStream.close();
		inputStream.close();
		return path;
	}

	/***
	 * 读取EXCEL数据
	 * 
	 * @param fullPath
	 *            全路径
	 * @return 数据
	 * @throws Exception
	 */
	@SuppressWarnings("finally")
	public static List<List<String>> readExcelData(String fullPath)
			throws Exception {

		List<List<String>> infoList = new ArrayList<List<String>>();

		// 判断文件夹是否存在
		File tmp = new File(fullPath);
		if (!tmp.exists()) {
			return null;
		}

		// 读取EXCEL数据
		HSSFWorkbook wb = null;
		try {
			FileInputStream in = new FileInputStream(fullPath);// 文件输入流
			POIFSFileSystem fs = new POIFSFileSystem(in);// 构建poifsfileSystem对象，根据输入流
			wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);

			for (int i = 0; i <= sheet.getLastRowNum(); i++) {

				HSSFRow oneHeadRow = sheet.getRow(i);// 第三行
				if (oneHeadRow != null && !StringUtil.isEmpty(oneHeadRow)) {
					List<String> info = new ArrayList<String>();
					for (int j = 0;; j++) {
						if (!StringUtil.isEmpty(oneHeadRow.getCell(j))) {
							info.add(oneHeadRow.getCell(j).toString());
						} else {
							break;
						}
					}
					infoList.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return infoList;
		}
	}

}
