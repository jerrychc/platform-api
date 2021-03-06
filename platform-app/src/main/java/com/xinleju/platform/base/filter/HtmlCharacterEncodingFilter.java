package com.xinleju.platform.base.filter;

import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Ly on 2017/8/1.
 */
public class HtmlCharacterEncodingFilter extends CharacterEncodingFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String url = request.getRequestURI();



        String encoding = getEncoding();
        if (encoding != null) {
            if (isForceRequestEncoding() || request.getCharacterEncoding() == null) {
                request.setCharacterEncoding(encoding);
            }
            if (isForceResponseEncoding()) {
                if (url.indexOf(".html") != -1) {
                    if (url.indexOf("/content/contentRowType/contentRowType_staticPage") != -1||url.indexOf("/officeFiles/") != -1) {
                        response.setCharacterEncoding("gb2312");
                    }
                }else
                response.setCharacterEncoding(encoding);
            }
        }
        filterChain.doFilter(request, response);
    }
}
