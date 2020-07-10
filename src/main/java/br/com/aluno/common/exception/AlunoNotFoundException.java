package br.com.aluno.common.exception;

import br.com.aluno.exception.business.BaseBusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AlunoNotFoundException extends BaseBusinessException {
    private static final String CHAVE_MENSAGEM = "EXCEPTION_ALUNO_NOT_FOUND";

    public AlunoNotFoundException() {
        super(new HashMap<>());
    }

    @Override
    public String getExceptionKey() {
        return CHAVE_MENSAGEM;
    }
}