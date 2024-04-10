package com.baoge.service.impl;

import com.baoge.mapper.ClientMapper;
import com.baoge.model.Client;
import com.baoge.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientMapper clientMapper;

    @Override
    public Client findByAccount(String account) {
        return clientMapper.findByAccount(account);
    }
}
