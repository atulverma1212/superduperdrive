package com.udacity.jwdnd.course1.cloudstorage.config;

import com.udacity.jwdnd.course1.cloudstorage.constants.Consts;
import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService  {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CredentialMapper credentialMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
       Optional<User> user = getUserByUsername(s);
       if (user.isEmpty())
           throw new UsernameNotFoundException(s);

        Credentials credentials = credentialMapper.findDefaultByUserId(user.get().getUserId(), Consts.DEFAULT_CREDS);
        if(credentials == null) {
            return null;
        }

        return new org.springframework.security.core.userdetails.User(
               user.get().getUsername(), credentials.getPassword(), true, true, true,
               true, new ArrayList<>());
    }

    public Optional<User> getUserByUsername(String s) {
        return Optional.ofNullable(userMapper.findByUsername(s));
    }
}
