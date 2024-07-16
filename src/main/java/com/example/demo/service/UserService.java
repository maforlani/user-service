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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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

    public UserWeb getUserById(String id) {
        logger.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .map(user -> mapper.map(user, UserWeb.class))
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", id);
                    return new ResourceNotFoundException("User not found");
                });
    }

    public UserWeb getUserByFiscalCode(String fiscalCode) {
        logger.info("Fetching user with fiscalCode: {}", fiscalCode);
        return userRepository.findByFiscalCode(fiscalCode)
                .map(user -> mapper.map(user, UserWeb.class))
                .orElseThrow(() -> {
                    logger.error("User not found with fiscalCode: {}", fiscalCode);
                    return new ResourceNotFoundException("User not found");
                });
    }

    public UserWeb createUser(UserWeb user) {
        logger.info("Creating user: {}", user);
        if (user != null) {
            User userModel = userRepository.save(mapper.map(user, User.class));
            return mapper.map(userModel, UserWeb.class);
        } else {
            logger.error("User must not be null");
            throw new InconsistentDataException("User must not be null");
        }
    }

    public UserWeb updateUser(String id, UserWeb userDetails) {
        logger.info("Updating user: {}, with id: {}", userDetails, id);
        if (id != null && userDetails != null && id.equals(userDetails.getId())) {
            User updatedUser = userRepository.save(mapper.map(userDetails, User.class));
            return mapper.map(updatedUser, UserWeb.class);
        } else {
            logger.error("Inconsistent Data, userId: {}, User:{}", id, userDetails != null ? userDetails.getId() : "null");
            throw new InconsistentDataException(MessageFormat.format("Inconsistent Data, userId: {0}, User:{1}", id, userDetails != null ? userDetails.getId() : "null"));
        }
    }

    public void deleteUser(String id) {
        logger.info("Deleting user with id: {}", id);
        userRepository.findById(id)
                .ifPresentOrElse(user -> userRepository.deleteById(id), () -> {
                    logger.error("User not found with id: {}", id);
                    throw new ResourceNotFoundException("User not found");
                });
    }

    @Transactional
    public void saveUsersFromCsv(MultipartFile csvFile) {
        try (Reader reader = new InputStreamReader(csvFile.getInputStream());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withDelimiter(';')
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            List<User> users = StreamSupport.stream(csvParser.getRecords().spliterator(), false)
                    .map(this::mapCsvRecordToUser)
                    .collect(Collectors.toList());

            userRepository.saveAll(users);

        } catch (IOException e) {
            String errMsg = MessageFormat.format("Invalid File. Message {0}:", e.getMessage());
            logger.error(errMsg);
            throw new InconsistentDataException(errMsg);
        }
    }

    private User mapCsvRecordToUser(CSVRecord csvRecord) {
        User user = new User();
        logger.info("csvRecord: {}", csvRecord);
        setUserFields(user, csvRecord);
        return user;
    }

    private void setUserFields(User user, CSVRecord csvRecord) {
        user.setNome(csvRecord.get("nome"));
        user.setCognome(csvRecord.get("cognome"));
        user.setEmail(csvRecord.get("email"));
        user.setIndirizzo(csvRecord.get("indirizzo"));
        user.setFiscalCode(csvRecord.get("nome"));
    }

}

