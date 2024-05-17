package com.example.antiplagiarism.controller;

import com.example.antiplagiarism.service.common.AuthService;
import com.example.antiplagiarism.service.database.TextTestService;
import com.example.antiplagiarism.service.model.TextTestDto;
import com.example.antiplagiarism.service.model.TextTestSubmitDto;
import com.example.antiplagiarism.service.model.UserAuthDto;
import com.example.antiplagiarism.util.AntiplagiarismUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.example.antiplagiarism.controller.AuthController.LOGIN_PAGE_NAME;
import static com.example.antiplagiarism.util.AntiplagiarismUtil.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private static final String HOME_PAGE_NAME = "home";

    private final TextTestService textTestService;
    private final AuthService authService;

    @GetMapping(value = "/" + HOME_PAGE_NAME)
    public ModelAndView getHomePage(Model model, HttpServletRequest request) {
        try {
            String username = authService.getUserSessionBySessionId(request.getSession().getId()).getUsername();
            if (!authService.isUserLoggedIn(username)) {
                model.addAttribute(USER_ATTRIBUTE_KEY, new UserAuthDto());
                return AntiplagiarismUtil.buildMav(LOGIN_PAGE_NAME, model);
            }
        } catch (EntityNotFoundException e) {
            model.addAttribute(USER_ATTRIBUTE_KEY, new UserAuthDto());
            return AntiplagiarismUtil.buildMav(LOGIN_PAGE_NAME, model);
        }
        model.addAttribute(TEXT_TEST_SUBMIT_ATTRIBUTE_KEY, new TextTestSubmitDto());
        return AntiplagiarismUtil.buildMav(HOME_PAGE_NAME, model);
    }

    @PostMapping(value = "/" + HOME_PAGE_NAME)
    public ModelAndView doTextTest(@Valid @ModelAttribute(TEXT_TEST_SUBMIT_ATTRIBUTE_KEY) TextTestSubmitDto textTestSubmitDto,
                                   BindingResult bindingResult, Model model, HttpServletRequest request) {
        try {
            String username = authService.getUserSessionBySessionId(request.getSession().getId()).getUsername();
            if (!authService.isUserLoggedIn(username)) {
                model.addAttribute(USER_ATTRIBUTE_KEY, new UserAuthDto());
                return AntiplagiarismUtil.buildMav(LOGIN_PAGE_NAME, model);
            }
            TextTestDto textTestDto = new TextTestDto();
            textTestDto.setTextOne(textTestSubmitDto.getTextOne());
            textTestDto.setTextTwo(textTestSubmitDto.getTextTwo());
            model.addAttribute(TEXT_TEST_ATTRIBUTE_KEY, textTestService.doTextTestAndSaveResult(textTestDto, username));
            model.addAttribute(TEXT_TEST_SUBMIT_ATTRIBUTE_KEY, textTestSubmitDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute(USER_ATTRIBUTE_KEY, new UserAuthDto());
            return AntiplagiarismUtil.buildMav(LOGIN_PAGE_NAME, model);
        }
        if (bindingResult.hasErrors()) {
            return AntiplagiarismUtil.buildMav(HOME_PAGE_NAME, model);
        }
        return AntiplagiarismUtil.buildMav(HOME_PAGE_NAME, model);
    }

}
