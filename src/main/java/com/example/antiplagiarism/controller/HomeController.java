package com.example.antiplagiarism.controller;

import com.example.antiplagiarism.service.model.TextTestSubmitDto;
import com.example.antiplagiarism.util.AntiplagiarismThymeleafUtil;
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

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private static final String HOME_PAGE_NAME = "home";

    @GetMapping(value = "/" + HOME_PAGE_NAME)
    public ModelAndView getHomePage(Model model) {
        model.addAttribute("textTest", new TextTestSubmitDto());
        return AntiplagiarismThymeleafUtil.buildMav(HOME_PAGE_NAME, model);
    }

    @PostMapping(value = "/" + HOME_PAGE_NAME)
    public ModelAndView doTextTest(@ModelAttribute("textTest") @Valid TextTestSubmitDto textTestSubmitDto, Model model,
                                   Errors errors) {
        return AntiplagiarismThymeleafUtil.buildMav(HOME_PAGE_NAME, model);
    }

}
