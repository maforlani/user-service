package com.example.demo.service;

import com.example.demo.exception.InconsistentDataException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.web.data.UserWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Component("userService")
public class UserServiceProxy implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceProxy.class);

    @Autowired
    UserService userServiceImpl;

    @Override
    public List<UserWeb> getAllUsers() {
        return userServiceImpl.getAllUsers();
    }
    //caso dritto

    @Override
    public UserWeb getUserById(String id) {
        checkParameter(id);
        return Optional.ofNullable(userServiceImpl.getUserById(id))
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }
    //caso dritto
    //caso in cui id è null
    //caso in cui id è vuoto
    //caso in cui il service torna null

    @Override
    public UserWeb getUserByFiscalCode(String fiscalCode) {
        checkParameter(fiscalCode);
        return Optional.ofNullable(userServiceImpl.getUserByFiscalCode(fiscalCode))
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }
    //caso dritto
    //caso in cui id è null
    //caso in cui id è vuoto
    //caso in cui il service torna null

    @Override
    public UserWeb createUser(UserWeb user) {
        logger.info("check user validation");
        if (user == null) {
            throw new InconsistentDataException("User must not be null");
        } else if(userServiceImpl.getUserById(user.getId()) == null && userServiceImpl.getUserByFiscalCode(user.getFiscalCode()) == null) {
            throw new UserAlreadyExistsException("user already present");
        }
        return userServiceImpl.createUser(user);
    }
    //caso dritto
    //caso in cui user è null
    //user gia presente per id e cf

    @Override
    public UserWeb updateUser(String id, UserWeb userDetails) {
        if (id == null || !id.trim().equalsIgnoreCase("") || userDetails != null || id.equals(userDetails.getId())) {
            logger.error("Inconsistent Data, userId: {}, User:{}", id, userDetails != null ? userDetails.getId() : "null");
            throw new InconsistentDataException(MessageFormat.format("Inconsistent Data, userId: {0}, User:{1}", id, userDetails != null ? userDetails.getId() : "null"));
        } else if (!notDuplicate(id, userDetails)) {
            logger.error("User already exist");
            throw new UserAlreadyExistsException("User already exist");
        }
        return userServiceImpl.updateUser(id, userDetails);
    }

    private boolean notDuplicate(String id, UserWeb userDetails) {
        UserWeb userWebForId = userServiceImpl.getUserById(id);
        UserWeb userWebForFC = userServiceImpl.getUserByFiscalCode(userDetails.getFiscalCode());
        return userWebForId != null && (userWebForFC == null || userWebForFC.getId().equalsIgnoreCase(id));
    }
    //caso dritto
    //caso in cui id è null
    //caso in cui id è vuoto
    //caso in cui id è compilato e userDetail è null
    //caso in cui id è compilato e userDetail è compilato e id è diverso da userDetailId
    //caso in cui service ritorna null
    //caso in cui l utenza non esiste non fa l'update
    // caso in cui l utenza esiste ma provo a cambiare fc con uno gia esistente

    @Override
    public void deleteUser(String id) {
        checkParameter(id);
        if (userServiceImpl.getUserById(id) == null) {
            throw new InconsistentDataException("User not found");
        }
        userServiceImpl.deleteUser(id);
    }
    //caso dritto
    //caso in cui id è null
    //caso in cui id è vuoto

    @Override
    public void saveUsersFromCsv(MultipartFile csvFile) {
        Optional.ofNullable(csvFile)
                .orElseThrow(() -> new InconsistentDataException("csv file not present"));
        userServiceImpl.saveUsersFromCsv(csvFile);
    }
    //caso dritto
    //caso in cui csv è null

    private static void checkParameter(String parameter) {
        Optional.ofNullable(parameter)
                .filter(data -> !data.trim().equalsIgnoreCase(""))
                .orElseThrow(() -> new InconsistentDataException("id cannot empty or null"));
    }
}
