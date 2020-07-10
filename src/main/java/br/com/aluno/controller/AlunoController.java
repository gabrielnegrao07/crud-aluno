package br.com.aluno.controller;

import br.com.aluno.common.exception.AlunoNotFoundException;
import br.com.aluno.common.request.AlunoRequest;
import br.com.aluno.common.response.AlunoResponse;
import br.com.aluno.model.entity.Aluno;
import br.com.aluno.model.mapper.AlunoMapper;
import br.com.aluno.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoMapper mapper;

    private final AlunoService service;

    @PostMapping("/api/alunos")
    public ResponseEntity<Void> cadastrar(@RequestBody AlunoRequest request) {
        service.cadastrar(mapper.requestToEntity(request));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/alunos")
    public ResponseEntity<List<AlunoResponse>> listar() {
        List<Aluno> alunos = service.buscarAlunos();
        List<AlunoResponse> alunosResponse = alunos.stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(alunosResponse);
    }

    @GetMapping("/api/alunos/{idAluno}")
    public ResponseEntity<AlunoResponse> buscarPorId(@PathVariable("idAluno") Long idAluno) throws AlunoNotFoundException {
        return ResponseEntity.ok(mapper.entityToResponse(service.buscarPorId(idAluno)));
    }

    @PutMapping("/api/alunos/{idAluno}")
    public ResponseEntity<Void> atualizar(@PathVariable("idAluno") Long idAluno, @RequestBody AlunoRequest request) throws AlunoNotFoundException {
        service.atualizar(mapper.requestToEntity(request), idAluno);
        return ResponseEntity.noContent().build();
    }


}
