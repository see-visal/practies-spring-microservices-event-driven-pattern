package com.see.visal.itp_indentity.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // If a user lands on the IAM root (e.g. after a direct login),
        // redirect them back to the main BFF Gateway application.
        return "redirect:http://localhost:10000";
    }
}