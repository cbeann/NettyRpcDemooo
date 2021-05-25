package com.example.rpcexampleserverclient.service;

import org.springframework.stereotype.Service;

import com.service.ServiceClientService;

/**
 * Created on 2021-05-25
 */
@Service
public class ServiceClientServiceImpl  implements ServiceClientService {
    @Override
    public String getServerClient(String id) {
        return "ServiceClientServiceImpl--" + id;
    }
}
