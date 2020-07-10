package br.com.aluno.service;

import br.com.aluno.common.exception.AlunoNotFoundException;
import br.com.aluno.model.entity.Aluno;
import br.com.aluno.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository repository;

    public void cadastrar(Aluno aluno) {
        repository.save(aluno);
    }

    public void atualizar(Aluno alunoRequest, Long idAluno) throws AlunoNotFoundException {
        Aluno aluno = buscarPorId(idAluno);
        aluno.setIdade(alunoRequest.getIdade());
        repository.save(aluno);
    }

    public Aluno buscarPorId(Long idAluno) throws AlunoNotFoundException {
        return repository.findById(idAluno).orElseThrow(AlunoNotFoundException::new);
    }

    public List<Aluno> buscarAlunos() {
        return repository.findAll();
    }
}
