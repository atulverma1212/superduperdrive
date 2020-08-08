package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public ModelAndView showRegistrationForm(ModelAndView model) {
        User userDto = new User();
        model.addObject("user", userDto);
        model.setViewName("signup");
        return model;
    }

    @PostMapping("/signup")
    public ModelAndView registerUserAccount
            (ModelAndView modelAndView, @Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        Optional<User> existingUser = userService.findByUserName(user.getUsername());
        modelAndView.addObject("user", new User());
        modelAndView.setViewName("signup");
        if (existingUser.isPresent()) {
            modelAndView.addObject("error","Username already exists");
            modelAndView.setStatus(HttpStatus.NOT_ACCEPTABLE);
            modelAndView.addObject("user", new User());
            bindingResult.reject("username");
            return modelAndView;
        }
        Optional<User> user1 = userService.createUser(user);
        modelAndView.addObject("success", "User created with Id: " + user1.get().getUserId());
        return modelAndView;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.of(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") int id) {
        return ResponseEntity.of(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        Optional<User> user = userService.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(user.get());
        return ResponseEntity.ok().build();
    }

}
