package br.com.aluno.exception.resource;

import java.util.Map;

public abstract class BaseException extends Exception {

    private Map<String, Object> mapDetails;

    public BaseException(final Map<String, Object> mapDetails) {
        this.mapDetails = mapDetails;
    }

    public abstract String getExceptionKey();

    public Map<String, Object> getMapDetails() {
        return mapDetails;
    }
}
