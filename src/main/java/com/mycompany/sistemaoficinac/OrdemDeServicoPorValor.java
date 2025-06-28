package com.mycompany.sistemaoficinac;

import java.util.Comparator;

public class OrdemDeServicoPorValor implements Comparator<OrdemServico> {
    @Override
    public int compare(OrdemServico o1, OrdemServico o2) {
        if (o1 == null && o2 == null) return 0;
        if (o1 == null) return -1;
        if (o2 == null) return 1;
        return Double.compare(o1.getValor(), o2.getValor());
    }
}