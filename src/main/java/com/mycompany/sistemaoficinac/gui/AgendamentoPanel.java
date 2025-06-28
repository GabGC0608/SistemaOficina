package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Painel para gerenciamento de agendamentos.
 */
public class AgendamentoPanel extends BasePanel {
    
    public AgendamentoPanel(Oficina oficina) {
        super(oficina, "Gerenciamento de Agendamentos");
    }
    
    @Override
    protected void setupTable() {
        String[] colunas = {"Número", "Cliente", "Veículo", "Serviço", "Status", "Data"};
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
        buttonPanel.add(createButton("Agendar Serviço", e -> agendarServico()));
        buttonPanel.add(createButton("Cancelar Ordem", e -> cancelarOrdem()));
        buttonPanel.add(createButton("Concluir Ordem", e -> concluirOrdem()));
        buttonPanel.add(createButton("Listar Ordens", e -> listarOrdens()));
    }
    
    @Override
    protected void setupForm() {
        // Formulário será implementado quando necessário
    }
    
    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        List<OrdemServico> ordens = oficina.getOrdensServico();
        
        for (OrdemServico ordem : ordens) {
            Object[] row = {
                ordem.getNumeroOrdem(),
                ordem.getCliente() != null ? ordem.getCliente().getNome() : "N/A",
                ordem.getVeiculo() != null ? ordem.getVeiculo().getModelo() : "N/A",
                ordem.getServicos() != null && !ordem.getServicos().isEmpty() ? 
                    ordem.getServicos().get(0).getNome() : "N/A",
                ordem.getStatus(),
                ordem.getData()
            };
            tableModel.addRow(row);
        }
    }
    
    @Override
    protected boolean saveFormData() {
        return true; // Implementar quando necessário
    }
    
    private void agendarServico() {
        try {
            // Selecionar cliente
            List<Cliente> clientes = oficina.getClientes();
            if (clientes.isEmpty()) {
                showError("Nenhum cliente cadastrado!");
                return;
            }
            String[] nomesClientes = clientes.stream().map(Cliente::getNome).toArray(String[]::new);
            String clienteNome = (String) JOptionPane.showInputDialog(this, "Selecione o cliente:", "Agendar Serviço", JOptionPane.QUESTION_MESSAGE, null, nomesClientes, nomesClientes[0]);
            if (clienteNome == null) return;
            Cliente cliente = clientes.stream().filter(c -> c.getNome().equals(clienteNome)).findFirst().orElse(null);
            if (cliente == null) { showError("Cliente não encontrado!"); return; }
            if (cliente.getVeiculos().isEmpty()) { showError("Cliente não possui veículos!"); return; }
            String[] placas = cliente.getVeiculos().stream().map(Veiculo::getPlaca).toArray(String[]::new);
            String placa = (String) JOptionPane.showInputDialog(this, "Selecione o veículo:", "Agendar Serviço", JOptionPane.QUESTION_MESSAGE, null, placas, placas[0]);
            if (placa == null) return;
            Veiculo veiculo = cliente.getVeiculos().stream().filter(v -> v.getPlaca().equals(placa)).findFirst().orElse(null);
            if (veiculo == null) { showError("Veículo não encontrado!"); return; }
            // Selecionar serviço
            List<Servico> servicos = oficina.getServicos();
            if (servicos.isEmpty()) { showError("Nenhum serviço cadastrado!"); return; }
            String[] nomesServicos = servicos.stream().map(Servico::getNome).toArray(String[]::new);
            String servicoNome = (String) JOptionPane.showInputDialog(this, "Selecione o serviço:", "Agendar Serviço", JOptionPane.QUESTION_MESSAGE, null, nomesServicos, nomesServicos[0]);
            if (servicoNome == null) return;
            Servico servico = servicos.stream().filter(s -> s.getNome().equals(servicoNome)).findFirst().orElse(null);
            if (servico == null) { showError("Serviço não encontrado!"); return; }
            // Selecionar responsável
            List<Funcionario> funcionarios = oficina.getFuncionarios();
            if (funcionarios.isEmpty()) { showError("Nenhum funcionário cadastrado!"); return; }
            String[] nomesFuncionarios = funcionarios.stream().map(Funcionario::getNome).toArray(String[]::new);
            String responsavelNome = (String) JOptionPane.showInputDialog(this, "Selecione o responsável:", "Agendar Serviço", JOptionPane.QUESTION_MESSAGE, null, nomesFuncionarios, nomesFuncionarios[0]);
            if (responsavelNome == null) return;
            Funcionario responsavel = funcionarios.stream().filter(f -> f.getNome().equals(responsavelNome)).findFirst().orElse(null);
            if (responsavel == null) { showError("Funcionário não encontrado!"); return; }
            // Data e hora
            String data = JOptionPane.showInputDialog(this, "Data (dd/MM/yyyy):", "Agendar Serviço", JOptionPane.QUESTION_MESSAGE);
            if (data == null || data.trim().isEmpty()) return;
            String hora = JOptionPane.showInputDialog(this, "Hora (HH:mm):", "Agendar Serviço", JOptionPane.QUESTION_MESSAGE);
            if (hora == null || hora.trim().isEmpty()) return;
            // Criar ordem
            OrdemServico ordem = new OrdemServico(cliente, veiculo, java.util.Collections.singletonList(servico), responsavel, data, hora);
            oficina.getOrdensServico().add(ordem);
            loadData();
            showMessage("Ordem de serviço agendada!");
        } catch (Exception ex) {
            showError("Erro ao agendar serviço: " + ex.getMessage());
        }
    }
    
    private void cancelarOrdem() {
        if (!hasSelection()) {
            showError("Selecione uma ordem para cancelar!");
            return;
        }
        int row = table.getSelectedRow();
        String numero = (String) tableModel.getValueAt(row, 0);
        OrdemServico ordem = oficina.getOrdensServico().stream().filter(o -> o.getNumeroOrdem().equals(numero)).findFirst().orElse(null);
        if (ordem == null) { showError("Ordem não encontrada!"); return; }
        if ("Cancelado".equalsIgnoreCase(ordem.getStatus())) { showError("Ordem já está cancelada!"); return; }
        if (confirmAction("Cancelar a ordem de serviço #" + numero + "?")) {
            ordem.setStatus("Cancelado");
            loadData();
            showMessage("Ordem cancelada!");
        }
    }
    
    private void concluirOrdem() {
        if (!hasSelection()) {
            showError("Selecione uma ordem para concluir!");
            return;
        }
        int row = table.getSelectedRow();
        String numero = (String) tableModel.getValueAt(row, 0);
        OrdemServico ordem = oficina.getOrdensServico().stream().filter(o -> o.getNumeroOrdem().equals(numero)).findFirst().orElse(null);
        if (ordem == null) { showError("Ordem não encontrada!"); return; }
        if ("Concluído".equalsIgnoreCase(ordem.getStatus())) { showError("Ordem já está concluída!"); return; }
        if (confirmAction("Concluir a ordem de serviço #" + numero + "?")) {
            ordem.setStatus("Concluído");
            loadData();
            showMessage("Ordem concluída!");
        }
    }
    
    private void listarOrdens() {
        loadData();
        showMessage("Lista de ordens atualizada!");
    }
} 