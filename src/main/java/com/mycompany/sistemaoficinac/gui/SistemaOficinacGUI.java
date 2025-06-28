package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Interface gráfica principal do sistema de gerenciamento de oficina mecânica.
 * Substitui a interface de console por uma interface gráfica moderna.
 */
public class SistemaOficinacGUI extends JFrame {
    
    private static Oficina oficina = new Oficina();
    private static Login loginManager = new Login();
    private static String tipoUsuarioLogado = null;
    
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private JPanel sidebarPanel;
    
    // Constantes para os cards
    public static final String CARD_MENU = "menu";
    public static final String CARD_CLIENTES = "clientes";
    public static final String CARD_VEICULOS = "veiculos";
    public static final String CARD_SERVICOS = "servicos";
    public static final String CARD_AGENDAMENTOS = "agendamentos";
    public static final String CARD_FUNCIONARIOS = "funcionarios";
    public static final String CARD_FINANCEIRO = "financeiro";
    public static final String CARD_ESTOQUE = "estoque";
    public static final String CARD_ELEVADORES = "elevadores";
    public static final String CARD_PONTO = "ponto";
    public static final String CARD_RELATORIOS = "relatorios";
    
    // Painéis
    private ClientePanel clientePanel;
    private VeiculoPanel veiculoPanel;
    private ServicoPanel servicoPanel;
    private AgendamentoPanel agendamentoPanel;
    private FuncionarioPanel funcionarioPanel;
    private FinanceiroPanel financeiroPanel;
    private EstoquePanel estoquePanel;
    private ElevadorPanel elevadorPanel;
    private PontoPanel pontoPanel;
    private RelatorioPanel relatorioPanel;
    
    public SistemaOficinacGUI() {
        setTitle("Sistema Oficina C - Gestão Mecânica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Carregar dados
        carregarDados();
        
        // Exibir login
        if (!exibirLogin()) {
            System.exit(0);
        }
        
        // Inicializar componentes
        initializeComponents();
        setupLayout();
        
        // Salvar dados ao fechar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salvarDados();
            }
        });
    }
    
    private void carregarDados() {
        try {
            oficina.carregarDados();
        } catch (Exception e) {
            int resposta = JOptionPane.showConfirmDialog(this,
                "Erro ao carregar dados: " + e.getMessage() + "\nDeseja criar um novo sistema?",
                "Erro", JOptionPane.YES_NO_OPTION);
            
            if (resposta == JOptionPane.YES_OPTION) {
                oficina = new Oficina();
                loginManager = new Login();
                oficina.inicializarDadosDemonstracao();
                try {
                    oficina.salvarDados();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar dados: " + ex.getMessage());
                }
            } else {
                System.exit(0);
            }
        }
    }
    
    private boolean exibirLogin() {
        LoginDialog loginDialog = new LoginDialog(this, loginManager);
        loginDialog.setVisible(true);
        
        if (loginDialog.isLoginSucesso()) {
            tipoUsuarioLogado = loginDialog.getTipoUsuario();
            return true;
        }
        return false;
    }
    
    private void initializeComponents() {
        // Painel de cards
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        
        // Criar painéis
        clientePanel = new ClientePanel(oficina);
        veiculoPanel = new VeiculoPanel(oficina);
        servicoPanel = new ServicoPanel(oficina);
        agendamentoPanel = new AgendamentoPanel(oficina);
        funcionarioPanel = new FuncionarioPanel(oficina);
        financeiroPanel = new FinanceiroPanel(oficina);
        estoquePanel = new EstoquePanel(oficina);
        elevadorPanel = new ElevadorPanel(oficina);
        pontoPanel = new PontoPanel(oficina);
        relatorioPanel = new RelatorioPanel(oficina);
        
        // Adicionar cards
        cardsPanel.add(criarDashboard(), CARD_MENU);
        cardsPanel.add(clientePanel, CARD_CLIENTES);
        cardsPanel.add(veiculoPanel, CARD_VEICULOS);
        cardsPanel.add(servicoPanel, CARD_SERVICOS);
        cardsPanel.add(agendamentoPanel, CARD_AGENDAMENTOS);
        cardsPanel.add(funcionarioPanel, CARD_FUNCIONARIOS);
        cardsPanel.add(financeiroPanel, CARD_FINANCEIRO);
        cardsPanel.add(estoquePanel, CARD_ESTOQUE);
        cardsPanel.add(elevadorPanel, CARD_ELEVADORES);
        cardsPanel.add(pontoPanel, CARD_PONTO);
        cardsPanel.add(relatorioPanel, CARD_RELATORIOS);
        
        // Barra lateral
        sidebarPanel = criarSidebar();
        
        add(sidebarPanel, BorderLayout.WEST);
        add(cardsPanel, BorderLayout.CENTER);
    }
    
    private JPanel criarSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridBagLayout());
        sidebar.setBackground(new Color(40, 40, 50));
        sidebar.setPreferredSize(new Dimension(250, 800));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        int row = 0;
        
        // Logo/Título
        JLabel titleLabel = new JLabel("Sistema Oficina C");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = row++;
        sidebar.add(titleLabel, gbc);
        
        // Separador
        gbc.gridy = row++;
        sidebar.add(createSeparator(), gbc);
        
        // Botões do menu
        sidebar.add(createSidebarButton("Dashboard", this::voltarMenu), gbc);
        gbc.gridy = ++row;
        
        sidebar.add(createSidebarButton("Clientes", this::abrirClientes), gbc);
        gbc.gridy = ++row;
        
        sidebar.add(createSidebarButton("Veículos", this::abrirVeiculos), gbc);
        gbc.gridy = ++row;
        
        sidebar.add(createSidebarButton("Serviços", this::abrirServicos), gbc);
        gbc.gridy = ++row;
        
        sidebar.add(createSidebarButton("Agendamentos", this::abrirAgendamentos), gbc);
        gbc.gridy = ++row;
        
        sidebar.add(createSidebarButton("Estoque", this::abrirEstoque), gbc);
        gbc.gridy = ++row;
        
        sidebar.add(createSidebarButton("Elevadores", this::abrirElevadores), gbc);
        gbc.gridy = ++row;
        
        sidebar.add(createSidebarButton("Ponto", this::abrirPonto), gbc);
        gbc.gridy = ++row;
        
        // Botões específicos do administrador
        if ("Administrador".equals(tipoUsuarioLogado)) {
            sidebar.add(createSidebarButton("Funcionários", this::abrirFuncionarios), gbc);
            gbc.gridy = ++row;
            
            sidebar.add(createSidebarButton("Financeiro", this::abrirFinanceiro), gbc);
            gbc.gridy = ++row;
            
            sidebar.add(createSidebarButton("Relatórios", this::abrirRelatorios), gbc);
            gbc.gridy = ++row;
        }
        
        // Separador
        gbc.gridy = ++row;
        sidebar.add(createSeparator(), gbc);
        
        // Botão sair
        gbc.gridy = ++row;
        sidebar.add(createSidebarButton("Sair", this::sair), gbc);
        
        return sidebar;
    }
    
    private JPanel createSeparator() {
        JPanel separator = new JPanel();
        separator.setBackground(new Color(80, 80, 100));
        separator.setPreferredSize(new Dimension(200, 2));
        return separator;
    }
    
    private JButton createSidebarButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(30, 120, 220));
        button.setForeground(Color.BLACK);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 16f));
        button.setPreferredSize(new Dimension(200, 40));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 255), 2),
            BorderFactory.createEmptyBorder(10, 24, 10, 24)
        ));
        button.addActionListener(e -> action.run());
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(20, 90, 180));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(30, 120, 220));
            }
        });
        return button;
    }
    
    private JPanel criarDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));
        
        JLabel welcomeLabel = new JLabel("Bem-vindo ao Sistema Oficina C!", SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 24f));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Usuário: " + tipoUsuarioLogado, SwingConstants.CENTER);
        userLabel.setFont(userLabel.getFont().deriveFont(16f));
        userLabel.setForeground(Color.LIGHT_GRAY);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(40, 40, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(welcomeLabel, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        centerPanel.add(userLabel, gbc);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }
    
    // Métodos de navegação
    private void voltarMenu() {
        cardLayout.show(cardsPanel, CARD_MENU);
    }
    
    private void abrirClientes() {
        cardLayout.show(cardsPanel, CARD_CLIENTES);
        clientePanel.atualizarDados();
    }
    
    private void abrirVeiculos() {
        cardLayout.show(cardsPanel, CARD_VEICULOS);
        veiculoPanel.atualizarDados();
    }
    
    private void abrirServicos() {
        cardLayout.show(cardsPanel, CARD_SERVICOS);
        servicoPanel.atualizarDados();
    }
    
    private void abrirAgendamentos() {
        cardLayout.show(cardsPanel, CARD_AGENDAMENTOS);
        agendamentoPanel.atualizarDados();
    }
    
    private void abrirFuncionarios() {
        cardLayout.show(cardsPanel, CARD_FUNCIONARIOS);
        funcionarioPanel.atualizarDados();
    }
    
    private void abrirFinanceiro() {
        cardLayout.show(cardsPanel, CARD_FINANCEIRO);
        financeiroPanel.atualizarDados();
    }
    
    private void abrirEstoque() {
        cardLayout.show(cardsPanel, CARD_ESTOQUE);
        estoquePanel.atualizarDados();
    }
    
    private void abrirElevadores() {
        cardLayout.show(cardsPanel, CARD_ELEVADORES);
        elevadorPanel.atualizarDados();
    }
    
    private void abrirPonto() {
        cardLayout.show(cardsPanel, CARD_PONTO);
        pontoPanel.atualizarDados();
    }
    
    private void abrirRelatorios() {
        cardLayout.show(cardsPanel, CARD_RELATORIOS);
        relatorioPanel.atualizarDados();
    }
    
    private void setupLayout() {
        // Configurações adicionais de layout se necessário
    }
    
    private void salvarDados() {
        try {
            oficina.salvarDados();
            JOptionPane.showMessageDialog(this, "Dados salvos com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar dados: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void sair() {
        int resposta = JOptionPane.showConfirmDialog(this, 
            "Deseja salvar os dados antes de sair?", "Sair", 
            JOptionPane.YES_NO_CANCEL_OPTION);
        
        if (resposta == JOptionPane.YES_OPTION) {
            salvarDados();
            System.exit(0);
        } else if (resposta == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
        // Se CANCEL, não faz nada
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new SistemaOficinacGUI().setVisible(true);
        });
    }
} 