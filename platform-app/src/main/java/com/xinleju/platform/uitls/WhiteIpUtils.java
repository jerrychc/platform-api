package com.xinleju.platform.uitls;

import com.xinleju.platform.base.utils.ConfigurationUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 校验白名单
 */
public class WhiteIpUtils {
    private static Logger log = Logger.getLogger(WhiteIpUtils.class);

    /**
     * rest接口校验白名单
     */
    public static boolean checkHttpMethod(){
        boolean flag = true;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //客户端ip
        String clientIP = getIpAddress(request);
        if(StringUtils.isBlank(clientIP)){
            return flag;
        }
        try {
            //服务器ip
            String localIp = InetAddress.getLocalHost().getHostAddress();
            //本地调用，默认放行
            if(clientIP.equals(localIp) || "127.0.0.1".equals(clientIP) || "0:0:0:0:0:0:0:1".equals(clientIP)){
                return flag;
            }
            //系统配置的白名单列表
            String whiteIpList = ConfigurationUtil.getValue("whiteIpList");
            String whiteIps[] = null;
            //如果没有设置白名单，默认不校验ip
            if(StringUtils.isBlank(whiteIpList)){
                return flag;
            }
            whiteIps = whiteIpList.split(",");
            if(ArrayUtils.indexOf(whiteIps,clientIP) >= 0){
                return flag;
            }else{
                log.info("用户不在ip白名单内，clientIP=" + clientIP);
                flag = false;
                return flag;
            }
        } catch (UnknownHostException e) {
            log.info("ip白名单内报错" + e.getMessage());
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }



    /**
     * 获取对象的真实IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_REAL_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
