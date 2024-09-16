package com.project.shopapp.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// RequestContextHolder: cho phép truy cập đến request hiện tại (HTTP request) trong ngữ cảnh thread (luồng) hiện tại
/*
   - ServletRequestAttributes là một lớp con của RequestAttributes, chuyên dùng để làm việc với các thuộc tính của HTTP Servlet request.
    - Nó cung cấp các phương thức để lấy ra đối tượng HttpServletRequest và HttpServletResponse của request hiện tại.
*/

public class WebUtils {
    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}
