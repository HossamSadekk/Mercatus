package com.sideproject.mercatus.service;

import com.sideproject.mercatus.api.model.LoginBody;
import com.sideproject.mercatus.api.model.RegistrationBody;
import com.sideproject.mercatus.exceptions.MailFailureException;
import com.sideproject.mercatus.exceptions.UserAlreadyExistException;
import com.sideproject.mercatus.exceptions.UserNotVerifiedException;
import com.sideproject.mercatus.model.LocalUser;
import com.sideproject.mercatus.model.VerificationToken;
import com.sideproject.mercatus.model.dao.LocalUserDAO;
import com.sideproject.mercatus.model.dao.VerificationTokenDAO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    public LocalUserDAO localUserDAO;

    @Autowired
    private VerificationTokenDAO verificationTokenDAO;
    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private EmailService emailService;

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistException, MailFailureException {
        if (localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent() || localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExistException();
        }
        LocalUser localUser = new LocalUser();
        localUser.setUsername(registrationBody.getUsername());
        localUser.setEmail(registrationBody.getEmail());
        localUser.setPassword(encryptionService.hashPassword(registrationBody.getPassword()));
        localUser.setFirstName(registrationBody.getFirstName());
        localUser.setLastName(registrationBody.getLastName());
        VerificationToken verificationToken = createVerificationToken(localUser);
        emailService.sendVerificationEmail(verificationToken);
        return localUserDAO.save(localUser);
    }


    private VerificationToken createVerificationToken(LocalUser user) {
        VerificationToken token = new VerificationToken();
        token.setToken(jwtService.generateVerificationJWT(user));
        token.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        token.setLocalUser(user);
        user.getVerificationTokens().add(token);
        return token;
    }

    @Transactional
    public Boolean verifyEmail(String token) {
        Optional<VerificationToken> optToken = verificationTokenDAO.findByToken(token);
        if (optToken.isPresent()) {
            VerificationToken verificationToken = optToken.get();
            LocalUser localUser = verificationToken.getLocalUser();
            if (!localUser.isEmailVerified()) {
                localUser.setEmailVerified(true);
                localUserDAO.save(localUser);
                verificationTokenDAO.deleteByLocalUser(localUser);
                return true;
            }
        }
        return false;
    }

    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, MailFailureException {
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            if (encryptionService.checkPassword(loginBody.getPassword(), user.getPassword())) {
                if (user.isEmailVerified()) {
                    return jwtService.generateJWT(user);
                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.size() == 0 ||
                            verificationTokens
                                    .get(0)
                                    .getCreatedTimestamp()
                                    .before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    if (resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenDAO.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }
}
