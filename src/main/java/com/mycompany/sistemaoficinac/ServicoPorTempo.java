package com.mycompany.sistemaoficinac;

import java.util.Comparator;

/**
 * Comparador que ordena serviços pelo tempo estimado de execução.
 */
public class ServicoPorTempo implements Comparator<Servico> {
    /**
     * Compara dois serviços pelo tempo estimado.
     * @param s1 Primeiro serviço a ser comparado
     * @param s2 Segundo serviço a ser comparado
     * @return Diferença entre os tempos estimados dos serviços
     */
    @Override
    public int compare(Servico s1, Servico s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return -1;
        if (s2 == null) return 1;
        return s1.getTempoEstimado() - s2.getTempoEstimado();
    }
} 