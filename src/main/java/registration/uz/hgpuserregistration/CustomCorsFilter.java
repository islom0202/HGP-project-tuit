//package registration.uz.hgpuserregistration;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class CustomCorsFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) res;
//        HttpServletRequest request = (HttpServletRequest) req;
//
//        // Allow only your specific origin
//        response.setHeader("Access-Control-Allow-Origin", "https://high-gas-protection.vercel.app");
//
//        // Allow credentials
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//
//        // Specify allowed methods and headers
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
//        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With");
//
//        // If it's a preflight request (OPTIONS), handle it and return status 200
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//        } else {
//            chain.doFilter(req, res);
//        }
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // You can initialize any resources here if needed
//    }
//
//    @Override
//    public void destroy() {
//        // Clean up any resources here if needed
//    }
//}