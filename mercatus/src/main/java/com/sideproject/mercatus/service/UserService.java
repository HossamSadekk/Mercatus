package com.sideproject.mercatus.service;

import com.sideproject.mercatus.api.model.RegistrationBody;
import com.sideproject.mercatus.exceptions.UserAlreadyExistException;
import com.sideproject.mercatus.model.LocalUser;
import com.sideproject.mercatus.model.dao.LocalUserDAO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    public LocalUserDAO localUserDAO;

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistException {
        if (localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent() || localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExistException();
        }
        LocalUser localUser = new LocalUser();
        localUser.setUsername(registrationBody.getUsername());
        localUser.setEmail(registrationBody.getEmail());
        localUser.setPassword(registrationBody.getPassword());
        localUser.setFirstName(registrationBody.getFirstName());
        localUser.setLastName(registrationBody.getLastName());
        return localUserDAO.save(localUser);
    }
}
