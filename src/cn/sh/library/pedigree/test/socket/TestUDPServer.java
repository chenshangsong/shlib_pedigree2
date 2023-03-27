package cn.sh.library.pedigree.test.socket;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class TestUDPServer
{
	public static void main(String args[]) throws Exception
	{
		//定义码头
		DatagramSocket ds = new DatagramSocket(5678);  //5678表示该码头占用的是5678这个编号，因为一台计算机可以有多个码头接收多个数据，这些码头用不同的编号来表示，这些编号的专业术语就是端口号
		System.out.println("服务端已启动.等待接收收据"); 
		
		//定义可以用来接受数据的集装箱
		byte buf[] = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		
		try
		{
			while(true)
			{
				//在码头上用集装箱接受对方发送过来的数据
				ds.receive(dp); //注意：本语句执行完毕就意味着，dp数据包中就已经含有了从客户端接收过来的数据
				System.out.println("服务端已接收到数据"); 
				//从集装箱中取出对方发送过来的数据
				ByteArrayInputStream bais = new ByteArrayInputStream(dp.getData()); //1、 ByteArrayInputStream的内核必须是个字节数组，并且是从该字节数组中读取数据  2、dp.getData()表示把dp集装箱中的数据转化为一个字节数组并返回该字节数组  
				DataInputStream dis = new DataInputStream(bais);
				System.out.println("服务端已接收客户端的字符串："+dis.readUTF()); 
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ds.close();  //关闭码头
		}
		
	}
}
/*
	在JDK 1.6中的运行结果是：
----------------------------------
10000
数据源自: 127.0.0.1 : 1464
10000
数据源自: 127.0.0.1 : 1471
10000
数据源自: 127.0.0.1 : 1474
----------------------------------
*/