package com.sp.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogFilter implements Filter {
    
    private Logger logger = LoggerFactory.getLogger(LogFilter.class);
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
            String given_url = "";
            if (request instanceof HttpServletRequest) {
                given_url = ((HttpServletRequest)request).getRequestURI();
            }
            logger.info("Hello from: "+ request.getLocalAddr() +" url asked:"+given_url);
            
            System.out.println("I AM THE FILTER !");
            chain.doFilter(request, response);
    }


}
