package com.example.antiplagiarism.util;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

public class AntiplagiarismThymeleafUtil {

    public static ModelAndView buildMav(String viewName, Model model) {
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addAllObjects(model.asMap());
        return modelAndView;
    }

}
