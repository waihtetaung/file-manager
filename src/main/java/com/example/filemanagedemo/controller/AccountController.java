package com.example.filemanagedemo.controller;

import com.example.filemanagedemo.dto.AuthenticationRequest;
import com.example.filemanagedemo.dto.AuthenticationResponse;
import com.example.filemanagedemo.dto.RegisterRequest;
import com.example.filemanagedemo.service.AuthenticationService;
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
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/signup")
    public String signUp(Model model){
        model.addAttribute("registerRequest", new RegisterRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String saveUser(@ModelAttribute("registerRequest") RegisterRequest request, BindingResult result){
        if(result.hasErrors()){
            return "signup";
        }
        authService.register(request);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String logIn(@RequestBody AuthenticationRequest request, Model model) {
        AuthenticationResponse response = authService.signIn(request);
        if (response.getToken() != null) {
            return "redirect:/home";
        } else{
            model.addAttribute("loginError", true);
            return "login";
        }
    }
}
