package com.VincenteCodeFellowship.codeFellowship.controller;

import com.VincenteCodeFellowship.codeFellowship.models.SiteUser;
import com.VincenteCodeFellowship.codeFellowship.repositories.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;

@Controller
public class SiteUserController {

    @Autowired
    SiteUserRepository siteUserRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest request;

    @GetMapping("/")
    public String getHomePage(Model m, Principal p){
        if (p != null){
            String username = p.getName();
            SiteUser foundUser = siteUserRepo.findByUsername(username);

            m.addAttribute("username", username);
            m.addAttribute("firstName", foundUser.getFirstName());
        }
        return "index";
    }

    @GetMapping("login")
    public String getLoginPage(Model m, Principal p){
        if (p != null){
            SiteUser siteUser = siteUserRepo
                    .findByUsername(p.getName());
            m.addAttribute("username", p.getName());
        }
        return "login";
    }

    @GetMapping("/signup")
    public String getSignupPage(){
        return "signup";
    }

    @PostMapping("/signup")
    public RedirectView createUser(String username, String password, String firstName, String lastName, Date dateOfBirth, String bio){
        if(siteUserRepo.findByUsername(username) != null)
        return new RedirectView("/");
        String hashedPW = passwordEncoder.encode(password);
        SiteUser newUser = new SiteUser(username, hashedPW, firstName, lastName, dateOfBirth, bio);
        siteUserRepo.save(newUser);
        authWithHttpServletRequest(username, password);
        return new RedirectView("/");
    }

    public void authWithHttpServletRequest(String username, String password){
        try{
            request.login(username, password);
        }catch(ServletException e){
            e.printStackTrace();
        }
    }


}

