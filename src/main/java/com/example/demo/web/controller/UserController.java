package com.example.demo.web.controller;

import com.example.demo.service.CSVUserService;
import com.example.demo.service.UserService;
import com.example.demo.web.data.UserWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private UserService userService;
    private CSVUserService csvUserService;

    @Autowired
    public UserController(UserService userService, CSVUserService csvUserService) {
        this.userService = userService;
        this.csvUserService = csvUserService;
    }

    @GetMapping
    public ResponseEntity<List<UserWeb>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public UserWeb getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping("/fiscalCode")
    public UserWeb getUserByFiscalCode(@RequestParam String fiscalCode) {
        return userService.getUserByFiscalCode(fiscalCode);
    }

    @PostMapping
    public UserWeb createUser(@RequestBody UserWeb user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public UserWeb updateUser(@PathVariable String id, @RequestBody UserWeb userDetails) {
        return userService.updateUser(id, userDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadUsersFromCsv(@RequestParam("file") MultipartFile file) {
        log.info("multiPartFIle: {}",file);
        csvUserService.saveUsersFromCsv(file);
        return ResponseEntity.ok().build();
    }
}
