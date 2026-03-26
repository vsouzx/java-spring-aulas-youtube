package br.com.souza.spring_boot_essentials.dto;

import java.math.BigDecimal;

public interface AvaliacoesFisicasProjection {

    Integer getIdAluno();
    String getNomeAluno();
    Integer getIdAvaliacao();
    BigDecimal getPeso();
    BigDecimal getAltura();
    BigDecimal getPercentualGorduraCorporal();

}
