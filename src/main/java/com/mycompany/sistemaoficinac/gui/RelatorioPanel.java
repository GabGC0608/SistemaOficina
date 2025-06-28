package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Painel para gerenciamento de relatórios.
 */
public class RelatorioPanel extends BasePanel {
    
    public RelatorioPanel(Oficina oficina) {
        super(oficina, "Gerenciamento de Relatórios");
    }
    
    @Override
    protected void setupTable() {
        String[] colunas = {"Tipo", "Período", "Status", "Gerado em"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
    }
    
    @Override
    protected void setupButtons() {
        buttonPanel.add(createButton("Relatório Diário", e -> relatorioDiario()));
        buttonPanel.add(createButton("Relatório Mensal", e -> relatorioMensal()));
        buttonPanel.add(createButton("Relatório de Clientes", e -> relatorioClientes()));
        buttonPanel.add(createButton("Relatório de Funcionários", e -> relatorioFuncionarios()));
        buttonPanel.add(createButton("Relatório Estatístico", e -> relatorioEstatistico()));
    }
    
    @Override
    protected void setupForm() {
        // Formulário será implementado quando necessário
    }
    
    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        // Não carrega relatórios automaticamente, apenas via botões
    }
    
    @Override
    protected boolean saveFormData() {
        return true; // Implementar quando necessário
    }
    
    private void relatorioDiario() {
        String data = JOptionPane.showInputDialog(this, "Digite a data (dd/MM/yyyy):");
        if (data == null || !data.matches("\\d{2}/\\d{2}/\\d{4}")) {
            showError("Formato de data inválido! Use dd/MM/yyyy");
            return;
        }
        var caixa = oficina.getCaixa();
        double entradas = caixa.getOrdensServico().stream()
            .filter(os -> os.getData().equals(data) && os.getTipo().equals("Entrada"))
            .mapToDouble(os -> os.getValor()).sum();
        double saidas = caixa.getOrdensServico().stream()
            .filter(os -> os.getData().equals(data) && os.getTipo().equals("Saída"))
            .mapToDouble(os -> os.getValor()).sum();
        double saldo = entradas - saidas;
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"Diário", data, "OK", java.time.LocalDateTime.now()});
        String msg = String.format("Entradas: R$ %.2f\nSaídas: R$ %.2f\nSaldo: R$ %.2f", entradas, saidas, saldo);
        showMessage(msg);
    }
    
    private void relatorioMensal() {
        String mesStr = JOptionPane.showInputDialog(this, "Digite o mês (1-12):");
        String anoStr = JOptionPane.showInputDialog(this, "Digite o ano (ex: 2024):");
        int mes, ano;
        try {
            mes = Integer.parseInt(mesStr);
            ano = Integer.parseInt(anoStr);
        } catch (Exception e) {
            showError("Mês ou ano inválido!");
            return;
        }
        String dataInicio = String.format("01/%02d/%d", mes, ano);
        String dataFim = String.format("31/%02d/%d", mes, ano);
        var caixa = oficina.getCaixa();
        var ordens = caixa.getOrdensPorPeriodo(dataInicio, dataFim);
        double entradas = ordens.stream().filter(os -> os.getTipo().equals("Entrada")).mapToDouble(os -> os.getValor()).sum();
        double saidas = ordens.stream().filter(os -> os.getTipo().equals("Saída")).mapToDouble(os -> os.getValor()).sum();
        double saldo = entradas - saidas;
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"Mensal", mes + "/" + ano, "OK", java.time.LocalDateTime.now()});
        String msg = String.format("Entradas: R$ %.2f\nSaídas: R$ %.2f\nSaldo: R$ %.2f", entradas, saidas, saldo);
        showMessage(msg);
    }
    
    private void relatorioClientes() {
        var clientes = oficina.getClientes();
        tableModel.setRowCount(0);
        for (Cliente c : clientes) {
            tableModel.addRow(new Object[]{"Cliente", c.getNome(), "OK", java.time.LocalDateTime.now()});
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Total de clientes: ").append(clientes.size()).append("\n\n");
        for (Cliente c : clientes) {
            sb.append("Nome: ").append(c.getNome()).append(" | Telefone: ").append(c.getTelefone())
              .append(" | Veículos: ").append(c.getVeiculos().size()).append("\n");
        }
        showMessage(sb.toString());
    }
    
    private void relatorioFuncionarios() {
        var funcionarios = oficina.getFuncionarios();
        tableModel.setRowCount(0);
        for (Funcionario f : funcionarios) {
            tableModel.addRow(new Object[]{"Funcionário", f.getNome(), "OK", java.time.LocalDateTime.now()});
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Total de funcionários: ").append(funcionarios.size()).append("\n\n");
        for (Funcionario f : funcionarios) {
            sb.append("Nome: ").append(f.getNome()).append(" | Cargo: ").append(f.getCargo())
              .append(" | Matrícula: ").append(f.getMatricula()).append("\n");
        }
        showMessage(sb.toString());
    }
    
    private void relatorioEstatistico() {
        String dataInicial = JOptionPane.showInputDialog(this, "Data inicial (dd/MM/yyyy):");
        String dataFinal = JOptionPane.showInputDialog(this, "Data final (dd/MM/yyyy):");
        if (dataInicial == null || dataFinal == null) {
            showError("Datas inválidas!");
            return;
        }
        var ordens = oficina.getOrdensPorPeriodo(dataInicial, dataFinal);
        if (ordens.isEmpty()) {
            showError("Nenhuma ordem no período!");
            return;
        }
        // Estatísticas principais
        long totalServicos = ordens.size();
        double receitaTotal = ordens.stream().mapToDouble(os -> os.getValor()).sum();
        double ticketMedio = receitaTotal / totalServicos;
        // Mecânico mais requisitado
        java.util.Map<String, Long> porMecanico = new java.util.HashMap<>();
        for (var os : ordens) {
            if (os.getResponsavel() != null) {
                porMecanico.merge(os.getResponsavel().getNome(), 1L, Long::sum);
            }
        }
        String maisRequisitado = porMecanico.entrySet().stream()
            .max(java.util.Map.Entry.comparingByValue())
            .map(java.util.Map.Entry::getKey).orElse("N/A");
        // Serviços mais realizados
        java.util.Map<String, Long> porCategoria = new java.util.HashMap<>();
        for (var os : ordens) {
            if (os.getCategoria() != null)
                porCategoria.merge(os.getCategoria(), 1L, Long::sum);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Período: ").append(dataInicial).append(" a ").append(dataFinal).append("\n");
        sb.append("Total de serviços: ").append(totalServicos).append("\n");
        sb.append("Receita total: R$ ").append(String.format("%.2f", receitaTotal)).append("\n");
        sb.append("Ticket médio: R$ ").append(String.format("%.2f", ticketMedio)).append("\n");
        sb.append("Mecânico mais requisitado: ").append(maisRequisitado).append("\n");
        sb.append("Serviços mais realizados: ");
        porCategoria.entrySet().stream()
            .sorted(java.util.Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(3)
            .forEach(e -> sb.append(e.getKey()).append(" (").append(e.getValue()).append(") "));
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"Estatístico", dataInicial + " a " + dataFinal, "OK", java.time.LocalDateTime.now()});
        showMessage(sb.toString());
    }
} 