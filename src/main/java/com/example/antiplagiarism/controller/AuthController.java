package com.example.antiplagiarism.controller;

import com.example.antiplagiarism.service.database.UserService;
import com.example.antiplagiarism.service.model.UserAuthDto;
import com.example.antiplagiarism.service.model.UserDto;
import com.example.antiplagiarism.util.AntiplagiarismThymeleafUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private static final String LOGIN_PAGE_NAME = "login";
    private static final String SIGNUP_PAGE_NAME = "signup";
    private static final String HOME_PAGE_NAME = "home";
    private static final String ALERT_ATTRIBUTE_KEY = "alertAttr";

    private final UserService userService;

    @GetMapping(value = {"/", "/" + LOGIN_PAGE_NAME})
    public ModelAndView getLoginPage(Model model) {
        model.addAttribute("user", new UserAuthDto());
        return AntiplagiarismThymeleafUtil.buildMav(LOGIN_PAGE_NAME, model);
    }

    @GetMapping(value = "/" + SIGNUP_PAGE_NAME)
    public ModelAndView getSignupPage(Model model) {
        model.addAttribute("user", new UserAuthDto());
        return AntiplagiarismThymeleafUtil.buildMav(SIGNUP_PAGE_NAME, model);
    }

    @PostMapping(value = "/" + LOGIN_PAGE_NAME)
    public ModelAndView doLogin(@ModelAttribute("user") @Valid UserAuthDto userAuthDto, Model model) {
        if (userService.checkUserExists(userAuthDto)) {
            return AntiplagiarismThymeleafUtil.buildMav(HOME_PAGE_NAME, model);
        }
        model.addAttribute(ALERT_ATTRIBUTE_KEY, List.of("No such user! Check your credentials!"));
        return AntiplagiarismThymeleafUtil.buildMav(LOGIN_PAGE_NAME, model);
    }

    @PostMapping(value = "/" + SIGNUP_PAGE_NAME)
    public ModelAndView doSignUp(@ModelAttribute("user") @Valid UserAuthDto userAuthDto, Model model) {
        if (userService.checkUserExists(userAuthDto)) {
            model.addAttribute(ALERT_ATTRIBUTE_KEY, List.of("Such user already exist!"));
            return AntiplagiarismThymeleafUtil.buildMav(SIGNUP_PAGE_NAME, model);
        }
        UserDto newUser = userService.save(new UserDto(userAuthDto.getUsername(), userAuthDto.getPassword(),
                List.of(new SimpleGrantedAuthority("USER"))));
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, newUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return AntiplagiarismThymeleafUtil.buildMav(HOME_PAGE_NAME, model);
    }

}
