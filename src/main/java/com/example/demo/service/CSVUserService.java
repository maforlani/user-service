package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface CSVUserService {
    void saveUsersFromCsv(MultipartFile csvFile);
}
