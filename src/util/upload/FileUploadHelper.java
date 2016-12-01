package util.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import pojo.result.JsonResult;
import pojo.result.JsonResultHelper;
import pojo.result.UploadFileResult;
import pojo.result.UploadImgResult;
import util.LayIMFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by pz on 16/11/29.
 */
public class FileUploadHelper {

    //上传文件
    public String upload(HttpServletRequest request) throws IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        @SuppressWarnings("deprecation")
        String path = request.getRealPath("/upload");//设置磁盘缓冲路径
        System.out.println(path);
        factory.setRepository(new File(path));
        factory.setSizeThreshold(1024*1024);//设置创建缓冲大小

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(-1);//设置上传文件限制大小,-1无上限
        String fileSrc = "";
        String oldFileName = "";
        try {
            @SuppressWarnings("unchecked")
            List<FileItem> list = upload.parseRequest(request);
            String va = null;

            for(FileItem item : list){
                if(item.isFormField()){//判断是否是文件流
                }else {

                    String[] fileNames = getNewFileName(item);
                    //生成日期文件夹，新的文件名
                    String savePath =  createFile(path);
                    //保存
                    item.write(new File(path + "/" + savePath, fileNames[0]));

                    fileSrc = "/layim/upload/"+savePath+"/"+fileNames[0];
                    oldFileName = fileNames[1];
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        //如果是上传成功
        if (fileSrc.length()>0) {

            Object obj = null;
            String t = request.getParameter("t");
            if (t == null) {
                t = "img";
            }
            if (t.equals("img")) {
                UploadImgResult imgResult = new UploadImgResult();
                imgResult.setSrc(fileSrc);
                obj = imgResult;
            } else {
                UploadFileResult fileResult = new UploadFileResult();
                fileResult.setSrc(fileSrc);
                fileResult.setName(oldFileName);
                obj = fileResult;
            }

            JsonResult result = JsonResultHelper.createSuccessResult(obj);
            return LayIMFactory.createSerializer().toJSON(result);
        }else{
            //上传失败，提示
            JsonResult result = JsonResultHelper.createFailedResult("文件上传失败");
            return LayIMFactory.createSerializer().toJSON(result);
        }

    }

    private String[] getNewFileName(FileItem item){
        String value = item.getName();//会将完整路径名传过来
        int start = value.lastIndexOf("\\");
        String fileName = value.substring(start + 1);
        System.out.println(fileName);
        String[] extension = fileName.split("\\.");
        String newFileName = UUID.randomUUID()+"."+extension[1];
        return new String[]{ newFileName,fileName};
    }

    private String createFile(String path){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String savePath = format.format(date);
        //重新生成文件夹
        File file = new File(path+"/"+savePath);
        if(!file.exists()) {
            file.mkdirs();
        }
        return savePath;
    }
}
