package com.example.demo.service;

import com.example.demo.connector.db.user.UserRepository;
import com.example.demo.model.User;
import com.example.demo.web.data.UserWeb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        userService.getAllUsers();
        verify(userRepository, times(1)).findAll();
    }
    //torna una lista con 3 oggetti pieni e 2 oggetti null
    //torna una lista con soli 3 oggetti null
    //torna una lista empty

    @Test
    public void testGetUserById() {
        User user = new User();
        UserWeb userWeb = new UserWeb();
        when(userRepository.findById("123")).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserWeb.class)).thenReturn(userWeb);
        userService.getUserById("123");
        verify(userRepository, times(1)).findById(anyString());
    }
    //torna null
    //il mapper torna null

    @Test
    public void testGetUserByFiscalCode() {
        User user = new User();
        UserWeb userWeb = new UserWeb();
        when(userRepository.findByFiscalCode(anyString())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserWeb.class)).thenReturn(userWeb);

        userService.getUserByFiscalCode("ABC");
        verify(userRepository, times(1)).findByFiscalCode(anyString());
    }
    //torna un oggetto null
    //il mapper torna null

    //caso dritto
    @Test
    public void testCreateUser() {
        User user = new User();
        UserWeb userWeb = new UserWeb();
        when(userRepository.save(any())).thenReturn(user);
        userService.createUser(userWeb);
        verify(userRepository, times(1)).save(any());
    }
//il mapper torna null
    // il save torna null

//caso dritto
    @Test
    public void testUpdateUser() {
        User user = new User();
        UserWeb userWeb = new UserWeb();
        userWeb.setId("123");
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.updateUser("123", userWeb);
        verify(userRepository, times(1)).save(any());
    }

    //il mapper torna null
    //torna null

    //caso dritto
    @Test
    public void testDeleteUser() {
        User user = new User();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        userService.deleteUser("123");
        verify(userRepository, times(1)).deleteById(anyString());
    }

}
