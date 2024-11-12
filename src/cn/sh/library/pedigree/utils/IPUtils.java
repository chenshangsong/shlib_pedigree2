package cn.sh.library.pedigree.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.sh.library.pedigree.common.onlineInfomation.RequestFilter;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtils {
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : RequestFilter.threadLocalRequest.get();
    }
    private static final String IP_UTILS_FLAG = ",";
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP = "0:0:0:0:0:0:0:1";
    private static final String LOCALHOST_IP1 = "127.0.0.1";
    //黑名单IP段 20240123 chenss
private static final String[] blackIpRange = new String[]{
        "10.6.160.0-10.6.175.255",
//        "10.6.152.0-10.6.155.255",东馆内网
        "10.6.156.0-10.6.159.255",
        "10.6.196.0-10.6.199.255",

        "10.1.181.0-10.1.184.255",
        "10.1.195.0-10.1.196.255",
//        "10.1.187.0-10.1.189.255",西馆内网
        "10.1.172.0-10.1.179.255"
};
    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            //以下两个获取在k8s中，将真实的客户端IP，放到了x-Original-Forwarded-For。而将WAF的回源地址放到了 x-Forwarded-For了。
            ip = request.getHeader("X-Original-Forwarded-For");
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Forwarded-For");
            }
            //获取nginx等代理的ip
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("x-forwarded-for");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
            //兼容k8s集群获取ip
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (LOCALHOST_IP1.equalsIgnoreCase(ip) || LOCALHOST_IP.equalsIgnoreCase(ip)) {
                    //根据网卡取本机配置的IP
                    InetAddress iNet = null;
                    try {
                        iNet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                       System.out.println(e);
                    }
                    ip = iNet.getHostAddress();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        //使用代理，则获取第一个IP地址
        if (!StringUtils.isEmpty(ip) && ip.indexOf(IP_UTILS_FLAG) > 0) {
            ip = ip.substring(0, ip.indexOf(IP_UTILS_FLAG));
        }

        return ip;
    }
    /**
     * 判断是否是内网IP
     * @param ipAddress
     * @return
     */
    private static Boolean thisPrivateIP(String ipAddress) {
        try {
            if(StringUtils.isEmpty(ipAddress)){
                return false;
            }
            else if(LOCALHOST_IP.equals(ipAddress)||LOCALHOST_IP1.equals(ipAddress)){
                return true;
            }
            else{
                InetAddress inetAddress = InetAddress.getByName(ipAddress);
                return inetAddress.isSiteLocalAddress();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 加强验证，如果是内网IP，再判断是否在黑名单中，如果在黑名单中，则返回false，认为是外网IP
     * @param strongCheck
     * @return
     */
    public  static Boolean isPrivateIP(Boolean strongCheck){
        String ipaddress = IPUtils.getIpAddr(getRequest());
        Boolean isPrivateIP =  thisPrivateIP(ipaddress);
        if(strongCheck){
            if(isPrivateIP){
                for (String ipRange : blackIpRange) {
                    if(ipInRange(ipaddress,ipRange)){
                        return false;
                    }
                }
            }
        }
        return  isPrivateIP;
    }
    public  static  String getIpAddr(){
        return getIpAddr(getRequest());
    }
    /*
     * 判断IP是否在指定IP段内
     * ipRange IP段（以'-'分隔）
     * 如判断192.168.1.100 是否在IP段192.168.1.1-192.168.1.254内
     * @param ipRange
     * @param ip
     * @return boolean
     */
    public static boolean ipInRange(String ip, String ipRange) {
        if (ipRange == null)
            throw new NullPointerException("IP段不能为空！");
        if (ip == null)
            throw new NullPointerException("IP不能为空！");
        ipRange = ipRange.trim();
        ip = ip.trim();
        final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
        final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;
        if (!ipRange.matches(REGX_IPB) || !ip.matches(REGX_IP))
            return false;
        int idx = ipRange.indexOf('-');
        String[] sips = ipRange.substring(0, idx).split("\\.");
        String[] sipe = ipRange.substring(idx + 1).split("\\.");
        String[] sipt = ip.split("\\.");
        long ips = 0L, ipe = 0L, ipt = 0L;
        for (int i = 0; i < 4; ++i) {
            ips = ips << 8 | Integer.parseInt(sips[i]);
            ipe = ipe << 8 | Integer.parseInt(sipe[i]);
            ipt = ipt << 8 | Integer.parseInt(sipt[i]);
        }
        if (ips > ipe) {
            long t = ips;
            ips = ipe;
            ipe = t;
        }
        return ips <= ipt && ipt <= ipe;
    }

    public static void main(String[] args) {
        // 判断192.168.1.100 是否在IP段192.168.1.1-192.168.1.254内
        String ip =IPUtils.getIpAddr(getRequest());
        System.out.println(ip);
        String ipRange = "192.168.1.1-192.168.1.254";
        //	boolean isInRange = ipIsInRange(ip, ipRange);
        boolean isInRange = ipInRange(ip, ipRange);
        System.out.println(isInRange);
    }
}