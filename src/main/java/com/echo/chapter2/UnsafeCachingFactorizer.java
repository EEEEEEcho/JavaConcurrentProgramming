package com.echo.chapter2;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public class UnsafeCachingFactorizer implements Servlet {
    //AtomicReference代替对象引用的线程安全类
    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<>();


    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger i = extractFromRequest(servletRequest);
        if (i.equals(lastNumber.get())){
            encodeIntoResponse(servletResponse,lastFactors.get());
        }
        else{
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            encodeIntoResponse(servletResponse,factors);
        }
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

    private void encodeIntoResponse(ServletResponse servletResponse,BigInteger[] bigInteger){

    }
}
