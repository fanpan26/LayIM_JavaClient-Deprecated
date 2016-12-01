package api;

import util.upload.FileUploadHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pz on 16/11/30.
 */
@WebServlet(name = "UploadServlet",urlPatterns = "/upload")
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FileUploadHelper helper = new FileUploadHelper();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String result =  helper.upload(request);

        response.getWriter().append(result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
