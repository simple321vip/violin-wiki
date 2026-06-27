package cn.violin.wiki.handler;

import cn.violin.core.interceptor.GlobalExceptionHandler;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Wiki 服务专属全局异常处理器：继承 core 的公共模板。
 */
@RestControllerAdvice
public class ViolinWikiExceptionHandler extends GlobalExceptionHandler {

    public ViolinWikiExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }
}