package cn.shuwei.service.impl;

import cn.shuwei.common.ServerResponse;
import cn.shuwei.utils.PropertiesUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import cn.shuwei.service.IFileService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        // 扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 文件名字
        String uploadName = PropertiesUtil.getProperty("oss.dirName") + "/" + UUID.randomUUID().toString() + "." + fileExtensionName;
        try {
            InputStream inputStream = multipartFile.getInputStream();
            OSSClient client = new OSSClient(
                    PropertiesUtil.getProperty("oss.endpoint"),
                    PropertiesUtil.getProperty("oss.accessKeyId"),
                    PropertiesUtil.getProperty("oss.accessKeySecret"));
            PutObjectResult result = client.putObject(PropertiesUtil.getProperty("oss.bucketName"), uploadName, inputStream);
            client.shutdown();
            return uploadName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
