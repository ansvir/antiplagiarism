package com.example.antiplagiarism.controller;

import com.example.antiplagiarism.service.model.TextTestSubmitDto;
import com.example.antiplagiarism.util.AntiplagiarismUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static com.example.antiplagiarism.util.AntiplagiarismUtil.TEXT_TEST_ATTRIBUTE_KEY;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private static final String HOME_PAGE_NAME = "home";

    @GetMapping(value = "/" + HOME_PAGE_NAME)
    public ModelAndView getHomePage(Model model) {
        model.addAttribute(TEXT_TEST_ATTRIBUTE_KEY, new TextTestSubmitDto());
        return AntiplagiarismUtil.buildMav(HOME_PAGE_NAME, model);
    }

    @PostMapping(value = "/" + HOME_PAGE_NAME)
    public ModelAndView doTextTest(@ModelAttribute(TEXT_TEST_ATTRIBUTE_KEY) @Valid TextTestSubmitDto textTestSubmitDto, Model model) {
        return AntiplagiarismUtil.buildMav(HOME_PAGE_NAME, model);
    }

}
