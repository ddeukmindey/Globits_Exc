package com.globits.hr.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tika.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/public/hr/file")
public class RestHRPublicController {
    private static final Logger logger = LoggerFactory.getLogger(RestHRPublicController.class);
    @Autowired
    private Environment env;

    @RequestMapping(path = "/getImage/{filename}/{type}", method = RequestMethod.GET)
    public void getImage(HttpServletResponse response, @PathVariable String filename, @PathVariable String type) {
        try {
            String path = "";
            if (env.getProperty("hrm.file.folder") != null) {
                path = env.getProperty("hrm.file.folder");
            }
            File file = new File(path + filename + "." + type);
            String contentType = "application/octet-stream";
            response.setContentType(contentType);
            OutputStream out = response.getOutputStream();
            FileInputStream in = new FileInputStream(file);
            IOUtils.copy(in, out);
            out.close();
            in.close();
        } catch (Exception e){
            logger.error("Error get image: {}", e.getMessage(), e.getLocalizedMessage());
        }
    }
}
