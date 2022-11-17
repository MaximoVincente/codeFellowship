package com.VincenteCodeFellowship.codeFellowship.controller;

import com.VincenteCodeFellowship.codeFellowship.models.Post;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @GetMapping("/my-profile")
    public String getMyProfile(Principal p, Model m){
        if(p != null){
            SiteUser siteUser = (SiteUser) siteUserRepo.findByUsername(p.getName());
            m.addAttribute("siteUser", siteUser);
        }
        return "my-profile";
    }

    @GetMapping("/users/{id}")
    public String getUserInfo(Model m, Principal p, @PathVariable Long id) {
            SiteUser siteUser = (SiteUser) siteUserRepo.findByUsername(p.getName());
            m.addAttribute("siteUser", siteUser);
            SiteUser userToLookAt = siteUserRepo.findById(id).orElseThrow();
                m.addAttribute("usersIFollow", userToLookAt.getUsersIFollow());
                m.addAttribute("usersWhoFollowMe", userToLookAt.getUsersWhoFollowMe());
                m.addAttribute("username", userToLookAt.getUsername());
                m.addAttribute("firstName", userToLookAt.getLastName());
                m.addAttribute("bio", userToLookAt.getBio());
                m.addAttribute("id", userToLookAt.getId());
            List<Post> posts = new ArrayList<>();
        for (SiteUser user : siteUser.getUsersWhoFollowMe()) {
            posts.addAll(user.getPostMsg());
            m.addAttribute("posts", posts);
        }

        return "profile-user";
    }

    @PutMapping("/users/{id}")
    public RedirectView editUserInfo(@PathVariable Long id, Principal p, Model m) {
        if (p != null) {
            SiteUser siteUser = siteUserRepo.findByUsername(p.getName());
            SiteUser userToLookAt = siteUserRepo.findById(id).orElseThrow();
            if (siteUser != null && userToLookAt != null) {
                m.addAttribute("siteUser", siteUser);
                siteUserRepo.save(siteUser);
            }
        }
        return new RedirectView("/users/" + id);
    }

    @PutMapping("/follow-user/{id}")
    public RedirectView followUser(Principal p, @PathVariable Long id){
        SiteUser userToFollow = siteUserRepo.findById(id).orElseThrow(() -> new RuntimeException("Error getting data for Id: " + id));
        SiteUser browsingUser = siteUserRepo.findByUsername(p.getName());

        if(browsingUser.getUsername().equals(userToFollow.getUsername())){
            throw new IllegalArgumentException("Following yourself is a bad idea!");
        }
        browsingUser.getUsersIFollow().add(userToFollow);
        siteUserRepo.save(browsingUser);
        // save to db
        return new RedirectView("/users/" + id);
    }

    @GetMapping("/users")
    public String getAllUsersPage(Principal p, Model m) {
        if (p != null) {
            SiteUser siteUser = siteUserRepo.findByUsername(p.getName());
            m.addAttribute("applicationUser", siteUser);
        }
        List<SiteUser> users = siteUserRepo.findAll();
        m.addAttribute("users", users);
        return "users.html";
    }

    @GetMapping("/feed")
    public String getMyFeed(Principal p, Model m) {
        if (p != null) {
            SiteUser siteUser = siteUserRepo.findByUsername(p.getName());
            m.addAttribute("siteUser", siteUser);
            List<Post> usersPosts = new ArrayList<>();
            for (SiteUser user : siteUser.getUsersWhoFollowMe()) {
                usersPosts.addAll(user.getPostMsg());
            }
            m.addAttribute("usersPosts", usersPosts);
        }
        return "feed.html";
    }

}

