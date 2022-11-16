package com.VincenteCodeFellowship.codeFellowship.controller;

import com.VincenteCodeFellowship.codeFellowship.models.Post;
import com.VincenteCodeFellowship.codeFellowship.models.SiteUser;
import com.VincenteCodeFellowship.codeFellowship.repositories.PostRepository;
import com.VincenteCodeFellowship.codeFellowship.repositories.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Date;

@Controller
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    SiteUserRepository siteUserRepository;

    @PostMapping("/create-post")
    public RedirectView newPost(Model m, Principal p, String body) {
        if (p != null){
            String username = p.getName();
            SiteUser siteUser = (SiteUser) siteUserRepository.findByUsername(username);
            m.addAttribute("siteUser", siteUser);
            Post post = new Post(body);
            post.setCreatedAt(new Date());
            post.setSiteUser(siteUser);
            postRepository.save(post);
        }
        return new RedirectView("my-profile");
    }
}
