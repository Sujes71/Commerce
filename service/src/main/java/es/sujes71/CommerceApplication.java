package es.sujes71;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableR2dbcRepositories
@SpringBootApplication
public class CommerceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CommerceApplication.class, args);
  }
}