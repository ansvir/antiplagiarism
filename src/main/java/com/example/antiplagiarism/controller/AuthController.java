package com.example.antiplagiarism.controller;

import com.example.antiplagiarism.service.common.AuthService;
import com.example.antiplagiarism.service.database.UserService;
import com.example.antiplagiarism.service.model.TextTestSubmitDto;
import com.example.antiplagiarism.service.model.UserAuthDto;
import com.example.antiplagiarism.service.model.UserDto;
import com.example.antiplagiarism.util.AntiplagiarismUtil;
import lombok.RequiredArgsConstructor;
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
import java.util.ArrayList;
import java.util.List;

import static com.example.antiplagiarism.util.AntiplagiarismUtil.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    public static final String LOGIN_PAGE_NAME = "login";
    public static final String SIGNUP_PAGE_NAME = "signup";
    public static final String ERROR_PAGE_NAME = "error";
    public static final String HOME_PAGE_NAME = "home";

    private final UserService userService;
    private final AuthService authService;

    @GetMapping(value = {"/", "/" + LOGIN_PAGE_NAME})
    public ModelAndView getLoginPage(Model model) {
        model.addAttribute(USER_ATTRIBUTE_KEY, new UserAuthDto());
        return AntiplagiarismUtil.buildMav(LOGIN_PAGE_NAME, model);
    }

    @GetMapping(value = "/" + SIGNUP_PAGE_NAME)
    public ModelAndView getSignupPage(Model model) {
        model.addAttribute(USER_ATTRIBUTE_KEY, new UserAuthDto());
        return AntiplagiarismUtil.buildMav(SIGNUP_PAGE_NAME, model);
    }

    @PostMapping(value = "/" + LOGIN_PAGE_NAME)
    public ModelAndView doLogin(@Valid @ModelAttribute(USER_ATTRIBUTE_KEY) UserAuthDto userAuthDto,
                                BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return AntiplagiarismUtil.buildMav(LOGIN_PAGE_NAME, model);
        }
        UserDto userDto = userService.checkUserExists(userAuthDto);
        if (userDto != null) {
            authService.loginUser(userDto.getUsername(), request.getSession(true).getId());
            model.addAttribute(TEXT_TEST_SUBMIT_ATTRIBUTE_KEY, new TextTestSubmitDto());
            return AntiplagiarismUtil.buildMav(HOME_PAGE_NAME, model);
        }
        model.addAttribute(ALERT_ATTRIBUTE_KEY, List.of("No such user! Check your credentials!"));
        return AntiplagiarismUtil.buildMav(LOGIN_PAGE_NAME, model);
    }

    @PostMapping(value = "/" + SIGNUP_PAGE_NAME)
    public ModelAndView doSignUp(@Valid @ModelAttribute(USER_ATTRIBUTE_KEY) UserAuthDto userAuthDto,
                                 BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return AntiplagiarismUtil.buildMav(SIGNUP_PAGE_NAME, model);
        }
        UserDto userDto = userService.checkUserExists(userAuthDto);
        if (userDto != null) {
            model.addAttribute(ALERT_ATTRIBUTE_KEY, List.of("Such user already exist!"));
            return AntiplagiarismUtil.buildMav(SIGNUP_PAGE_NAME, model);
        }
        UserDto newUser = userService.save(new UserDto(userAuthDto.getUsername(), userAuthDto.getPassword(),
                USER_ROLE, new ArrayList<>(), true));
        authService.loginUser(newUser.getUsername(), request.getSession(true).getId());
        model.addAttribute(TEXT_TEST_SUBMIT_ATTRIBUTE_KEY, new TextTestSubmitDto());
        return AntiplagiarismUtil.buildMav(HOME_PAGE_NAME, model);
    }

    @GetMapping("/logout")
    public ModelAndView doLogout(Model model, HttpServletRequest request) {
        try {
            AuthService.UserSession userSession = authService.getUserSessionBySessionId(request.getSession().getId());
            authService.logoutUser(userSession.getUsername());
        } catch (EntityNotFoundException e) {
            model.addAttribute(ALERT_ATTRIBUTE_KEY, List.of("Cannot logout! Error occurred!"));
            model.addAttribute("statusCode", 500);
            model.addAttribute("errorMessage", "Cannot logout, please try again later!");
            return AntiplagiarismUtil.buildMav(ERROR_PAGE_NAME, model);
        }
        model.addAttribute(USER_ATTRIBUTE_KEY, new UserAuthDto());
        return AntiplagiarismUtil.buildMav(LOGIN_PAGE_NAME, model);
    }

}
