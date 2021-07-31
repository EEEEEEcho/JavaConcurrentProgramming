package com.echo.chapter2;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

public class SynchronizedFactorizer implements Servlet {
    private BigInteger lastNumber;
    private BigInteger[] lastFactors;


    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public synchronized void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger i = extractFromRequest(servletRequest);
        if (i.equals(lastNumber)){
            encodeIntoResponse(servletResponse,lastFactors);
        }
        else{
            BigInteger[] factor = factor(i);
            lastNumber = i;
            lastFactors = factor;
            encodeIntoResponse(servletResponse,factor);
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
