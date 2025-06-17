package com.mycompany.sistemaoficinac;

import java.util.Comparator;

/**
 * Comparador que ordena agendamentos por data e hora.
 */
public class OrdemPorData implements Comparator<OrdemServico> {
    /**
     * Compara dois agendamentos pela data e hora.
     * @param a1 Primeiro agendamento a ser comparado
     * @param a2 Segundo agendamento a ser comparado
     * @return Resultado da comparação das datas no formato string
     */
    @Override
    public int compare(OrdemServico a1, OrdemServico a2) {
        if (a1 == null && a2 == null) return 0;
        if (a1 == null) return -1;
        if (a2 == null) return 1;
        
        String data1 = a1.getData();
        String data2 = a2.getData();
        
        if (data1 == null && data2 == null) return 0;
        if (data1 == null) return -1;
        if (data2 == null) return 1;
        
        return data1.compareTo(data2);
    }
} 