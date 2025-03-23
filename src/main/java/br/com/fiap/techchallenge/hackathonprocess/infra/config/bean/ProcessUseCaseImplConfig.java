package br.com.fiap.techchallenge.hackathonprocess.infra.config.bean;

import br.com.fiap.techchallenge.hackathonprocess.application.usecase.impl.ProcessUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessUseCaseImplConfig {

    @Bean
    public ProcessUseCaseImpl processUseCase(){
        return new ProcessUseCaseImpl();
    }
}
