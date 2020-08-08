package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.exception.EntityNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ModelAndView createNote(HttpSession session, @ModelAttribute("newNote") Note note, ModelAndView model) {
        Optional<User> user = userService.findByUserName(session.getAttribute("user").toString());
        if (note.getId() == 0) {
            note.setUser(user.get());
            noteService.create(note);
        } else {
            noteService.update(note);
        }
        model.setViewName("result");
        model.addObject("success", "success");
        return model;
    }

    @GetMapping("/")
    public ResponseEntity<List<Note>> getAllNotes(@RequestParam("userId") int userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(User.class, "userId", userId);
        }
        return ResponseEntity.of(noteService.getAllUserNotes(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getUserNotes(@RequestParam("userId") int userId, @PathVariable("id") int noteId) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(User.class, "userId", userId);
        }
        return ResponseEntity.of(noteService.getUserNote(noteId, userId));
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteNote(HttpSession session, @PathVariable("id") int noteId, ModelAndView model) {
        Optional<User> user = userService.findByUserName(session.getAttribute("user").toString());
        noteService.delete(noteId, user.get().getUserId());
        model.setViewName("result");
        model.addObject("success", "success");
        return model;
    }

}
