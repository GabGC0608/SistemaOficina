package com.mycompany.sistemaoficinac;

/**
 * Classe que representa um elevador utilizado na oficina.
 */
public class Elevador {

    private float peso; // Peso suportado pelo elevador
    private String modelo; // Modelo do elevador
    private String estado; // Estado do elevador ("Disponível", "Ocupado", "Manutenção")

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
     * Aloca um elevador disponível para um serviço.
     *
     * @return O elevador alocado ou null se nenhum estiver disponível.
     */
    public static Elevador alocarElevador() {
        for (Elevador elevador : elevadores) {
            if (elevador != null && "Disponível".equalsIgnoreCase(elevador.getEstado())) {
                elevador.setEstado("Ocupado");
                return elevador;
            }
        }
        System.out.println("Nenhum elevador disponível no momento.");
        return null;
    }

    /**
     * Libera um elevador após a conclusão de um serviço.
     *
     * @param elevador Elevador a ser liberado.
     */
    public static void liberarElevador(Elevador elevador) {
        if (elevador != null) {
            elevador.setEstado("Disponível");
            System.out.println("Elevador modelo " + elevador.getModelo() + " liberado.");
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
}