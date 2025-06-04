package com.mycompany.sistemaoficinac;

import java.util.Comparator;

/**
 * Classe utilitária que contém comparadores para ordenação de diferentes entidades do sistema.
 * Fornece implementações de Comparator para Cliente, Agendamento e Serviço.
 */
public class Comparadores {
    
    /**
     * Comparador que ordena clientes por nome em ordem alfabética.
     * A comparação é case-insensitive e considera a ordem lexicográfica dos caracteres.
     */
    public static class ClientePorNome implements Comparator<Cliente> {
        /**
         * Compara dois clientes pelo nome.
         * @param c1 Primeiro cliente a ser comparado
         * @param c2 Segundo cliente a ser comparado
         * @return -1 se c1 vem antes, 1 se c2 vem antes, 0 se são iguais
         */
        @Override
        public int compare(Cliente c1, Cliente c2) {
            if (c1 == null && c2 == null) return 0;
            if (c1 == null) return -1;
            if (c2 == null) return 1;
            
            // Converte para minúsculo para comparação case-insensitive
            char[] nome1 = c1.getNome().toLowerCase().toCharArray();
            char[] nome2 = c2.getNome().toLowerCase().toCharArray();
            
            // Compara caractere por caractere
            int minLength = Math.min(nome1.length, nome2.length);
            
            for (int i = 0; i < minLength; i++) {
                if (nome1[i] < nome2[i]) {
                    return -1;  // nome1 vem antes na ordem alfabética
                }
                if (nome1[i] > nome2[i]) {
                    return 1;   // nome1 vem depois na ordem alfabética
                }
            }
            
            // Se chegou aqui, os caracteres até o comprimento mínimo são iguais
            // O nome mais curto deve vir primeiro
            if (nome1.length < nome2.length) return -1;
            if (nome1.length > nome2.length) return 1;
            return 0;
        }
    }
    
    /**
     * Comparador que ordena clientes pela quantidade de veículos que possuem.
     */
    public static class ClientePorQtdVeiculos implements Comparator<Cliente> {
        /**
         * Compara dois clientes pela quantidade de veículos.
         * @param c1 Primeiro cliente a ser comparado
         * @param c2 Segundo cliente a ser comparado
         * @return Diferença entre a quantidade de veículos dos clientes
         */
        @Override
        public int compare(Cliente c1, Cliente c2) {
            if (c1 == null && c2 == null) return 0;
            if (c1 == null) return -1;
            if (c2 == null) return 1;
            return c1.getVeiculos().size() - c2.getVeiculos().size();
        }
    }
    
    /**
     * Comparador que ordena agendamentos por data e hora.
     */
    public static class AgendamentoPorData implements Comparator<Agenda.Agendamento> {
        /**
         * Compara dois agendamentos pela data e hora.
         * @param a1 Primeiro agendamento a ser comparado
         * @param a2 Segundo agendamento a ser comparado
         * @return Resultado da comparação das datas no formato string
         */
        @Override
        public int compare(Agenda.Agendamento a1, Agenda.Agendamento a2) {
            if (a1 == null && a2 == null) return 0;
            if (a1 == null) return -1;
            if (a2 == null) return 1;
            
            String data1 = a1.getDataHora();
            String data2 = a2.getDataHora();
            
            if (data1 == null && data2 == null) return 0;
            if (data1 == null) return -1;
            if (data2 == null) return 1;
            
            return data1.compareTo(data2);
        }
    }
    
    /**
     * Comparador que ordena serviços pelo valor.
     */
    public static class ServicoPorValor implements Comparator<Servico> {
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
    
    /**
     * Comparador que ordena serviços pelo tempo estimado de execução.
     */
    public static class ServicoPorTempo implements Comparator<Servico> {
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
} 