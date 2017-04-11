import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@WebServlet({
    "/basic",
    "/basic/*",
    "/basic/**",
})
public class BasicAuthenticationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        if ("/basic".equals(path)) {
            resp.sendRedirect("/basic/index");
        } else {
            ServletContext sc = req.getServletContext();
            Path p;
            if ("/basic/index".equals(path) || "/basic/link".equals(path)) {
                p = Paths.get(sc.getRealPath(path + ".html"));
            } else {
                p = Paths.get(sc.getRealPath("/basic/error.html"));
            }
            Files.copy(p, resp.getOutputStream());
        }
    }
}
