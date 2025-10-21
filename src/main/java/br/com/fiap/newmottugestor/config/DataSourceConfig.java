package br.com.fiap.newmottugestor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.fiap.newmottugestor.oracle.repository")
@EnableMongoRepositories(basePackages = "br.com.fiap.newmottugestor.mongo.repository")
public class DataSourceConfig {
}