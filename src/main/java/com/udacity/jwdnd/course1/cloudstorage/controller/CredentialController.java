package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.constants.Consts;
import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/api/creds")
public class CredentialController {

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private UserService userService;

    @DeleteMapping("/{id}")
    public void deleteNote(HttpSession session, @PathVariable("id") int credsId) {
        Optional<User> userDto = userService.findByUserName(session.getAttribute("user").toString());
        credentialService.deleteCreds(credsId, userDto.get());
    }

    @PostMapping("/")
    public ModelAndView createCreds(HttpSession session, @ModelAttribute("newCred") Credentials credentials, ModelAndView model) {
        Optional<User> user = userService.findByUserName(session.getAttribute("user").toString());
        if (credentials.getId() != 0) {
            Credentials defaultUserCredentials = credentialService.getCredentialsById(credentials.getId(), user.get().getUserId());
            if (defaultUserCredentials.getUrl().matches(Consts.DEFAULT_CREDS)
                    && (!defaultUserCredentials.getUrl().matches(credentials.getUrl())
                    || !defaultUserCredentials.getUsername().matches(credentials.getUsername()))) {
                model.addObject("error", "url or username of cloudstorage user credentials can not be updated");
                model.setViewName("result");
                return model;
            }
        } else if (credentials.getUrl().matches(Consts.DEFAULT_CREDS)) {
            model.addObject("error", "There can only be one default credentials for cloudstorage");
            model.setViewName("result");
            return model;
        }
        credentials.setKey(credentials.getUsername());
        credentials = credentialService.createNew(credentials, user.get());
        model.setViewName("result");
        model.addObject("success", "Credentials for " + user.get().getUsername()
                + " created/updated. Credential Id: " + credentials.getId());
        return model;
    }
}
