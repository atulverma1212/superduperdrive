package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.exception.EntityNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    public Optional<Note> create(Note note) {
        noteMapper.insert(note);
        return Optional.of(note);
    }

    public Optional<Note> update(Note note) {
        noteMapper.update(note);
        return Optional.of(note);
    }

    public Optional<List<Note>> getAllUserNotes(int userid) {
        return Optional.of(noteMapper.findAllByUserId(userid));
    }

    public void delete(int noteId, int userId) {
        Optional<Note> userNote = getUserNote(noteId, userId);
        if (userNote.isEmpty()) {
            throw new EntityNotFoundException(Note.class, "noteId", noteId, "userId", userId);
        }
        noteMapper.deleteById(userNote.get().getId());
    }

    public Optional<Note> getUserNote(int id, int userId) {
        Optional<Note> userNote = noteMapper.findByIdAndUserId(id, userId);
        if (userNote.isEmpty()) {
            throw new EntityNotFoundException(Note.class, "noteId", id, "userId", userId);
        }
        return userNote;
    }
}
