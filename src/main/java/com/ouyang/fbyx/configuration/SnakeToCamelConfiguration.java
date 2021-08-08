/*
 * Copyright (c) 2010-2018 www.co-mall.com/ Inc. All rights reserved.
 * 注意：本内容仅限于北京科码先锋互联网技术股份有限公司内部传阅，禁止外泄以及用于其他商业目的。
 */

package com.ouyang.fbyx.configuration;

import com.google.common.base.CaseFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求参数从下划线命名转换成驼峰命名
 * @author wanchuanlai Email:wanchuanlai@co-mall.com
 * @since 2018/6/27 18:12
 */
@Configuration
public class SnakeToCamelConfiguration {
    @Bean
    public OncePerRequestFilter snakeCaseConverterFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                final Map<String, String[]> parameters = new ConcurrentHashMap<>(request.getParameterMap().size());
                for (String param : request.getParameterMap().keySet()) {
                    String camelCaseParam = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, param);
                    parameters.put(camelCaseParam, request.getParameterValues(param));
                    parameters.put(param, request.getParameterValues(param));
                }
                filterChain.doFilter(new HttpServletRequestWrapper(request) {
                    @Override
                    public String getParameter(String name) {
                        return parameters.containsKey(name) ? parameters.get(name)[0] : null;
                    }

                    @Override
                    public Enumeration<String> getParameterNames() {
                        return Collections.enumeration(parameters.keySet());
                    }

                    @Override
                    public String[] getParameterValues(String name) {
                        return parameters.get(name);
                    }

                    @Override
                    public Map<String, String[]> getParameterMap() {
                        return parameters;
                    }
                }, response);
            }
        };
    }
}
