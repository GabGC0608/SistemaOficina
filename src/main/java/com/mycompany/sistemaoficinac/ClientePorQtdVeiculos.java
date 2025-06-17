package com.mycompany.sistemaoficinac;

import java.util.Comparator;

/**
 * Comparador que ordena clientes pela quantidade de veículos que possuem.
 */
public class ClientePorQtdVeiculos implements Comparator<Cliente> {
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