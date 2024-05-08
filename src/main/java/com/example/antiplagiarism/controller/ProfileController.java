package com.example.antiplagiarism.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.example.antiplagiarism.service.database.ProfileService;
import com.example.antiplagiarism.util.AntiplagiarismThymeleafUtil;

import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProfileController {

    private static final String PROFILE_PAGE_NAME = "profile";

    @GetMapping(value = "/" + PROFILE_PAGE_NAME)
    public ModelAndView getMainPage(Model model) {
        return AntiplagiarismThymeleafUtil.buildMav(PROFILE_PAGE_NAME, model);
    }

}
