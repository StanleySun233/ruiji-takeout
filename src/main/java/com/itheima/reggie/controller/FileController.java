package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${reggie.filepath}")
    private String filePath;

    @PostMapping("/upload")
    public R<String> uploadFile(MultipartFile file) throws IOException {
        String originFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + originFileName.substring(originFileName.lastIndexOf("."));
        log.info(originFileName + fileName);

        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        file.transferTo(new File(filePath + fileName));

        return R.success(fileName);

    }

    @GetMapping("/download")
    public void downloadFile(String name, HttpServletResponse httpServletResponse, OutputStream outputStream) throws IOException {
        log.info(name);
        FileInputStream fileInputStream = new FileInputStream(new File(filePath + name));
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        httpServletResponse.setContentType("image/jpeg");
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = fileInputStream.read(bytes)) != -1) {
            servletOutputStream.write(bytes, 0, len);
            servletOutputStream.flush();
        }
        servletOutputStream.close();
        fileInputStream.close();

    }

    public void deleteFile(String fileName) {
        File file = new File(filePath + fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
