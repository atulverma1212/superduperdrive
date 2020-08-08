package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.UploadFileResponse;
import com.udacity.jwdnd.course1.cloudstorage.entity.DbFile;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.exception.EntityNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @PostMapping("/uploadFile")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file, HttpSession session, ModelAndView model) {
        Optional<User> user = userService.findByUserName(session.getAttribute("user").toString());
        if (user.isEmpty()) {
            throw new EntityNotFoundException(User.class);
        }
        if (fileService.doFileExists(StringUtils.cleanPath(file.getOriginalFilename()), user.get().getUserId())) {
            model.addObject("error", "Filename: " + file.getOriginalFilename() + " already exists");
            model.setViewName("result");
            return model;
        }
        try {
            DbFile dbFile = fileService.store(file, user.get());
            model.setViewName("result");
            model.addObject("success", "File: " + dbFile.getName() + " stored with id: " + dbFile.getId());
            return model;
        } catch (Exception ex) {
            model.addObject("error", ex.getClass().getSimpleName() + " occured while storing the file");
            model.setViewName("result");
            return model;
        }
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int fileId, HttpSession session) {
        Optional<User> user = userService.findByUserName(session.getAttribute("user").toString());
        if (user.isEmpty()) {
            throw new EntityNotFoundException(User.class, "userId", user.get().getUserId());
        }
        DbFile dbFile = fileService.getFile(fileId, user.get().getUserId());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @DeleteMapping("/{fileId}")
    public void deleteFile(@PathVariable int fileId) {
        fileService.deleteFile(fileId);
    }
}
