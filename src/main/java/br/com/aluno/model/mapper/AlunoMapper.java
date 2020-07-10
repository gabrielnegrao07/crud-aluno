package br.com.aluno.model.mapper;

import br.com.aluno.common.request.AlunoRequest;
import br.com.aluno.common.response.AlunoResponse;
import br.com.aluno.model.entity.Aluno;
import org.springframework.stereotype.Component;

@Component
public class AlunoMapper {

    public Aluno requestToEntity(AlunoRequest alunoRequest) {
        return Aluno.builder()
                .nome(alunoRequest.getNome())
                .idade(alunoRequest.getIdade())
                .build();
    }

    public AlunoResponse entityToResponse(Aluno aluno) {
        return AlunoResponse.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .idade(aluno.getIdade())
                .build();
    }
}
