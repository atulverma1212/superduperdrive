package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.constants.Consts;
import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    @Autowired
    private CredentialMapper credentialMapper;
    @Autowired
    private HashService hashService;

    public void saveCredentials(Credentials credentials) {
        hashCreds(credentials);
        this.credentialMapper.insert(credentials);
    }

    public List<Credentials> getCredentialsByUserId(int userid) {
        return credentialMapper.findAllByUserId(userid);
    }

    public Credentials getCredentialsById(int id, int userid) {
        return credentialMapper.findByIdAndUserId(id, userid);
    }

    public Credentials createNew(Credentials credentials, User user) {
        hashCreds(credentials);
        if (credentials.getId() == 0) {
            credentials.setUser(user);
            credentialMapper.insert(credentials);
        } else {
            credentialMapper.update(credentials);
        }
        return credentials;
    }

    public Credentials getDefaultUserCredentials(int userid) {
        return credentialMapper.findDefaultByUserId(userid, Consts.DEFAULT_CREDS);
    }

    public void deleteCreds(int credsId, User user) {
        Credentials cred = credentialMapper.findByIdAndUserId(credsId, user.getUserId());
        if (cred.getUrl().matches(Consts.DEFAULT_CREDS)) {
            return;
        }
        credentialMapper.deleteById(credsId);
    }

    private void hashCreds(Credentials credentials) {
        String hashedPwd = hashService.getEncodedPwd(credentials.getPassword(), credentials.getKey());
        credentials.setPassword(hashedPwd);
    }
}
