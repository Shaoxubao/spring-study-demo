package com.baoge.dao.order;

import com.baoge.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDao {
    Order selectByPrimaryKey(Long id);
}
