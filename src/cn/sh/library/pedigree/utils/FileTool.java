package cn.sh.library.pedigree.utils;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import cn.sh.library.pedigree.common.dataImport.DataUtilC;

/**
 * 文件工具
 * 
 * @author chen
 * 
 */
public final class FileTool {

	double size = 0.0;

	/**
	 * 计算文件或者文件夹的大小 ，单位 MB
	 * 
	 * @param file
	 *            要计算的文件或者文件夹 ， 类型：java.io.File
	 * @return 大小，单位：MB
	 * @throws IOException
	 */
	public static double getSize(File file) {
		// 判断文件是否存在
		if (file.exists()) {
			// 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
			if (!file.isFile()) {
				// 获取文件大小
				File[] fl = file.listFiles();
				double ss = 0;
				for (File f : fl)
					ss += getSize(f);
				return ss;
			} else {
				double ss = (double) file.length() / 1024 / 1024;
				return ss;
			}
		} else {
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
			return 0.0;
		}

	}

	public static double getSizes(File file) throws FileNotFoundException {
		// 判断文件是否存在
		if (file.exists()) {
			// 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
			if (!file.isFile()) {
				// 获取文件大小
				File[] fl = file.listFiles();
				double ss = 0;
				for (File f : fl)
					ss += getSize(f);
				return ss;
			} else {
				FileChannel fc = null;
				FileInputStream fis = new FileInputStream(
						file.getAbsolutePath());
				fc = fis.getChannel();
				double ss;
				try {
					ss = (double) fc.size() / 1024 / 1024;
					fis.close();
					return StringUtilC.Round(ss, 2);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return 0.0;
			}
		} else {
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
			return 0.0;
		}

	}

	/**
	 * 获取目录大小
	 * 
	 * @param _path
	 * @return
	 */
	public static double getDirectorySize(String _path) {

		FileTool fd = new FileTool();
		double all = fd.getSize(new File(_path));
		return all;

	}

	/**
	 * 复制文件并覆盖
	 *
	 * @author Starwhere
	 *
	 * @date 2014-6-18
	 *
	 * @param source
	 * @param destination
	 * @return
	 */
	public static boolean copyFile(String source, String destination) {
		Path _source = Paths.get(source);
		Path _destination = Paths.get(destination);
		try {
			if (!_destination.getParent().toFile().exists()) {
				Files.createDirectories(_destination.getParent());
			}
			Files.copy(_source, _destination,
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 移动文件并覆盖
	 *
	 * @author Starwhere
	 *
	 * @date 2014-6-18
	 *
	 * @param source
	 * @param destination
	 * @return
	 */
	public static boolean moveFile(String source, String destination) {
		Path _source = Paths.get(source);
		Path _destination = Paths.get(destination);
		try {
			if (!_destination.getParent().toFile().exists()) {
				Files.createDirectories(_destination.getParent());
			}
			Files.move(_source, _destination,
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 删除文件并新建
	 *
	 * @author Starwhere
	 *
	 * @date 2014-6-18
	 *
	 * @param source
	 * @param destination
	 * @return
	 */
	public static boolean deleteNewFile(String path1, String path2) {
		Path _source = Paths.get(path1);
		Path _source2 = Paths.get(path1);
		try {
			if (Files.isSameFile(_source, _source2)) {
				Files.delete(_source);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = files[i].delete();
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 *
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public static boolean DeleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return file.delete();
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
	/**
	 * 彩色转为黑白
	 * 
	 * @author zack
	 * @param source
	 *            输入文件路径
	 * @param result
	 *            输出文件路径
	 **/
	public static void gray(String source, String result) {
		try {
			BufferedImage src = ImageIO.read(new File(source));
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			ImageIO.write(src, "JPEG", new File(result)); // 输出 JPEG 格式
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取图片宽、高
	 * 
	 * @author zack
	 * @param source
	 *            文件地址
	 * @param type
	 *            获取类型，h:高;w:宽;
	 * @return
	 */
	public static Integer getPicHeightOrWith(String sourcePath, String type) {
		Integer num = 0;
		try {
			BufferedImage src = ImageIO.read(new File(sourcePath));
			if ("h".equals(type)) {
				num = src.getHeight();
			} else if ("w".equals(type)) {
				num = src.getWidth();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return num;
	}

	/**
	 * 替换标签
	 * 
	 * @param _tagname
	 * @param _replacestr
	 * @param _doc
	 */
	public static void replaceTag(String _tagname, String _replacestr,
			XWPFDocument _doc) {

		try {
			List<XWPFParagraph> _paralist = _doc.getParagraphs();
			for (int i = 0; i < _paralist.size(); i++) {
				if (_paralist.get(i).getParagraphText().contains(_tagname)) {
					XWPFRun _r = _paralist.get(i).createRun();
					_r.setText(_replacestr);
					_paralist.get(i).removeRun(1);
					_paralist.get(i).addRun(_r);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 替换段落里面的变量
	 * 
	 * @param doc
	 *            要替换的文档
	 * @param params
	 *            参数
	 */
	public static void replaceInPara(XWPFDocument doc,
			Map<String, Object> params) {
		Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
		XWPFParagraph para;
		while (iterator.hasNext()) {
			para = iterator.next();
			replaceInPara(para, params);
		}
	}

	/**
	 * 替换段落里面的变量
	 * 
	 * @param para
	 *            要替换的段落
	 * @param params
	 *            参数
	 */
	private static void replaceInPara(XWPFParagraph para,
			Map<String, Object> params) {
		List<XWPFRun> runs;
		Matcher matcher2;
		if (matcher(para.getParagraphText()).find()) {
			runs = para.getRuns();
			for (int i = 0; i < runs.size(); i++) {
				XWPFRun run = runs.get(i);
				String runText = run.toString();
				matcher2 = matcher(runText);
				if (matcher2.find()) {
					while ((matcher2 = matcher(runText)).find()) {
						runText = matcher2.replaceFirst(String.valueOf(params
								.get(matcher2.group(1))));
					}
					// 直接调用XWPFRun的setText()方法设置文本时，在底层会重新创建一个XWPFRun，把文本附加在当前文本后面，
					// 所以我们不能直接设值，需要先删除当前run,然后再自己手动插入一个新的run。
					para.removeRun(i);
					para.insertNewRun(i).setText(runText);
				}
			}
		}
	}

	/**
	 * 替换表格里面的变量
	 * 
	 * @param doc
	 *            要替换的文档
	 * @param params
	 *            参数
	 */
	public static void replaceInTable(XWPFDocument doc,
			Map<String, Object> params) {
		Iterator<XWPFTable> iterator = doc.getTablesIterator();
		XWPFTable table;
		List<XWPFTableRow> rows;
		List<XWPFTableCell> cells;
		List<XWPFParagraph> paras;
		while (iterator.hasNext()) {
			table = iterator.next();
			rows = table.getRows();
			for (XWPFTableRow row : rows) {
				cells = row.getTableCells();
				for (XWPFTableCell cell : cells) {
					paras = cell.getParagraphs();
					for (XWPFParagraph para : paras) {
						replaceInPara(para, params);
					}
				}
			}
		}
	}

	/**
	 * 正则匹配字符串:输出word使用
	 * 
	 * @param str
	 * @return
	 */
	public static Matcher matcher(String str) {
		Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		return matcher;
	}

	/**
	 * 重命名文件
	 * 
	 * @param filepath
	 */
	public static void RenameFile(String filepath) {
		File file = new File(filepath); // 指定文件名及路径
		String filename = file.getAbsolutePath();
		if (filename.indexOf(".") >= 0) {
			filename = filename.substring(0, filename.lastIndexOf("."));
		}
		file.renameTo(new File(filename.toUpperCase() + ".jpg")); // 改名
	}

	/**
	 * 重命名文件
	 * 
	 * @param file
	 * @param toFile
	 */
	public void renameFile(String file, String toFile) {
		// new Main().renameFile("C:\\temp\\file1.txt", "C:\\temp\\file2.txt");
		File toBeRenamed = new File(file);
		// 检查要重命名的文件是否存在，是否是文件
		if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
			System.out.println("File does not exist: " + file);
			return;
		}
		File newFile = new File(toFile);
		// 修改文件名
		if (toBeRenamed.renameTo(newFile)) {
			System.out.println("File has been renamed.");
		} else {
			System.out.println("Error renmaing file");
		}

	}

	/**
	 * 遍历某文件文件夹下的所有文件
	 * 
	 * @param path
	 */
	public static List<File> traverseFolder2(String path) {
		File file = new File(path);
		List<File> filelist = new ArrayList<File>();
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("文件夹是空的!");
				return null;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
					//	System.out.println("文件夹:" + file2.getAbsolutePath());
						traverseFolder2(file2.getAbsolutePath());
					} else {
						filelist.add(file2);
						//System.out.println("文件:" + file2.getAbsolutePath());
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
		return filelist;
	}

	/**
	 * 遍历某文件文件夹下的所有文件
	 * 
	 * @param strPath
	 * @return
	 */
	public static List<File> getFileList(String strPath) {
		List<File> filelist = new ArrayList<File>();
		File dir = new File(strPath);
		File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String fileName = files[i].getName();
				if (files[i].isDirectory()) { // 判断是文件还是文件夹
					getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
				} else if (fileName.endsWith("jpg")) { // 判断文件名是否以.avi结尾
					String strFileName = files[i].getAbsolutePath();
					System.out.println("---" + strFileName);
					filelist.add(files[i]);
				} else {
					continue;
				}
			}

		}
		return filelist;
	}

	public static void main(String[] args) {
		//String result = "D:\\eclipse-luna-win64\\workspace\\pedigree\\WebRoot\\WEB-INF\\res\\images\\jpfm";
		String result = "C:\\Users\\Administrator\\Desktop\\my\\chenyunchang\\yishuzhao";
		List<File> fileList = traverseFolder2(result);
		for (File file : fileList) {
			String filename = file.getAbsolutePath();
			if (filename.indexOf(".") >= 0) {
				filename = filename.substring(0, filename.lastIndexOf("."));
				String newName = DataUtilC.getRandomUriValue(16);
				file.renameTo(new   File(result+"\\"+newName+".jpg"));   //改名  
				System.err.println(filename.substring(filename.lastIndexOf("\\")+1)+"  "+newName);
			}
			
		}
	}
}
