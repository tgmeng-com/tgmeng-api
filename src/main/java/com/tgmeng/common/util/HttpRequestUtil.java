package com.tgmeng.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@UtilityClass
public class HttpRequestUtil {
    // 获取当前请求的url全路径
    public String getRequestUrl() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String url = "";
        if (request != null) {
            url = request.getRequestURL().toString();
        }
        return url;
    }

    // 获取路径（不含域名）
    public String getRequestPath() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String path = "";
        if (request != null) {
            path = request.getRequestURI();
        }
        return path;
    }

    // 获取路径里面的最后一个单词
    public String getRequestPathLastWord() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String path = "";
        if (request != null) {
            path = request.getRequestURI(); // 获取 /xxx/yyy/zzz
        }
        // 去掉末尾可能的 "/"
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        // 取最后一段
        String lastSegment = "";
        if (!path.isEmpty()) {
            int lastSlash = path.lastIndexOf("/");
            lastSegment = path.substring(lastSlash + 1); // 取最后一个单词
        }
        return lastSegment;
    }

    // 获取路径+参数（不含域名）
    public String getRequestPathWithQuery() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String path = "";
        if (request != null) {
            path = request.getRequestURI();
            String queryString = request.getQueryString();
            if (queryString != null && !queryString.isEmpty()) {
                path += "?" + queryString;
            }
        }
        return path;
    }

    // 获取域名
    public String getRequestDomain() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String domain = "";
        if (request != null) {
            domain = request.getScheme() + "://" + request.getServerName();
            if (request.getServerPort() != 80 && request.getServerPort() != 443) {
                domain += ":" + request.getServerPort();
            }
        }
        return domain;
    }

    public String getRequestHeader(String headerName) {
        // 获取当前请求的 HttpServletRequest
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader(headerName);  // 获取指定请求头
    }

    // 获取请求的Origin
    public String getRequestOrigin(String url) {
        try {
            // origin = scheme + "://" + host + (port)
            return url.split("://")[0] + "://" + url.split("://")[1].split("/")[0];
        } catch (Exception e) {
            throw new RuntimeException("URL解析失败: " + url, e);
        }
    }

    // 获取请求的Referer
    public String getRequestReferer(String url) {
        return getRequestOrigin(url) + "/";
    }

    public String getRequestRandomUserAgent() {
        return UserAgentGeneratorUtil.generateRandomUserAgent();
    }


}
