package br.com.aluno.exception.handler.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonPropertyOrder(value = {"timestamp", "exceptionKey", "message", "httpStatusCode", "httpError", "method", "path"})
@JsonIgnoreProperties({"httpStatus"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseExceptionResponseDTO {

    private HttpStatus httpStatus;

    private Date timestamp;

    private String exceptionKey;

    private String message;

    private String path;

    private String method;

    private String pathQuery;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

    public String getHttpError() {
        return httpStatus.getReasonPhrase();
    }
}
