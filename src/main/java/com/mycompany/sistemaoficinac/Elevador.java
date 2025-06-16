package com.mycompany.sistemaoficinac;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um elevador utilizado na oficina.
 */
public class Elevador {

    private float peso; // Peso suportado pelo elevador
    private String modelo; // Modelo do elevador
    private String estado; // Estado do elevador ("Disponível", "Ocupado", "Manutenção")
    private LocalDateTime horarioAlocacao; // Horário em que o elevador foi alocado
    private List<String> historicoUso; // Histórico de uso do elevador

    // Array estático para gerenciar os 3 elevadores da oficina
    private static final Elevador[] elevadores = new Elevador[3];

    /**
     * Construtor da classe Elevador.
     *
     * @param peso Peso suportado pelo elevador.
     * @param modelo Modelo do elevador.
     * @param estado Estado inicial do elevador.
     */
    public Elevador(float peso, String modelo, String estado) {
        this.peso = peso;
        this.modelo = modelo;
        this.estado = estado;
        this.historicoUso = new ArrayList<>();
    }

    /**
     * Obtém o peso suportado pelo elevador.
     *
     * @return Peso suportado.
     */
    public float getPeso() {
        return peso;
    }

    /**
     * Define o peso suportado pelo elevador.
     *
     * @param peso Novo peso suportado.
     */
    public void setPeso(float peso) {
        this.peso = peso;
    }

    /**
     * Obtém o modelo do elevador.
     *
     * @return Modelo do elevador.
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Define o modelo do elevador.
     *
     * @param modelo Novo modelo do elevador.
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Obtém o estado atual do elevador.
     *
     * @return Estado do elevador.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Define o estado do elevador.
     *
     * @param estado Novo estado do elevador.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Verifica se o elevador está ocupado.
     *
     * @return true se o elevador estiver ocupado, false caso contrário.
     */
    public boolean isOcupado() {
        return "Ocupado".equalsIgnoreCase(estado);
    }

    /**
     * Obtém o horário em que o elevador foi alocado.
     *
     * @return Horário de alocação.
     */
    public LocalDateTime getHorarioAlocacao() {
        return horarioAlocacao;
    }

    /**
     * Define o horário de alocação do elevador.
     *
     * @param horarioAlocacao Novo horário de alocação.
     */
    public void setHorarioAlocacao(LocalDateTime horarioAlocacao) {
        this.horarioAlocacao = horarioAlocacao;
    }

    /**
     * Obtém o histórico de uso do elevador.
     *
     * @return Lista com o histórico de uso.
     */
    public List<String> getHistoricoUso() {
        return historicoUso;
    }

    /**
     * Adiciona um registro ao histórico de uso do elevador.
     *
     * @param registro Registro a ser adicionado.
     */
    public void adicionarRegistroHistorico(String registro) {
        this.historicoUso.add(registro);
    }

    /**
     * Aloca um elevador disponível para um serviço.
     *
     * @return O elevador alocado ou null se nenhum estiver disponível.
     */
    public static Elevador alocarElevador(int index) {
        if (index >= 0 && index < elevadores.length && elevadores[index] != null) {
            if ("Disponível".equalsIgnoreCase(elevadores[index].getEstado())) {
                elevadores[index].setEstado("Ocupado");
                elevadores[index].setHorarioAlocacao(LocalDateTime.now());
                elevadores[index].adicionarRegistroHistorico(
                    String.format("Alocado em %s para o veículo: %s",
                        elevadores[index].getHorarioAlocacao(),
                        elevadores[index].getModelo())
                );
                return elevadores[index];
            }
        }
        System.out.println("Elevador não disponível ou inválido.");
        return null;
    }

    /**
     * Libera um elevador após a conclusão de um serviço.
     *
     * @param elevador Elevador a ser liberado.
     */
    public static void liberarElevador(int index) {
        if (index >= 0 && index < elevadores.length && elevadores[index] != null) {
            elevadores[index].setEstado("Disponível");
            elevadores[index].adicionarRegistroHistorico(
                String.format("Liberado em %s",
                    LocalDateTime.now())
            );
        }
    }

    /**
     * Inicializa os elevadores da oficina.
     */
    public static void inicializarElevadores() {
        elevadores[0] = new Elevador(2000, "Geral 1", "Disponível");
        elevadores[1] = new Elevador(2000, "Geral 2", "Disponível");
        elevadores[2] = new Elevador(1500, "Alinhamento", "Disponível");
    }

    /**
     * Lista o estado atual de todos os elevadores.
     */
    public static void listarElevadores() {
        System.out.println("\n=== ESTADO DOS ELEVADORES ===");
        for (int i = 0; i < elevadores.length; i++) {
            Elevador elevador = elevadores[i];
            if (elevador != null) {
                System.out.println("Elevador " + (i + 1) + ": " + elevador.getModelo() +
                                   " | Estado: " + elevador.getEstado());
            }
        }
    }

    /**
     * Obtém um elevador específico do array estático.
     * @param index Índice do elevador (0-2)
     * @return O elevador no índice especificado
     */
    public static Elevador getElevador(int index) {
        if (index >= 0 && index < elevadores.length) {
            return elevadores[index];
        }
        return null;
    }
}