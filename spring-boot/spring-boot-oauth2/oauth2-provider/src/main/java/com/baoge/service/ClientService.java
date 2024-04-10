package com.baoge.service;

import com.baoge.model.Client;

public interface ClientService {
    Client findByAccount(String account);
}
