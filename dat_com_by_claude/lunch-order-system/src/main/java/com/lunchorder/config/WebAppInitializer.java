package com.lunchorder.config;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{ApplicationConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        // Cấu hình multipart file upload
        String uploadLocation = System.getProperty("java.io.tmpdir");
        long maxFileSize = 10485760L; // 10MB
        long maxRequestSize = 20971520L; // 20MB
        int fileSizeThreshold = 5242880; // 5MB

        MultipartConfigElement multipartConfig = new MultipartConfigElement(
                uploadLocation,
                maxFileSize,
                maxRequestSize,
                fileSizeThreshold
        );

        registration.setMultipartConfig(multipartConfig);
    }
}