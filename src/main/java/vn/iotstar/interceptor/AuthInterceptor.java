package vn.iotstar.interceptor;


import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.iotstar.entity.User;


// chặn yêu cầu không hợp lệ ở giai đoạn sớm 
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        String uri = request.getRequestURI();

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        if (uri.startsWith("/admin") && !user.isAdmin()) {
            response.sendRedirect("/login?error=access_denied");
            return false;
        }

        return true;
    }
}