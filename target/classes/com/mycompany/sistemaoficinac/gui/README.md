# Interface GUI - Sistema Oficina C

Esta é a interface gráfica (GUI) para o Sistema de Gerenciamento de Oficina Mecânica, desenvolvida em Java Swing.

## Estrutura da Interface

### Arquivos Principais

- **SistemaOficinacGUI.java** - Classe principal da interface, gerencia a janela principal e navegação
- **LoginDialog.java** - Diálogo de login para autenticação de usuários
- **BasePanel.java** - Classe base para todos os painéis da interface

### Painéis de Funcionalidades

- **ClientePanel.java** - Gerenciamento de clientes (cadastro, edição, remoção)
- **VeiculoPanel.java** - Visualização e busca de veículos
- **ServicoPanel.java** - Gerenciamento de serviços oferecidos
- **AgendamentoPanel.java** - Gerenciamento de agendamentos e ordens de serviço
- **FuncionarioPanel.java** - Gerenciamento de funcionários
- **FinanceiroPanel.java** - Controle financeiro da oficina
- **EstoquePanel.java** - Gerenciamento de estoque de peças
- **ElevadorPanel.java** - Controle dos elevadores da oficina
- **PontoPanel.java** - Registro de ponto dos funcionários
- **RelatorioPanel.java** - Geração de relatórios

### Diálogos Auxiliares

- **VeiculoDialog.java** - Diálogo para adicionar veículos aos clientes

## Como Executar

### Opção 1: Executar a Interface GUI
```java
// Execute a classe SistemaOficinacGUI
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new SistemaOficinacGUI().setVisible(true);
    });
}
```

### Opção 2: Modificar o Sistema Original
Para integrar a interface ao sistema original, modifique o método `main` em `SistemaOficinac.java`:

```java
public static void main(String[] args) {
    // Comentar ou remover o código da interface de console
    // E adicionar:
    SwingUtilities.invokeLater(() -> {
        new SistemaOficinacGUI().setVisible(true);
    });
}
```

## Funcionalidades Implementadas

### ✅ Totalmente Funcionais
- **Login**: Autenticação de usuários (admin/admin123 ou func/func123)
- **Clientes**: Cadastro, edição, remoção e busca de clientes
- **Veículos**: Visualização de todos os veículos e busca por placa
- **Serviços**: Cadastro, remoção e listagem de serviços
- **Funcionários**: Visualização e busca de funcionários

### 🔄 Parcialmente Implementadas
- **Agendamentos**: Estrutura básica criada, funcionalidades específicas pendentes
- **Financeiro**: Estrutura básica criada, integração com caixa pendente
- **Estoque**: Estrutura básica criada, integração com estoque pendente
- **Elevadores**: Estrutura básica criada, integração com elevadores pendente
- **Ponto**: Estrutura básica criada, integração com registros pendente
- **Relatórios**: Estrutura básica criada, geração de relatórios pendente

## Características da Interface

### Design
- **Tema Escuro**: Interface com cores escuras para melhor experiência visual
- **Layout Responsivo**: Adapta-se ao tamanho da janela
- **Navegação Intuitiva**: Menu lateral com acesso rápido a todas as funcionalidades

### Usabilidade
- **Tabelas Interativas**: Exibição de dados em tabelas organizadas
- **Formulários Modais**: Diálogos para entrada de dados
- **Validação de Dados**: Verificação de campos obrigatórios
- **Mensagens Informativas**: Feedback visual para o usuário

### Segurança
- **Autenticação**: Sistema de login obrigatório
- **Controle de Acesso**: Diferentes funcionalidades para administradores e funcionários
- **Validação de Entrada**: Verificação de dados antes de salvar

## Próximos Passos

### Funcionalidades a Implementar
1. **Integração Completa**: Conectar todos os painéis com as funcionalidades existentes
2. **Formulários Avançados**: Criar diálogos para todas as operações
3. **Relatórios Visuais**: Implementar gráficos e relatórios detalhados
4. **Exportação de Dados**: Funcionalidade para exportar relatórios
5. **Configurações**: Painel de configurações do sistema

### Melhorias de Interface
1. **Temas**: Opção de temas claro/escuro
2. **Atalhos de Teclado**: Navegação por teclado
3. **Tooltips**: Dicas visuais para melhor usabilidade
4. **Animações**: Transições suaves entre painéis

## Dependências

A interface utiliza apenas bibliotecas padrão do Java:
- `javax.swing.*` - Componentes da interface gráfica
- `java.awt.*` - Utilitários de layout e eventos
- `com.mycompany.sistemaoficinac.*` - Classes do sistema original

## Suporte

Para dúvidas ou problemas com a interface, verifique:
1. Se todas as classes do sistema original estão compiladas
2. Se os arquivos de dados estão no local correto
3. Se as permissões de arquivo estão adequadas

A interface foi projetada para ser extensível e fácil de manter, permitindo adicionar novas funcionalidades conforme necessário. 