package com.baoge.service;

import com.baoge.dao.order.OrderDao;
import com.baoge.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;

    public Order getOrder() {
        return orderDao.selectByPrimaryKey(101L);
    }

}
