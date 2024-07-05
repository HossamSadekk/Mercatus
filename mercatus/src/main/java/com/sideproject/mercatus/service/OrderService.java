package com.sideproject.mercatus.service;

import com.sideproject.mercatus.model.LocalUser;
import com.sideproject.mercatus.model.WebOrder;
import com.sideproject.mercatus.model.dao.WebOrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    WebOrderDAO orderDAO;

    public List<WebOrder> getOrders(LocalUser user) {
        return orderDAO.findByUser(user);
    }
}
