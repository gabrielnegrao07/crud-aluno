package br.com.aluno.exception.handler.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO de tratamento do retorno de todas as exceções {@link ConstraintViolationException}.
 */

@Data
@NoArgsConstructor
public class ConstraintViolationExceptionResponseDTO extends BaseExceptionResponseDTO {

    @Data
    @AllArgsConstructor
    public class MessageDetailDto {
        private String field;
        private String messageDetail;
    }

    public static final String EXCEPTION_CONTRAINT_VIOLATION = "EXCEPTION_CONTRAINT_VIOLATION";

    private List<MessageDetailDto> messageDetails;

    public ConstraintViolationExceptionResponseDTO addMessageDetails(@NotNull Path field, @NotNull String messageDetail) {

        if (Objects.nonNull(this.getMessageDetails())) {
            this.messageDetails = new ArrayList<>();
        }

        this.getMessageDetails().add(new MessageDetailDto(field.toString(), messageDetail));
        return this;
    }
}
