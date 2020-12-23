package com.example.communication.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return null;
    }

    @GetMapping("/error")
    public String handleError() {
        return "error";
    }
}
