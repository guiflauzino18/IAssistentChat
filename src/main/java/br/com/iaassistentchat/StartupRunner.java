package br.com.iaassistentchat;

import br.com.iaassistentchat.services.wikijs.WikijsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements ApplicationRunner {

    @Autowired
    private WikijsService wikijsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
