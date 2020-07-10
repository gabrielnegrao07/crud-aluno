package br.com.aluno.exception.business;

import br.com.aluno.exception.resource.BaseException;

import java.util.Map;

/**
 * Classe de exceção para regras de negócio.
 */
public abstract class BaseBusinessException extends BaseException {
    public BaseBusinessException(Map<String, Object> mapDetails) {
        super(mapDetails);
    }
}
