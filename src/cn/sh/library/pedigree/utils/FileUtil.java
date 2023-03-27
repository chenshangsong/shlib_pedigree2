package cn.sh.library.pedigree.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;




/**
 * File共通类
 * @author chenss
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

	/**
	 * 下载文件
	 * 
	 * @param filePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static InputStream downloadFile(String downloadFilepath)
			throws FileNotFoundException, IOException {
		File file = new File(downloadFilepath);
		InputStream inputS = new FileInputStream(file);
		// 路径为文件且不为空则进行删除  
	    /*if (file.isFile() && file.exists()) {  
	        file.delete();  
	    }  */
		return inputS;
	}
	/**
	 * 删除文件:先关闭流
	 * 
	 * @param filePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void deleteFile(String filepath,InputStream inputS)
			throws FileNotFoundException, IOException {
		File file = new File(filepath);
		//inputS.close();//当时这里没有关闭
		// 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	    }  
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
				if (oneHeadRow != null && !StringUtilC.isEmpty(oneHeadRow)) {
					List<String> info = new ArrayList<String>();
					for (int j = 0;; j++) {
						if (!StringUtilC.isEmpty(oneHeadRow.getCell(j))) {
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
