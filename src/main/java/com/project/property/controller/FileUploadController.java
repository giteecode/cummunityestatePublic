package com.project.property.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.project.property.entity.ResultMessage;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 文件上传控制器
 * </p>
 *
 * @author Lizuxian
 * @create 2022/3/6 18:56
 */
@RestController
@RequestMapping("/api/file/upload")
public class FileUploadController {
    @PostMapping(value = "image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultMessage uploadImage(@RequestParam(value = "file") MultipartFile file) throws IOException {
        if (file == null) {
            return new ResultMessage(-1, "请求参数有误，图片不能为空！");
        }

        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + extension;

        String filePath = StrUtil.format("images{}{}{}{}", File.separator, DateUtil.format(new Date(), "yyyy-MM"), File.separator, fileName);

        FileUploadController.writeToFile(file.getBytes(), "resource.file" + File.separator + filePath);

        return new ResultMessage(0, filePath);
    }

    @PostMapping(value = "document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultMessage uploadDocument(@RequestParam(value = "file") MultipartFile file) throws IOException {
        if (file == null) {
            return new ResultMessage(-1, "请求参数有误，文档不能为空！");
        }

        String filePath = StrUtil.format("documents{}{}{}{}", File.separator, DateUtil.format(new Date(), "yyyy-MM"), File.separator, file.getOriginalFilename());

        FileUploadController.writeToFile(file.getBytes(), "resource.file" + File.separator + filePath);

        return new ResultMessage(0, filePath);
    }

    /**
     * <p>
     * 写入到文件
     * </P>
     *
     * @param fileData
     * @param fileName
     * @throws IOException
     */
    public static void writeToFile(byte[] fileData, String fileName) throws IOException {
        String resourcePath = ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).getPath(); // 资源绝对路径（根路径）
        resourcePath = URLDecoder.decode(resourcePath, StandardCharsets.UTF_8.name());  // 解码
        File file = new File(System.getProperty("user.dir") + File.separator + fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        FileOutputStream os = new FileOutputStream(file, false);

        os.write(fileData);
        os.flush();
        os.close();
    }
}
