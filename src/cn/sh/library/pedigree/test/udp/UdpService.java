package cn.sh.library.pedigree.test.udp;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
/**
 * @说明 UDP连接服务端，这里一个包就做一个线程处理
 * @author 陈尚松
 * @version 1.0
 * @since
 */
public class UdpService {
	public static void main(String[] args) {
		try {
			init();
			while(true){
				try {
					byte[] buffer = new byte[1024*64]; // 缓冲区
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					receive(packet);
					new Thread(new ServiceImpl(packet)).start();
				} catch (Exception e) {
				}
				Thread.sleep(1 * 1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 接收数据包，该方法会造成线程阻塞
	 * @return
	 * @throws Exception 
	 * @throws IOException
	 */
	public static DatagramPacket receive(DatagramPacket packet) throws Exception {
		try {
			datagramSocket.receive(packet);
			ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData()); //1、 ByteArrayInputStream的内核必须是个字节数组，并且是从该字节数组中读取数据  2、dp.getData()表示把dp集装箱中的数据转化为一个字节数组并返回该字节数组  
			DataInputStream dis = new DataInputStream(bais);
			System.out.println("服务端已接收客户端的字符串："+dis.readUTF()); 
			System.out.println("服务端接收到数据：---"+Arrays.toString(packet.getData()));
			return packet;
		} catch (Exception e) {
			System.out.println("服务端接收异常：未接收到数据"+e);
			throw e;
		}
	}
	/**
	 * 将响应包发送给请求端
	 * @param bt
	 * @throws IOException
	 */
	public static void response(DatagramPacket packet) {
		try {
			datagramSocket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化连接
	 * @throws SocketException
	 */
	public static void init(){
		try {
			//socketAddress = new InetSocketAddress("192.168.1.107", 39169);
			datagramSocket = new DatagramSocket(39169);
			datagramSocket.setSoTimeout(5 * 1000);
			System.out.println("服务端已经启动");
		} catch (Exception e) {
			datagramSocket = null;
			System.err.println("服务端启动失败");
			e.printStackTrace();
		}
	}
	private static InetSocketAddress socketAddress = null; // 服务监听个地址
	private static DatagramSocket datagramSocket = null; // 连接对象
}
/**
 * @说明 打印收到的数据包，并且将数据原封返回，中间设置休眠表示执行耗时
 * @author 陈尚松
 * @version 1.0
 * @since
 */
class ServiceImpl implements Runnable {
	private DatagramPacket packet;
	public ServiceImpl(DatagramPacket packet){
		this.packet = packet;
	}
	public void run() {
		try {
			byte[] bt = new byte[packet.getLength()];
			System.arraycopy(packet.getData(), 0, bt, 0, packet.getLength());
			System.out.println("来自："+packet.getAddress().getHostAddress() + "：" + packet.getPort() + "：" + Arrays.toString(bt));
			Thread.sleep(5 * 1000); // 5秒才返回，标识服务端在处理数据
			// 设置回复的数据，原数据返回，以便客户端知道是那个客户端发送的数据
			byte[] bt2 = "6".getBytes();
			packet.setData(bt);
			UdpService.response(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
} 