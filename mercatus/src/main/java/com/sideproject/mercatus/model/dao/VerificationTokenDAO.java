package com.sideproject.mercatus.model.dao;

import com.sideproject.mercatus.model.LocalUser;
import com.sideproject.mercatus.model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByLocalUser(LocalUser localUser);
}
