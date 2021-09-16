package com.sprout.web.file;

import com.sprout.common.file.FileManager;
import com.sprout.web.base.BaseController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/file")
public class FileController extends BaseController {

    private static final long GB = 1024*1024*1024;

    @RequestMapping(value = "view")
    public String view(HttpServletRequest request, HttpServletResponse response) {
        FileSystem fs = FileManager.getDefaultFileSystem();
        for (FileStore store: fs.getFileStores()) {
            try {
                long total = store.getTotalSpace() / GB;
                long used = (store.getTotalSpace() - store.getUnallocatedSpace()) / GB;
                long avail = store.getUsableSpace() / GB;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        request.setAttribute("fileStores", fs.getFileStores());
        List<Path> pathList = new ArrayList<>();
        fs.getRootDirectories().forEach(path -> {
            pathList.add(path);
        });
        request.setAttribute("pathList", pathList);
        return "file/fileList";
    }


    /**
     * 下载文件或者文件夹，如果是文件夹则先压缩再下载，下载完成后删除压缩后的文件。
     * @param parentPath 文件或文件夹所在父目录
     * @param fileName 文件名称
     * @param request
     * @param response
     */
    @RequestMapping(value = "/downLoadPath")
    public ResponseEntity<byte[]> downLoadPath(String parentPath, String fileName, HttpServletRequest request,
                                               HttpServletResponse response) {
        String path = parentPath + FileManager.FILE_SEPARATOR + fileName;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            if (Files.isDirectory(Paths.get(path))) { //如果是文件夹则先压缩再下载
                Path p = FileManager.toZIP(path);
                headers.setContentDispositionFormData("attachment", p.getFileName().toString());
                return new ResponseEntity<byte[]>(Files.readAllBytes(p),
                        headers, HttpStatus.OK);
            } else { //如果是文件则直接下载
                headers.setContentDispositionFormData("attachment", fileName);
                return new ResponseEntity<byte[]>(Files.readAllBytes(Paths.get(path)),
                        headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            List<Charset> acceptableCharsets = new ArrayList<>();
            acceptableCharsets.add(Charset.forName("iso-8859-1"));
            headers.setAcceptCharset(acceptableCharsets );
            return new ResponseEntity<byte[]>("download error!".getBytes(),
                    headers, HttpStatus.OK);
        }
    }

    /**
     * 文件预览
     * @param path 文件路径信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/previewPath")
    public ResponseEntity<byte[]> previewPath(String path, HttpServletRequest request,
                                              HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentType(MediaType.valueOf(Files.probeContentType(Paths.get(path))));
            return new ResponseEntity<byte[]>(Files.readAllBytes(Paths.get(path)),
                    headers, HttpStatus.OK);
        } catch (Exception e) {
            headers.setContentType(MediaType.TEXT_HTML);
            String message = e.getMessage();
            message = "该文件不支持预览";
            try {
                return new ResponseEntity<byte[]>(Files.readAllBytes(Paths.get(path)),
                        headers, HttpStatus.OK);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }
}
