package com.example.antiplagiarism.controller;

import com.example.antiplagiarism.service.common.AuthService;
import com.example.antiplagiarism.service.database.ProfileService;
import com.example.antiplagiarism.service.database.TextTestService;
import com.example.antiplagiarism.service.model.ProfileDto;
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
public class ProfileController {

    private static final String PROFILE_PAGE_NAME = "profile";

    private final ProfileService profileService;
    private final AuthService authService;

    @GetMapping(value = "/" + PROFILE_PAGE_NAME)
    public ModelAndView getProfilePage(Model model, HttpServletRequest request) {
        try {
            String username = authService.getUserSessionBySessionId(request.getSession().getId()).getUsername();
            if (!authService.isUserLoggedIn(username)) {
                model.addAttribute(USER_ATTRIBUTE_KEY, new UserAuthDto());
                return AntiplagiarismUtil.buildMav(LOGIN_PAGE_NAME, model);
            }
            ProfileDto profileDto = profileService.findByUsername(username);
            model.addAttribute(PROFILE_ATTRIBUTE_KEY, profileDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute(USER_ATTRIBUTE_KEY, new UserAuthDto());
            return AntiplagiarismUtil.buildMav(LOGIN_PAGE_NAME, model);
        }
        return AntiplagiarismUtil.buildMav(PROFILE_PAGE_NAME, model);
    }

}
