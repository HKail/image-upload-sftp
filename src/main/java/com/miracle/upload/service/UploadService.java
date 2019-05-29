package com.miracle.upload.service;

import com.miracle.upload.util.JSchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传业务处理类
 * Created at 2019-05-29 14:22:43
 * @author Allen
 */
@Service
public class UploadService {

    @Autowired
    private JSchUtil jSchUtil;

    // 状态码
    private static int SUCCESS = 300, FAIL = 400;

    /**
     * 图片上传
     * @param file 图片MultipartFile对象
     * @return Map<String, Object> {"code": 状态码, "msg"/"data": 额外信息/数据}
     */
    public Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        String url = "";
        try {
            url = jSchUtil.uploadFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (url == null || "".equals(url)) {
            result.put("code", FAIL);
            result.put("msg", "图片上传失败!");
        } else {
            result.put("code", SUCCESS);
            result.put("data", url);
        }
        return result;
    }
}
