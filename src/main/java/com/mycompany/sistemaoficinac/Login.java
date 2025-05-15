package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável pelo gerenciamento de autenticação e autorização de usuários no sistema.
 * <p>
 * Armazena credenciais de acesso (usuário/senha) e níveis de permissão associados a cada usuário.
 * </p>
 * 
 * @version 1.0
 * @author Gabriel
 */
public class Login {
    /**
     * Mapa que armazena as credenciais de acesso (usuário como chave e senha como valor).
     * Anotado para serialização JSON com Jackson.
     */
    @JsonProperty("credenciais")
    private Map<String, String> credenciais = new HashMap<>();

    /**
     * Mapa que armazena os níveis de permissão (usuário como chave e tipo de permissão como valor).
     * Anotado para serialização JSON com Jackson.
     */
    @JsonProperty("permissoes")
    private Map<String, String> permissoes = new HashMap<>();

    /**
     * Construtor padrão que inicializa o sistema de login com usuários de exemplo.
     * <p>
     * Cria automaticamente:
     * <ul>
     *   <li>Usuário "admin" com senha "admin123" e permissão de "Administrador"</li>
     *   <li>Usuário "funcionario1" com senha "func123" e permissão de "Funcionario"</li>
     * </ul>
     */
    public Login() {
        // Adiciona credenciais de exemplo
        credenciais.put("admin", "admin123");
        permissoes.put("admin", "Administrador");

        credenciais.put("func", "func123");
        permissoes.put("func", "Funcionario");
    }

    /**
     * Realiza a autenticação de um usuário no sistema.
     *
     * @param usuario Nome de usuário para autenticação
     * @param senha Senha do usuário
     * @return String contendo o tipo de usuário ("Administrador" ou "Funcionario") se autenticado com sucesso,
     *         ou {@code null} se as credenciais forem inválidas
     */
    public String login(String usuario, String senha) {
        if (credenciais.containsKey(usuario) && credenciais.get(usuario).equals(senha)) {
            return permissoes.get(usuario);
        }
        return null;
    }
    /***
     * 
     * @param usuario
     * @param novaSenha
    */
    public void alterarSenha(String usuario, String senhaAtual, String novaSenha) {
        // Verifica se o usuário existe e a senha atual está correta
        if (credenciais.containsKey(usuario) && credenciais.get(usuario).equals(senhaAtual)) {
            credenciais.put(usuario, novaSenha); // Atualiza a senha
            System.out.println("Senha alterada com sucesso!");
        } else {
            System.out.println("Erro: Usuário não encontrado ou senha atual incorreta.");
        }
    }

    /**
     * Retorna uma representação em string do objeto Login, mostrando todas as credenciais e permissões.
     *
     * @return String formatada contendo o estado atual do objeto Login
     */
    @Override
    public String toString() {
        return "Login{" +
                "credenciais=" + credenciais +
                ", permissoes=" + permissoes +
                '}';
    }
}