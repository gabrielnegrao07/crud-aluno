package br.com.aluno.exception.utils;

import br.com.aluno.exception.config.MessagePropertiesConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Classe responsável por tratar traduções da mensagens de exceção.
 * Tradução das mensagens devem ser inseridas no arquivo classpath:/messages/exceptions.properties
 */

@Component
@Log4j2
public class MessageUtils {

    @Autowired
    @Qualifier(MessagePropertiesConfig.MESSAGE_PROPERTIES_BEAN_NAME)
    private Map<String, String> prop;

    private static final String DEFAULT_EXCEPTION_KEY = "EXCEPTION_UNDEFINED_EXCEPTION";

    /**
     * Procura tradução da exceção no arquivo de exceções da aplicação.
     *
     * @Param exceptionKey chave da exceção;
     * @return {@code Optional} da tradução da exceção.
     */
    private Optional<String> findMessageTranslation(@NotNull final String exceptionKey) {
        String exceptionKeyUpper = exceptionKey.toUpperCase();

        //Verifica se existe o arquivo 'exceptions.properties' no classpath da aplicação.
        if (Objects.nonNull(this.prop)) {

            //Busca mensagem customizada.
            if (prop.containsKey(exceptionKeyUpper)) {

                log.debug("Exception Key '{}' encontrada!", exceptionKey);
                return Optional.ofNullable(this.prop.get(exceptionKeyUpper));
            }

            log.warn("Exception key '{}' não encontrada!", exceptionKey);
            return Optional.empty();
        }

        log.error("Arquivo de tradução das mensagens não foi encontrado!");
        return Optional.empty();
    }

    /**
     * Retorna a string de tradução da mensagem do arquivo properties de exceptions já interpolados com {@code messageParams}.
     *
     * @param exceptionKey Chave da mensagem.
     * @param exceptionParams {@code Map} de Parametros para ser interpolados na tradução da exceção.
     * @return {@code String} da mensagem sem interpolação dos parâmetros ou a propria exceptionKey.
     */
    public String getMessageTranslation(@NotNull final String exceptionKey, final Map<String, Object> exceptionParams) {

        String exceptionTranslation = getMessageTranslation(exceptionKey);

        // Interpola a string da mensagem com os parâmetros da mensagem.
        if (Objects.nonNull(exceptionParams)) {
            return new StrSubstitutor(exceptionParams).replace(exceptionTranslation);
        }
        // Caso não consiga resolver, retorna a chave da exceção com tradução.
        return exceptionTranslation;
    }

    /**
     * Retorna a string de tradução da mensagem do arquivo properties de exceptions.
     *
     * @param exceptionKey Chave da mensagem.
     * @return String da mensagem sem interpolação dos parâmetros ou a própria exceptionKey.
     */
    public String getMessageTranslation(@NotNull final String exceptionKey) {
        return findMessageTranslation(exceptionKey)
                .orElse(findMessageTranslation(DEFAULT_EXCEPTION_KEY)
                        .orElse(exceptionKey));
    }
}
