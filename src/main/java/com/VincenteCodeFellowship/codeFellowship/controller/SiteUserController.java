package com.VincenteCodeFellowship.codeFellowship.controller;

import com.VincenteCodeFellowship.codeFellowship.models.SiteUser;
import com.VincenteCodeFellowship.codeFellowship.repositories.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
        return new RedirectView("profile-user");
        String hashedPW = passwordEncoder.encode(password);
        SiteUser newUser = new SiteUser(username, hashedPW, firstName, lastName, dateOfBirth, bio);
        siteUserRepo.save(newUser);
        authWithHttpServletRequest(username, password);
        return new RedirectView("profile-user");
    }

    public void authWithHttpServletRequest(String username, String password){
        try{
            request.login(username, password);
        }catch(ServletException e){
            e.printStackTrace();
        }
    }

    @GetMapping("/my-profile")
    public String getMyProfile(Principal p, Model m){
        if(p != null){
            SiteUser siteUser = (SiteUser) siteUserRepo.findByUsername(p.getName());
            m.addAttribute("siteUser", siteUser);
        }
        return "my-profile";
    }

    @GetMapping("/users/{id}")
    public String getUserInfo(Model m, Principal p, @PathVariable Long id){
        SiteUser userToLookAt = siteUserRepo.getReferenceById(id);
        if (p != null) {
            SiteUser currentUser = siteUserRepo.findByUsername(p.getName());
            if (currentUser.getId() != userToLookAt.getId()){
                m.addAttribute("username", userToLookAt.getUsername());
                m.addAttribute("firstName", userToLookAt.getFirstName());
                m.addAttribute("lastName", userToLookAt.getLastName());
                m.addAttribute("bio", userToLookAt.getBio());
            }
        else {
                m.addAttribute("username", currentUser.getUsername());
                m.addAttribute("firstName", currentUser.getFirstName());
                m.addAttribute("lastName", currentUser.getLastName());
                m.addAttribute("bio", currentUser.getBio());
                m.addAttribute("dateOfBirth", currentUser.getDateOfBirth());
        }
        }

        return "profile-user";
    }

//    @PutMapping("/users/{id}")
//    public RedirectView editUserInfo(String post, Long userId) throws ServletException {
//        SiteUser user = siteUserRepo.findById(userId).orElseThrow();
//            user.setPost(post);
//            siteUserRepo.save(user);
//        return new RedirectView("/users/" + userId);
//    }

}

