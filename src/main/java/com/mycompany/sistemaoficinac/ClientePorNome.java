package com.mycompany.sistemaoficinac;

import java.util.Comparator;

/**
 * Comparador que ordena clientes por nome em ordem alfabética.
 * A comparação é case-insensitive e considera a ordem lexicográfica dos caracteres.
 */
public class ClientePorNome implements Comparator<Cliente> {
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