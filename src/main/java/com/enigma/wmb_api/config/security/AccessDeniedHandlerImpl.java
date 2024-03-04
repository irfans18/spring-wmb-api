package com.enigma.wmb_api.config.security;

import com.enigma.wmb_api.model.response.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements org.springframework.security.web.access.AccessDeniedHandler {
    private final ObjectMapper mapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        CommonResponse<String> commonResponse = CommonResponse.<String>builder()
                .statusCode(HttpServletResponse.SC_FORBIDDEN)
                .message("Forbidden")
                .build();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        String s = mapper.writeValueAsString(commonResponse);
        response.getWriter().write(s);
    }
}
