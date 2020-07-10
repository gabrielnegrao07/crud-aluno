package br.com.aluno.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlunoRequest {

    @NotEmpty(message = "Nome obrigatório")
    private String nome;

    @NotNull(message = "Idade obrigatório")
    private Integer idade;}
