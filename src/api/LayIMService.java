package api;

import dao.LayIMDao;
import org.omg.PortableInterceptor.ACTIVE;
import pojo.result.JsonResult;
import util.LayIMFactory;
import util.log.LayIMLog;
import util.serializer.IJsonSerializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pz on 16/11/22.
 */
@WebServlet(name = "LayIMService",urlPatterns = "/api")
public class LayIMService extends HttpServlet {

    LayIMDao dao = new LayIMDao();
    IJsonSerializer serializer = LayIMFactory.createSerializer();

    //post请求
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    //get请求
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");

        //得到action类型，根据action来进行业务处理
        String action = request.getParameter("action");
        String userId = request.getParameter("id");

        int userIdInt = 0;
        if(userId != null){
            userIdInt = Integer.parseInt(userId);
        }
        JsonResult result = new JsonResult();
        switch (action) {
            case RequestAction.BASE:
                result = dao.getBaseList(userIdInt);
                break;
            case RequestAction.MEMBER:
                int groupId =Integer.parseInt(request.getParameter("id"));
                result = dao.getMemberList(groupId);
                break;
            default:
                break;
        }

        writeToClient(response,result);

    }

    private void writeToClient(HttpServletResponse response,JsonResult result) throws IOException {
        response.getWriter().write(serializer.toJSON(result));
    }
}
