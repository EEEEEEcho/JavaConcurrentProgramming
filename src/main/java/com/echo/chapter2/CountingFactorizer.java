package com.echo.chapter2;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CountingFactorizer implements Servlet {
    //java内置的原子值。
    private final AtomicLong count = new AtomicLong(0);

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger num = extractFromRequest(servletRequest);
        BigInteger[] factors = factor(num);
        //原子操作，增加计数器的值
        count.incrementAndGet();
        encodeIntoResponse(factors,servletResponse);
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }

    private BigInteger extractFromRequest(ServletRequest servletRequest){
        return new BigInteger(String.valueOf(100));
    }

    private BigInteger[] factor(BigInteger integer){
        return new BigInteger[3];
    }

    private void encodeIntoResponse(BigInteger[] bigInteger,ServletResponse servletResponse){

    }
}
