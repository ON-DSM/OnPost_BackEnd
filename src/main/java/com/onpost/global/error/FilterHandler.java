package com.onpost.global.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onpost.global.error.exception.MasterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(0)
@Slf4j
@Component
@RequiredArgsConstructor
public class FilterHandler extends OncePerRequestFilter {

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (MasterException e) {
            response.setStatus(e.getErrorCode().getStatus());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ErrorResponse result = new ErrorResponse(e.getErrorCode());
            String json = mapper.writeValueAsString(result);
            log.error(json);
            response.getWriter().write(json);
        }
    }
}
