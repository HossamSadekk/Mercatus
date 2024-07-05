package com.sideproject.mercatus.model.dao;

import com.sideproject.mercatus.model.LocalUser;
import com.sideproject.mercatus.model.WebOrder;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WebOrderDAO extends ListCrudRepository<WebOrder, Long> {
    List<WebOrder> findByUser(LocalUser user);

}
