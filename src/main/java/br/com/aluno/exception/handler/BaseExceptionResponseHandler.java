package br.com.aluno.exception.handler;

import br.com.aluno.exception.handler.response.BaseExceptionResponseDTO;
import br.com.aluno.exception.handler.response.ConstraintViolationExceptionResponseDTO;
import br.com.aluno.exception.resource.BaseException;
import br.com.aluno.exception.resource.BaseRuntimeException;
import br.com.aluno.exception.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Handler de tratamento do retorno de todas as exceções {@link BaseException}.
 */

@ControllerAdvice
@Log4j2
public class BaseExceptionResponseHandler {

    @Autowired
    private MessageUtils messageUtils;

    @ResponseBody
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseExceptionResponseDTO> handleBaseException(HttpServletRequest req,
                                                                        BaseException exception) {
        return getBaseExceptionResponseDtoResponseEntity(req, exception, exception.getExceptionKey(),
                exception.getMapDetails());
    }

    @ResponseBody
    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<BaseExceptionResponseDTO> handleBaseException(HttpServletRequest req,
                                                                        BaseRuntimeException exception) {
        return getBaseExceptionResponseDtoResponseEntity(req, exception, exception.getExceptionKey(),
                exception.getMapDetails());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity handleMethodArgumentNotValid(HttpServletRequest req, MethodArgumentNotValidException ex) {

        String errorURL = req.getRequestURL().toString();

        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        StringBuilder errorMessage = new StringBuilder("");

        for (FieldError fieldError : fieldErrors) {

            errorMessage.append(fieldError.getDefaultMessage()).append(" - ");
            errorMessage.append(fieldError.getObjectName()).append(".");
            errorMessage.append(fieldError.getField());
        }

        return getBaseExceptionResponseDtoResponseEntity(req, ex, errorMessage.toString(),
                HttpStatus.UNPROCESSABLE_ENTITY, result.getModel());
    }

    @ResponseBody
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<ConstraintViolationExceptionResponseDTO> handleConstraintViolation(HttpServletRequest req,
                                                                                             ConstraintViolationException exception) {

        ConstraintViolationExceptionResponseDTO responseDto = new ConstraintViolationExceptionResponseDTO();

        processExceptionResponse(responseDto, ConstraintViolationExceptionResponseDTO.EXCEPTION_CONTRAINT_VIOLATION,
                req, HttpStatus.BAD_REQUEST);

        if (exception != null) {
            exception.getConstraintViolations().forEach(constraintViolation -> {
                responseDto.addMessageDetails(constraintViolation.getPropertyPath(), constraintViolation.getMessage());
            });
        }
        responseDto.setMessage(messageUtils.getMessageTranslation(responseDto.getExceptionKey()));

        return new ResponseEntity<>(responseDto, responseDto.getHttpStatus());
    }

    private BaseExceptionResponseDTO processExceptionResponse(BaseExceptionResponseDTO responseDto,
                                                              final String exceptionKey, final HttpServletRequest req, final HttpStatus httpStatus) {

        // Timestamp.
        responseDto.setTimestamp(Calendar.getInstance().getTime());

        // Request details.
        responseDto.setPath(req.getRequestURI());
        responseDto.setPathQuery(req.getQueryString());
        responseDto.setMethod(req.getMethod());

        // Exception translations.
        responseDto.setExceptionKey(exceptionKey);

        // Retorna código HTTP padrão.
        responseDto.setHttpStatus(httpStatus);

        return responseDto;
    }

    private ResponseEntity<BaseExceptionResponseDTO> getBaseExceptionResponseDtoResponseEntity(HttpServletRequest req,
                                                                                               Exception exception, String exceptionKey, Map<String, Object> mapDetails) {
        // Valida se a exceção foi anotada com @ResponseStatus
        ResponseStatus responseStatusAnnotation = exception.getClass().getAnnotation(ResponseStatus.class);

        // Assume Http 500 como código HTTP padrão.
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (Objects.nonNull(responseStatusAnnotation)) {

            if (!httpStatus.equals(responseStatusAnnotation.code())) {
                httpStatus = responseStatusAnnotation.code();
            } else if (!httpStatus.equals(responseStatusAnnotation.value())) {
                httpStatus = responseStatusAnnotation.value();
            }
        }

        BaseExceptionResponseDTO responseDto = new BaseExceptionResponseDTO();
        processExceptionResponse(responseDto, exceptionKey, req, httpStatus);

        responseDto.setMessage(messageUtils.getMessageTranslation(responseDto.getExceptionKey(), mapDetails));

        return new ResponseEntity<BaseExceptionResponseDTO>(responseDto, httpStatus);
    }

    private ResponseEntity<BaseExceptionResponseDTO> getBaseExceptionResponseDtoResponseEntity(HttpServletRequest req,
                                                                                               Exception exception, String errorMessage, HttpStatus httpStatus, Map<String, Object> mapDetails) {
        // Valida se a exceção foi anotada com @ResponseStatus
        ResponseStatus responseStatusAnnotation = exception.getClass().getAnnotation(ResponseStatus.class);

        if (Objects.nonNull(responseStatusAnnotation)) {

            if (!httpStatus.equals(responseStatusAnnotation.code())) {
                httpStatus = responseStatusAnnotation.code();
            } else if (!httpStatus.equals(responseStatusAnnotation.value())) {
                httpStatus = responseStatusAnnotation.value();
            }
        }

        BaseExceptionResponseDTO responseDto = new BaseExceptionResponseDTO();
        processExceptionResponse(responseDto, errorMessage, req, httpStatus);

        responseDto.setMessage(messageUtils.getMessageTranslation(responseDto.getExceptionKey(), mapDetails));

        return new ResponseEntity<BaseExceptionResponseDTO>(responseDto, httpStatus);
    }
}
