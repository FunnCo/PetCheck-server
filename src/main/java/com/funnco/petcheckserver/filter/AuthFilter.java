package com.funnco.petcheckserver.filter;

import com.funnco.petcheckserver.utils.AuthUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Component
public class AuthFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthUtils authUtils;

    public AuthFilter(AuthUtils authUtils) {
        this.authUtils = authUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info(request.getRequestURI());

        String authDetails = request.getHeader("Authorization");
        if (isRequestWhitelisted(request, "GET") && authDetails == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if(authDetails == null && (request.getRequestURI().equals("/registration")
                || request.getRequestURI().split("/")[1].equals("accounts"))){
            filterChain.doFilter(request, response);
            return;
        }
        if(authDetails == null){
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "No auth details found");
            return;
        }
        authDetails = authDetails.substring(6);
        authDetails = authDetails.replace("=", "");

        try {
            boolean isUserAuthorized = authUtils.areUserDetailsValid(authDetails);

            if (request.getRequestURI().equals("/registration") && isUserAuthorized) {
                response.sendError(HttpStatus.FORBIDDEN.value(), "User is already authorized");
                return;
            }

            if (!isUserAuthorized) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Auth details are incorrect");
                return;
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            if (request.getRequestURI().equals("/registration")) {
                filterChain.doFilter(request, response);
            } else {
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal error happened: " + e.getLocalizedMessage()  + ",    " + Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private static final List<String> WHITELISTED_REQUESTS = List.of(
            "animals", "locations");

    private boolean isRequestWhitelisted(HttpServletRequest request, String method) {
        String searchedUri = request.getRequestURI().split("/")[1];
        logger.info("filtering " + searchedUri + "with method " + request.getMethod());
        return WHITELISTED_REQUESTS.contains(searchedUri) && request.getMethod().equals(method);
    }
}
