package com.miracle.upload.controller;

import com.miracle.upload.service.UploadService;
import com.miracle.upload.util.JSchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 图片上传控制器
 * Created at 2019-05-28 23:39:48
 * @author Allen
 */
@Controller
@RestController
public class UploadController {

    @Autowired
    private UploadService service;

    @Autowired
    private JSchUtil jSchUtil;

    /**
     * 页面跳转 —— index.html
     * @return ModelAndView 模型视图对象
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    /**
     * 上传并页面跳转 —— cover.html
     * @param file MultipartFile图片对象
     * @return ModelAndView 模型视图对象
     */
    @RequestMapping(value = "cover", method = RequestMethod.POST)
    public ModelAndView cover(MultipartFile file) {
        String viewName = "cover";
        String imageUrl = "";
        try {
            imageUrl = jSchUtil.uploadFile(file);
        } catch (Exception e) {
            viewName = "index";
            e.printStackTrace();
        }
        return new ModelAndView(viewName).addObject("imageUrl", imageUrl);
    }

    /**
     * 图片上传
     * @param file 图片MultipartFile对象
     * @return Map<String, Object> {"code": 状态码, "msg"/"data": 额外信息/数据}
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(MultipartFile file) {
        return service.upload(file);
    }
}
