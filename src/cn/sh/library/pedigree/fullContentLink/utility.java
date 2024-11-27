package cn.sh.library.pedigree.fullContentLink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class utility {

	public String mainpath = "";

	public String templatepath = "";

	public String tempfilepath = "";

	public String tempfileaddress = "";

	public String sourcepath = "";

	public String serveraddress = "";

	Properties props;

	public utility() {

	}

	public String GetValue(String name) {
		String data = "";
		try {
			data = props.getProperty(name);
		} catch (Exception E) {
			E.printStackTrace();
		}
		return data;
	}

	public String ReplaceStr(String sOld, String sNew, String sLine) {
		int iPosition;
		iPosition = sLine.indexOf(sOld);
		if (iPosition == -1) {
			return sLine;
		}
		String sLeft, sRight;
		sLeft = sLine.substring(0, iPosition);
		sRight = sLine.substring(iPosition + sOld.length());
		return sLeft + sNew + sRight;
	}

	public String ReplaceAllStr(String sOld, String sNew, String sLine) {
		int iPosition;
		iPosition = sLine.indexOf(sOld, 0);
		String sLeft, sRight;
		while (iPosition != -1) {
			sLeft = sLine.substring(0, iPosition);
			sRight = sLine.substring(iPosition + sOld.length());
			sLine = sLeft + sNew + sRight;
			iPosition = iPosition + sNew.length();
			iPosition = sLine.indexOf(sOld, iPosition);
		}
		return sLine;
	}

	public String ReadAllLine(String sFilename) {
		try {
			String sLine = "", sAllLine = "";
			java.io.File fpFile = new java.io.File(sFilename);
			BufferedReader brIn = new BufferedReader(new FileReader(fpFile));
			while ((sLine = brIn.readLine()) != null) {
				sAllLine = sAllLine + sLine + "\n";
			}
			brIn.close();
			return sAllLine;
		} catch (FileNotFoundException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
	}

	public String NativeToUnicode(String sOld) {
		if (sOld == null || sOld.length() == 0) {
			return "";
		}
		byte[] byBuffer = new byte[sOld.length()];
		for (int i = 0; i < sOld.length(); i++) {
			byBuffer[i] = (byte) sOld.charAt(i);
		}
		return new String(byBuffer);
	}

	public String UnicodeToNative(String sOld) {

		if (sOld == null || sOld.length() == 0) {
			return "";
		}
		char[] cBuffer = new char[sOld.length() * 2];
		char cTemp = ' ';
		int n = 0;
		try {
			for (int i = 0; i < sOld.length(); i++) {
				if (sOld.charAt(i) >= 128) {
					cTemp = sOld.charAt(i);
					byte[] byTemp = ("" + cTemp).getBytes();
					cBuffer[n++] = (char) byTemp[0];
					cBuffer[n - 1] -= 65280;
					cBuffer[n++] = (char) byTemp[1];
					cBuffer[n - 1] -= 65280;
				} else {
					cBuffer[n++] = sOld.charAt(i);
				}
			}
		} catch (Exception e) {
		}
		return new String(cBuffer, 0, n);
	}

	public String CurrentDate(String sFormat) {
		Date dateNow = new Date();
		SimpleDateFormat sdfInstance = new SimpleDateFormat(sFormat);
		return sdfInstance.format(dateNow);
	}

	// xcj:2009-07-07
	public String getDate(String dateStr, String format) throws Exception {

		if (dateStr == null || format == null) {
			throw new Exception( dateStr + "|" + format);
		}

		SimpleDateFormat df = new SimpleDateFormat(format);

		try {
			Date date = df.parse(dateStr);
			return df.format(date);
		} catch (Exception ex) {
			return null;
		}
	}

	public String getEDate(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(str, pos);
		String j = strtodate.toString();
		String[] k = j.split(" ");
		String d = "";
		if (k[2].substring(0, 1).equals("0")) {
			d = " " + k[2].substring(1);
		} else {
			d = k[2];
		}

		return k[1] + " " + d + " " + k[5];
	}

	public void RWEnLogFile(String writestring, String filename) {
		try {
			File f = new File(filename);
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			raf.seek(raf.length());
			raf.writeBytes(writestring);
			raf.close();
		} catch (IOException e) {
			System.out.println("RWEnLogFile");
		}
	}

	public String[] FGstring(String collec) {
		String[] ask_num = new String[10];
		int i = 0;
		int start_pos = 0, end_pos = 0;

		for (i = 0; i < 10; i++) {
			ask_num[i] = "";
		}

		i = 0;
		end_pos = collec.indexOf(";");

		while (end_pos != -1) {
			ask_num[i] = collec.substring(start_pos, end_pos);
			i = i + 1;
			start_pos = end_pos + 1;
			collec = collec.substring(start_pos, collec.length());
			start_pos = 0;
			end_pos = collec.indexOf(";");
		}
		ask_num[i] = collec;

		return ask_num;

	}

	public String AddStringToDYString(String newstring, String oldstring) {
		String[] fgstring = FGstring(oldstring);
		int i = 0, j = 0;

		while (!fgstring[i].equals("")) {
			if (fgstring[i].equals(newstring))
				j = 1;
			i++;
		}

		if (j == 0)
			if (oldstring.equals(""))
				oldstring = newstring;
			else
				oldstring = oldstring + ";" + newstring;

		return oldstring;
	}

	public String CombineFGString(String str1, String str2) {
		String[] strsu2 = FGstring(str2);
		int i = 0;

		while (!strsu2[i].equals("")) {
			str1 = AddStringToDYString(strsu2[i], str1);
			i++;
		}

		return str1;
	}

	public String AddSpace(String oldstring, int orderlength) {
		String space = " ";
		while (oldstring.length() < orderlength) {
			oldstring += space;
		}
		return oldstring;
	}

	public String GetSharpString(String st) {
		int pos = st.indexOf("#");
		String re = "";
		if (pos == -1)
			re = st;
		else
			re = st.substring(0, pos);
		return re;
	}

	public String GetSharpString(String st, String sharp) {
		int pos = st.indexOf(sharp);
		String re = "";
		if (pos == -1)
			re = st;
		else
			re = st.substring(0, pos);
		return re;
	}

	public String CutSharpString(String st) {
		int pos = st.indexOf("#");
		String re = "";
		if (pos == -1)
			re = "";
		else
			re = st.substring(pos + 1, st.length());
		return re;
	}

	public String CutSharpString(String st, String sharp) {
		int pos = st.indexOf(sharp);
		String re = "";
		if (pos == -1)
			re = "";
		else
			re = st.substring(pos + 1, st.length());
		return re;
	}

	public String[] SharpString(String collec) {
		String sharp = "#";
		int count = GetSubNum(collec, sharp);
		String[] ask_num = new String[count + 1];
		int i = 0;
		int pos = 0;
		i = 0;
		pos = collec.indexOf(sharp);
		while (pos != -1) {
			ask_num[i] = GetSharpString(collec);
			i++;
			collec = CutSharpString(collec);
			pos = collec.indexOf(sharp);
		}
		ask_num[i] = collec;
		return ask_num;
	}

	public String[] SharpString(String collec, String sharp) {
		int count = GetSubNum(collec, sharp);
		String[] ask_num = new String[count + 1];
		int i = 0;
		int pos = 0;
		i = 0;
		pos = collec.indexOf(sharp);
		if (pos == 0) {
			collec = collec.substring(1, collec.length());
			pos = collec.indexOf(sharp);
		}
		while (pos != -1) {
			ask_num[i] = GetSharpString(collec, sharp);
			i++;
			collec = CutSharpString(collec, sharp);
			pos = collec.indexOf(sharp);
		}
		if (i < ask_num.length)
			ask_num[i] = collec;
		return ask_num;
	}

	public int GetSubNum(String st, String subst) {
		int pos = 0;
		int count = 0;
		String temp = st;
		if (temp.equals("")) {
			count = 0;
		} else {
			pos = temp.indexOf(subst);
			while (pos != -1) {
				if (pos != (temp.length() - 1))
					count++;
				temp = temp.substring(pos + 1, temp.length());
				pos = temp.indexOf(subst);
			}
			pos = st.indexOf(subst);
			if (pos == 0)
				count--;
		}
		return count;
	}

	public int compare(String[] t1, String[] t2) {
		int i = 0, j = 1;

		while (i < t1.length && i < t2.length && j == 1) {
			if (t1[i].equals(t2[i])) {
				i++;
			} else {
				j = 0;
			}
		}
		return i;
	}

	public String LastSharp(String st) {
		String ls = "";
		String sharp = "#";
		int sharp_num = GetSubNum(st, sharp);
		if (sharp_num == 0) {
			ls = "";
		} else {
			String[] tt = SharpString(st);
			ls = tt[sharp_num];
		}
		return ls;
	}

	public String[] BetweenSharp(String st) {
		String[] data;
		String[] data1 = SharpString(st);
		int num = data1.length;
		int i = 0;
		if ((num % 2) == 1) {
			data = new String[(num / 2)];
			for (i = 0; i < (num / 2); i++)
				data[i] = data1[i * 2 + 1];
		} else {
			data = new String[0];
		}
		return data;
	}

	public String QF(String st, String subst) {
		String rst = "";
		int pos = 0, i = 0;
		pos = st.indexOf(subst);
		if (pos == -1) {
			rst = st;
		} else {
			String[] d = SharpString(st, "<");
			for (i = 0; i < d.length; i++) {
				pos = d[i].indexOf(subst);
				if (pos == -1) {
					rst += "<" + d[i];
				} else {
					i = i + 1;
				}
			}
		}
		return rst;
	}

	public String qtf(String ys, String qs) {
		String tys = ys;
		int t = 0;
		t = tys.indexOf(qs);
		while (t != -1) {
			tys = tys.substring(0, t) + tys.substring(t + qs.length());
			t = tys.indexOf(qs);
		}
		return tys;
	}

	public int ifinteger(String s) {
		int i = 0;
		try {
			Integer.parseInt(s);
			i = 1;
		} catch (NumberFormatException e) {
			i = 0;
		}
		return i;
	}
	public boolean ifyear(String s) {
		boolean rt = false;
		int y = 0;
		try {
			y = Integer.parseInt(s);
			if (y > 999 && y < 10000)
				rt = true;
			else
				rt = false;
		} catch (NumberFormatException e) {
			rt = false;
		}
		return rt;
	}

	public boolean ifmonth(String s) {
		boolean rt = false;
		int y = 0;
		try {
			y = Integer.parseInt(s);
			if (y > 0 && y < 13)
				rt = true;
			else
				rt = false;
		} catch (NumberFormatException e) {
			rt = false;
		}
		return rt;
	}

	public boolean ifday(String s) {
		boolean rt = false;
		int y = 0;
		try {
			y = Integer.parseInt(s);
			if (y > 0 && y < 32)
				rt = true;
			else
				rt = false;
		} catch (NumberFormatException e) {
			rt = false;
		}
		return rt;
	}

	public String GetRandomNumber() {
		int iRint;
		String s1, s2;
		Random rRand = new Random();
		iRint = rRand.nextInt();
		if (iRint < 0) {
			iRint = 0 - iRint;
		}
		s1 = "" + iRint;
		iRint = rRand.nextInt();
		if (iRint < 0) {
			iRint = 0 - iRint;
		}
		s2 = "" + iRint;
		return (s1 + s2).substring(0, 16);
	}

	public String[] AddTwoStingArray(String[] a1, String[] a2) {
		int l1 = a1.length;
		int l2 = a2.length;
		String[] a = new String[l1 + l2];
		for (int i = 0; i < l1; i++) {
			a[i] = a1[i];
		}
		for (int i = 0; i < l2; i++) {
			a[i + l1] = a2[i];
		}
		return a;
	}

	public String[][] AddTwoStingArray(String[][] a1, String[][] a2) {
		int l1 = a1.length;
		int l2 = a2.length;
		String[][] a = new String[l1 + l2][];
		for (int i = 0; i < l1; i++) {
			a[i] = a1[i];
		}
		for (int i = 0; i < l2; i++) {
			a[i + l1] = a2[i];
		}
		return a;
	}

	public String NumToString(int old, int ws) {
		String news = "";
		news = old + "";
		while (news.length() < ws) {
			news = "0" + news;
		}
		return news;
	}

	public int countDays(String startDate, String endDate, String format) {
		try {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			Date sDate = sf.parse(startDate);
			Date eDate = sf.parse(endDate);
			Calendar c = Calendar.getInstance();

			c.setTime(sDate);
			long ls = c.getTimeInMillis();

			c.setTime(eDate);
			long le = c.getTimeInMillis();

			return (int) ((le - ls) / (24 * 3600 * 1000));
		} catch (Exception e) {
			return 0;
		}
	}

	public int strToInt(String str) {

		Integer ddd = new Integer(Integer.parseInt(str));
		int xdd = ddd.intValue();
		return xdd;

	}

}
