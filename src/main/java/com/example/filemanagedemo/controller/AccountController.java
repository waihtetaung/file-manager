package com.example.filemanagedemo.controller;

import com.example.filemanagedemo.dto.AuthenticationRequest;
import com.example.filemanagedemo.dto.AuthenticationResponse;
import com.example.filemanagedemo.dto.RegisterRequest;
import com.example.filemanagedemo.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AuthenticationService authService;

    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError", false);
        return "login";
    }

    @GetMapping("/signup")
    public String signUp(Model model){
        model.addAttribute("registerRequest", new RegisterRequest());
        return "signup";
    }

//    @PostMapping("/signup")
//    public String saveUser(@ModelAttribute("registerRequest") RegisterRequest request, BindingResult result){
//        if(result.hasErrors()){
//            return "signup";
//        }
//        authService.register(request);
//        return "redirect:/";
//    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("authenticationRequest", new AuthenticationRequest());
        return "login";
    }

    @PostMapping("/authenticate")
    public String authenticateUser(@ModelAttribute("authenticationRequest") AuthenticationRequest request, BindingResult result, Model model, HttpServletResponse response){
        if(result.hasErrors()){
            return "login";
        }
        AuthenticationResponse authenticationResponse = authService.signIn(request, response);
        if(authenticationResponse.getToken() != null){
            return "redirect:/home";
        } else {
            model.addAttribute("loginError", true);
            return "login";
        }
    }

}



