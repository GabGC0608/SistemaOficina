package com.mycompany.sistemaoficinac;

import java.util.Comparator;
public class OrdemDeServicoPorNumeroDeServicos implements Comparator<OrdemServico> {
    @Override
    public int compare(OrdemServico o1, OrdemServico o2) {
        return Integer.compare(o1.getServicos().size(), o2.getServicos().size());
    }
}