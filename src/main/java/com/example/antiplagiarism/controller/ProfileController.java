package com.example.antiplagiarism.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.example.antiplagiarism.util.AntiplagiarismUtil;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProfileController {

    private static final String PROFILE_PAGE_NAME = "profile";

    @GetMapping(value = "/" + PROFILE_PAGE_NAME)
    public ModelAndView getProfilePage(Model model) {
        return AntiplagiarismUtil.buildMav(PROFILE_PAGE_NAME, model);
    }

}
