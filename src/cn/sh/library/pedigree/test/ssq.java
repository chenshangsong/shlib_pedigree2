package cn.sh.library.pedigree.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import cn.sh.library.pedigree.utils.StringUtilC;

/**
 * 双色球打号
 * 
 * @param ChenShangsong
 */
public class ssq {

	public static void main(String[] args) {
		System.out.println("------------1，是否选择蓝号？【y,n】-------------");
		String inBule = new String();
		Integer numbers;
		Scanner in = new Scanner(System.in);
		String flag = in.next();
		if (flag.equals("y")) {
			System.out.println("------------2，请输入蓝号-------------");
			inBule = in.next();
		}
		System.out.println("------------3，请输入需要几注-------------");
		numbers = in.nextInt();
		System.out.println("------------以下是您选择的双色球-------------");
		for (int i = 0; i < numbers; i++) {
			printNumber(inBule);
		}
		System.out.println("---------------------------------------");
		System.out.println("------------4，是否继续选号？【y,n】-------------");
		flag = in.next();
		if (flag.equals("y")) {
			main(new String[] {});
		} else {
			System.out.println("-----------选号结束---------");
		}
	}

	/**
	 * 打号总方法
	 */
	public static void printNumber(String buleNumber) {
		// TODO Auto-generated method stub
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < 7; i++) {
			// 红号
			Integer redNumber = getRed();
			if (list.contains(redNumber)) {
				redNumber = getRed();
			}
			list.add(redNumber);
		}
		// 排序
		Collections.sort(list, new Comparator<Integer>() {
			public int compare(Integer arg0, Integer arg1) {
				return arg0.compareTo(arg1);
			}
		});
		String str = new String();
		for (Integer dto : list) {
			str += StringUtilC.leftPad(2, dto.toString()) + "  ";
		}
		// 蓝号码
		if (StringUtilC.isEmpty(buleNumber)) {
			buleNumber = StringUtilC.getString(getBlue());
		}
		// 拼接 红，蓝号码
		String lastNumber = str + "[" + StringUtilC.leftPad(2, buleNumber)
				+ "]";
		// 输出最终双色球号码
		System.out.println(lastNumber);
	}

	/**
	 * 红号
	 * 
	 * @return
	 */
	public static Integer getRed() {
		Random random = new Random();// 定义随机类
		Integer result = random.nextInt(33) + 1;// 返回[0,10)集合中的整数，注意不包括10
		return result; // +1后，[0,10)集合变为[1,11)集合，满足要求
	}

	/**
	 * 蓝号
	 * 
	 * @return
	 */
	public static Integer getBlue() {
		Random random = new Random();// 定义随机类
		Integer result = random.nextInt(15) + 1;// 返回[0,10)集合中的整数，注意不包括10
		return result; // +1后，[0,10)集合变为[1,11)集合，满足要求
	}
}
