package com.sideproject.mercatus.service;

import com.sideproject.mercatus.api.model.LoginBody;
import com.sideproject.mercatus.api.model.RegistrationBody;
import com.sideproject.mercatus.exceptions.UserAlreadyExistException;
import com.sideproject.mercatus.model.LocalUser;
import com.sideproject.mercatus.model.dao.LocalUserDAO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    public LocalUserDAO localUserDAO;
    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private JWTService jwtService;

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistException {
        if (localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent() || localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExistException();
        }
        LocalUser localUser = new LocalUser();
        localUser.setUsername(registrationBody.getUsername());
        localUser.setEmail(registrationBody.getEmail());
        localUser.setPassword(encryptionService.hashPassword(registrationBody.getPassword()));
        localUser.setFirstName(registrationBody.getFirstName());
        localUser.setLastName(registrationBody.getLastName());
        return localUserDAO.save(localUser);
    }

    public String loginUser(LoginBody loginBody) {
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            if (encryptionService.checkPassword(loginBody.getPassword(), user.getPassword())) {
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }
}
