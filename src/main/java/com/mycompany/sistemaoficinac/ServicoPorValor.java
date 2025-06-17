package com.mycompany.sistemaoficinac;

import java.util.Comparator;

/**
 * Comparador que ordena serviços pelo valor.
 */
public class ServicoPorValor implements Comparator<Servico> {
    /**
     * Compara dois serviços pelo valor.
     * @param s1 Primeiro serviço a ser comparado
     * @param s2 Segundo serviço a ser comparado
     * @return -1 se s1 é mais barato, 1 se s2 é mais barato, 0 se têm o mesmo valor
     */
    @Override
    public int compare(Servico s1, Servico s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return -1;
        if (s2 == null) return 1;
        
        double diff = s1.getValor() - s2.getValor();
        if (diff < 0) return -1;
        if (diff > 0) return 1;
        return 0;
    }
} 