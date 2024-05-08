package com.example.antiplagiarism.controller;

import com.example.antiplagiarism.service.database.UserService;
import com.example.antiplagiarism.service.model.TextTestSubmitDto;
import com.example.antiplagiarism.service.model.UserAuthDto;
import com.example.antiplagiarism.service.model.UserDto;
import com.example.antiplagiarism.util.AntiplagiarismUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

import static com.example.antiplagiarism.util.AntiplagiarismUtil.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private static final String LOGIN_PAGE_NAME = "login";
    private static final String SIGNUP_PAGE_NAME = "signup";
    private static final String HOME_PAGE_NAME = "home";

    private final UserService userService;

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
    public ModelAndView doLogin(@ModelAttribute(USER_ATTRIBUTE_KEY) @Valid UserAuthDto userAuthDto, Model model) {
        UserDto userDto = userService.checkUserExists(userAuthDto);
        if (userDto != null) {
            setCurrentUser(userDto);
            model.addAttribute(TEXT_TEST_ATTRIBUTE_KEY, new TextTestSubmitDto());
            return AntiplagiarismUtil.buildMav(HOME_PAGE_NAME, model);
        }
        model.addAttribute(ALERT_ATTRIBUTE_KEY, List.of("No such user! Check your credentials!"));
        return AntiplagiarismUtil.buildMav(LOGIN_PAGE_NAME, model);
    }

    @PostMapping(value = "/" + SIGNUP_PAGE_NAME)
    public ModelAndView doSignUp(@ModelAttribute(USER_ATTRIBUTE_KEY) @Valid UserAuthDto userAuthDto, Model model) {
        UserDto userDto = userService.checkUserExists(userAuthDto);
        if (userDto != null) {
            model.addAttribute(ALERT_ATTRIBUTE_KEY, List.of("Such user already exist!"));
            return AntiplagiarismUtil.buildMav(SIGNUP_PAGE_NAME, model);
        }
        UserDto newUser = userService.save(new UserDto(userAuthDto.getUsername(), userAuthDto.getPassword(),
                List.of(new SimpleGrantedAuthority(USER_ROLE))));
        setCurrentUser(newUser);
        model.addAttribute(TEXT_TEST_ATTRIBUTE_KEY, new TextTestSubmitDto());
        return AntiplagiarismUtil.buildMav(HOME_PAGE_NAME, model);
    }

    private void setCurrentUser(UserDto user) {
        Authentication authentication = new RememberMeAuthenticationToken(REMEMBER_ME_KEY, user, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
