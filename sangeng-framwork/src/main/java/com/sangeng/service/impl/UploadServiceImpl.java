package com.sangeng.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.sangeng.domain.ResponseResult;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.service.UploadService;
import com.sangeng.utils.PathUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@Data
public class UploadServiceImpl implements UploadService
{

    @Value("${oss.accessKey}")
    private String accessKey;

    @Value("${oss.secretKey}")
    private String secretKey;

    @Value("${oss.bucket}")
    private String bucket;

    @Value("${oss.testUrl}")
    private String testUrl;

    @Override
    public ResponseResult uploadImg(MultipartFile img)
    {
        // 判断文件类型 大小

        // 获取原始文件名
        String filename = img.getOriginalFilename();
        // 对原始文件名判断
        if (!filename.endsWith(".png"))
        {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        // 生成文件目录
        String filePath = PathUtils.generateFilePath(filename);

        // 判断通过添加到OSS

        String url = uploadOSS(img,filePath);

        return ResponseResult.okResult(url);
    }


    private String uploadOSS(MultipartFile imgFile, String filePath)
    {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try
        {
//            FileInputStream fileInputStream = new FileInputStream("D:\\三更草堂\\SGBlog (20)\\SGBlog\\笔记\\兔.png");
            InputStream inputStream = imgFile.getInputStream();

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try
            {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                // 返回外链链接
                return testUrl + key;
            }
            catch (QiniuException ex)
            {
                Response r = ex.response;
                System.err.println(r.toString());
                try
                {
                    System.err.println(r.bodyString());
                }
                catch (QiniuException ex2)
                {
                    //ignore
                }
            }
        }
        catch (Exception ex)
        {
            //ignore
        }
        return "ad";
    }
}
