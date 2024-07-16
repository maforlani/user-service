package com.example.demo.service;

import com.example.demo.connector.db.user.UserRepository;
import com.example.demo.exception.InconsistentDataException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.web.data.UserWeb;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private ModelMapper mapper = new ModelMapper();
    @Autowired
    private UserRepository userRepository;

    public List<UserWeb> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll().stream()
            .filter(Objects::nonNull)
            .map(user -> mapper.map(user, UserWeb.class))
            .collect(Collectors.toList());
    }

    @Override
    public UserWeb getUserById(String id) {
        logger.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .map(data -> mapper.map(data, UserWeb.class))
                .orElse(null);
    }

    @Override
    public UserWeb getUserByFiscalCode(String fiscalCode) {
        logger.info("Fetching user with fiscalCode: {}", fiscalCode);
        return userRepository.findByFiscalCode(fiscalCode)
                .map(user -> mapper.map(user, UserWeb.class))
                .orElse(null);
    }

    @Override
    public UserWeb createUser(UserWeb user) {
        logger.info("Creating user: {}", user);
        User userModel = userRepository.save(mapper.map(user, User.class));
        return mapper.map(userModel, UserWeb.class);
    }

    @Override
    public UserWeb updateUser(String id, UserWeb userDetails) {
        logger.info("Updating user: {}, with id: {}", userDetails, id);
        User updatedUser = userRepository.save(mapper.map(userDetails, User.class));
        return mapper.map(updatedUser, UserWeb.class);
    }

    @Override
    public void deleteUser(String id) {
        logger.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }

}

