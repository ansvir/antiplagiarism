package com.example.antiplagiarism.util;

import lombok.experimental.UtilityClass;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@UtilityClass
public class AntiplagiarismUtil {

    public static final String ALERT_ATTRIBUTE_KEY = "alertMessage";
    public static final String TEXT_TEST_ATTRIBUTE_KEY = "textTest";
    public static final String USER_ATTRIBUTE_KEY = "user";
    public static final String REMEMBER_ME_KEY = "remember-me";

    public static final String USER_ROLE = "USER";

    public static ModelAndView buildMav(String viewName, Model model) {
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addAllObjects(model.asMap());
        return modelAndView;
    }

}
