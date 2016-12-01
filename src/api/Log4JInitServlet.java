package api;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by pz on 16/11/23.
 */
@WebServlet(name = "Log4JInitServlet")

public class Log4JInitServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        String log4jLocation = config.getInitParameter("log4j-properties-location");

        ServletContext sc = config.getServletContext();

        if (log4jLocation == null) {
            BasicConfigurator.configure();
        } else {
            String webAppPath = sc.getRealPath("/");
            String log4jProp = webAppPath + log4jLocation;
            File yoMamaYesThisSaysYoMama = new File(log4jProp);
            if (yoMamaYesThisSaysYoMama.exists()) {
                PropertyConfigurator.configure(log4jProp);
            } else {
                BasicConfigurator.configure();
            }
        }
        super.init(config);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
