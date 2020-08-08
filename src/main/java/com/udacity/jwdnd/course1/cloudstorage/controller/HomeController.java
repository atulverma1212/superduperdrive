package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private CredentialService credentialService;

    @GetMapping("/api/user/login")
    public ModelAndView getLoginPage(ModelAndView model) {
        User userDto = new User();
        model.addObject("user", userDto);
        model.setViewName("login");
        return model;
    }

    @GetMapping("/api/user/logout")
    public ModelAndView getLogoutPage(ModelAndView model) {
        User userDto = new User();
        model.addObject("user", userDto);
        model.addObject("logout", "logout");
        model.setViewName("login");
        return model;
    }

    @GetMapping("/api/user/home")
    public ModelAndView getHomePage(ModelAndView model, HttpSession session) {
        Optional<User> userDto = userService.findByUserName(session.getAttribute("user").toString());
        model.addObject("user", userDto.get());
        model.addObject("files", fileService.getAllUserFile(userDto.get().getUserId()));
        model.addObject("newNote", new Note());
        model.addObject("newCred", new Credentials());
        model.addObject("notes", noteService.getAllUserNotes(userDto.get().getUserId()).orElse(new ArrayList<>()));
        model.addObject("creds", credentialService.getCredentialsByUserId(userDto.get().getUserId()));
        model.setViewName("home");
        return model;
    }

    @GetMapping("/api/user/invalidCredentials")
    public ModelAndView redirectToInvalidCreds(ModelAndView model) {
        model.addObject("invalid", "invalid credentials");
        User userDto = new User();
        model.addObject("user", userDto);
        model.setViewName("login");
        return model;
    }
}
