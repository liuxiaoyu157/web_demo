package org.example.ddd.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.xmlbeans.XmlException;
import org.example.ddd.service.TService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private TService tService;

    @GetMapping("/world")
    public String helloWorld(){
        // 1.定义要跳转的页面，2.添加要共享的数据
        log.info("hello world");
        return "hello world 123";
    }

    @GetMapping("/readWord1")
    public String readWord1() throws IOException {
        return tService.readWord1();
    }

    @GetMapping("/readWord2")
    public String readWord2() throws IOException, InvalidFormatException {
        return tService.readWord2();
    }

    @GetMapping("/readWord3")
    public String readWord3() throws IOException, XmlException {
        return tService.readWord3();
    }

    @GetMapping("/readPhoto")
    public String readPhoto() throws IOException {
        return tService.readPhoto();
    }

    @GetMapping("/readExcel")
    public String readExcel() throws IOException {
        return tService.readExcel();
    }

    @GetMapping("/test1")
    public String test1() throws IOException {
        return "test1";
    }
}
