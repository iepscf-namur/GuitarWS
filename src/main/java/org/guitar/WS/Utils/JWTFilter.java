package org.guitar.WS.Utils;

import org.guitar.DAO.Utils.JWTUtils;
import io.jsonwebtoken.Claims;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class JWTFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (request instanceof HttpServletRequest) {

            //Method made to check headers data
            System.out.println(getHeadersInfo(httpRequest));

            //Check type of Request Method
            //Used to skip token checking in case of OPTION Method commonly used in Preflight requests
            if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
                ((HttpServletResponse)response).setStatus(HttpServletResponse.SC_OK);
                chain.doFilter(request,response);
            } else {
                String url;
                url = httpRequest.getRequestURI();
                System.out.println(httpRequest.getRequestURI());
                
                //List of URLs that require a valid token
                if (Stream.of("" +
                        "/login",
                        "/users",
                        "/catalog"
                        ).anyMatch(url::equalsIgnoreCase)) {

                    //Getting Authorization in header
                    String stringToken = httpRequest.getHeader("Authorization");
                    System.out.println(httpRequest.getHeader("Authorization"));

                    //We should test if null, but Header seems to never be
                    if (stringToken == null || stringToken.indexOf("Bearer") == -1) {
                        System.out.println("User Not Authorized : Authorization header not found");
                        throw new ServletException("User Not Authorized : Authorization header not found");
                    }

                    //Getting the token part
                    String[] token = stringToken.split("(?<=Bearer) ");

                    //Decoding token
                    Claims claims = null;
                    claims = JWTUtils.decodeJWT(token[1]);

                    //Checking validity
                    Boolean validity = null;
                    if (claims != null) {
                        validity = JWTUtils.isJWTValid(claims);
                    }
                    if (claims == null || !validity){
                        System.out.println("User Not Authorized : Token not valid or corrupted");
                        throw new ServletException("User Not Authorized : Token not valid or corrupted");
                    }

                    chain.doFilter(request,response);
                }
                else {
                    chain.doFilter(request,response);
                }
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {
    	
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

}
