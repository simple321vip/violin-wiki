package cn.violin.wiki.handler;

import cn.violin.core.interceptor.GlobalExceptionHandler;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Wiki service global exception handler; extends the {@code core} template.
 */
@RestControllerAdvice
public class ViolinWikiExceptionHandler extends GlobalExceptionHandler {

    public ViolinWikiExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }
}