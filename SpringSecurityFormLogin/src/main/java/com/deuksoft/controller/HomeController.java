package com.deuksoft.controller;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class HomeController {
    @GetMapping("/")
    public String home(Model model){

        model.addAttribute("msg", "hello");
        log.info("dsfdsfdsf");
        return "index";
    }
}
