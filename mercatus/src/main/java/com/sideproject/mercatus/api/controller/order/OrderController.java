package com.sideproject.mercatus.api.controller.order;

import com.sideproject.mercatus.model.LocalUser;
import com.sideproject.mercatus.model.WebOrder;
import com.sideproject.mercatus.service.OrderService;
import com.sideproject.mercatus.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping
    public List<WebOrder> getAllOrders(@AuthenticationPrincipal LocalUser user) {
        return orderService.getOrders(user);
    }
}
