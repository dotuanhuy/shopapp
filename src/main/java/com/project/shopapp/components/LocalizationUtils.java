package com.project.shopapp.components;

import com.project.shopapp.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocalizationUtils {
    private final MessageSource messageSource;  // lấy message từ các file message properties (.properties)
    private final LocaleResolver localeResolver;    // Dựa các yếu tố như header của request, tham số query string, hoặc cookie

    public String getLocalizedMessage(String messageKey, Object ... params) {
        HttpServletRequest request = WebUtils.getCurrentRequest();
        Locale locale = localeResolver.resolveLocale(request);  // lấy thông tin từ request của người dùng (headers: Accept-Language)
        return messageSource.getMessage(messageKey, params, locale);
    }
}
