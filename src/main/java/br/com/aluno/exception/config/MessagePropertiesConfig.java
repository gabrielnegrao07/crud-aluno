package br.com.aluno.exception.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 *  Configuração para carregar os arquivos de mensagens e exceptions.
 */

@Log4j2
@Configuration
public class MessagePropertiesConfig {

    public static final String MESSAGE_PROPERTIES_BEAN_NAME = "messagePropertiesMap";

    @Bean(MESSAGE_PROPERTIES_BEAN_NAME)
    public Map<String, String> getExceptionMessageProperties() {
        final String exceptionFilePath = "/messages/exceptions.properties";

        try {
            InputStreamReader exceptionStream = new InputStreamReader(getClass().getResourceAsStream(exceptionFilePath), StandardCharsets.UTF_8.name());

            Properties properties = new Properties();
            properties.load(exceptionStream);

            Map<String, String> result = new HashMap<>();

            return properties.keySet()
                    .stream()
                    .collect(Collectors.toMap(
                            key -> key.toString(),
                            key -> properties.getProperty(key.toString())
                            )
                    );

        } catch (IOException | NullPointerException e) {
            log.warn("Problemas ao carregar arquivo de mensagens '{}'", exceptionFilePath);
            log.error("Erro ao carregar arquivo de mensagens", e);
        }

        return null;
    }
}
