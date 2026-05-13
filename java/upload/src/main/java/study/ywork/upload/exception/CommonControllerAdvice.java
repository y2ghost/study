package study.ywork.upload.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import study.ywork.upload.service.dto.ResultDTO;

@ControllerAdvice
public class CommonControllerAdvice implements ResponseBodyAdvice {
    private final Logger log = LoggerFactory.getLogger(CommonControllerAdvice.class);

    @ExceptionHandler
    @ResponseBody
    public <T> ResultDTO<T> exceptionHandler(Exception e) {
        log.error("exception handle {}", e.getMessage());
        return ResultDTO.error(e.getMessage());
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return body;
    }
}
