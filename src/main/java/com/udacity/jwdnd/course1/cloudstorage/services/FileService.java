package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.DbFile;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.exception.EntityNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exception.FileStorageException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    private FileMapper fileMapper;

    public DbFile store(MultipartFile file, User user) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        if(filename.contains("..")) {
            throw new FileStorageException("Invalid filename: " + filename);
        }

        DbFile dbFile = new DbFile();
        try {
            dbFile.setData(file.getBytes());
            dbFile.setName(filename);
            dbFile.setSize(String.valueOf(file.getSize()));
            dbFile.setUser(user);
            dbFile.setContentType(file.getContentType());

            fileMapper.insert(dbFile);
        } catch (IOException e) {
            throw new FileStorageException("Exception while savinf file", e);
        }
        return dbFile;
    }

    public DbFile getFile(int fileId, int userid) {
        return fileMapper.findByIdAndUserId(fileId, userid)
                .orElseThrow(() -> new EntityNotFoundException(File.class, "fileId", fileId, "userId", userid));
    }

    public List<DbFile> getAllUserFile(int userid) {
        return fileMapper.findAllUserFiles(userid);
    }

    public Boolean doFileExists(String name, int userid) {
        Optional<DbFile> file = fileMapper.findByNameAndUserId(name, userid);
        return file.isPresent();
    }

    public void deleteFile(int fileId) {
        fileMapper.deleteById(fileId);
    }
}
