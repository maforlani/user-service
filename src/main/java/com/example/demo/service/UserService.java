package com.example.demo.service;

import com.example.demo.web.data.UserWeb;

import java.util.List;

public interface UserService {
    List<UserWeb> getAllUsers();
    UserWeb getUserById(String id);
    UserWeb getUserByFiscalCode(String fiscalCode);
    UserWeb createUser(UserWeb user);
    UserWeb updateUser(String id, UserWeb userDetails);
    void deleteUser(String id);
}
