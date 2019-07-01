package cn.shuwei.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    /**
     * 文件上传
     * @param multipartFile
     * @param path
     * @return
     */
    String upload(MultipartFile multipartFile);
}
