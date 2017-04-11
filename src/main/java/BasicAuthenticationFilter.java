import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebFilter({
    "/basic",
    "/basic/*",
    "/basic/**",
})
public class BasicAuthenticationFilter implements Filter {

    private static final Base64.Decoder DECODER = Base64.getDecoder();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            HttpServletRequest httpReq = (HttpServletRequest) req;
            HttpServletResponse httpResp = (HttpServletResponse) resp;
            boolean authenticated = isAuthenticated(httpReq);
            if (authenticated) {
                chain.doFilter(req, httpResp);
            } else {
                httpResp.setStatus(401);
                httpResp.setHeader("WWW-Authenticate", "Basic realm=\"User Visible Realm\"");
            }
        }
    }

    private boolean isAuthenticated(HttpServletRequest req) {
        String authorization = req.getHeader("Authorization");
        if (authorization == null) {
            return false;
        }
        String prefix = "Basic ";
        String encoded = authorization.substring(prefix.length());
        byte[] bytes = DECODER.decode(encoded);
        String decoded = new String(bytes, StandardCharsets.UTF_8);
        return "admin:admin".equals(decoded);
    }

    @Override
    public void destroy() {
    }
}
