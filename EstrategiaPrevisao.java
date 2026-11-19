import java.util.List;

// Abstrata porque não existe uma "previsão genérica", só formas específicas (média móvel e comparação).
public abstract class EstrategiaPrevisao {

    // Atributos comuns a todas as formas de previsão
    private String nome;
    private int minimoLeituras; // com menos leituras a previsão não é confiável

    public EstrategiaPrevisao(String nome, int minimoLeituras) {
        this.nome = nome;
        this.minimoLeituras = minimoLeituras;
    }

    // Método abstrato - cada forma de previsão faz a conta do seu jeito
    // As filhas são obrigadas a sobrescrever
    public abstract double prever(List<Double> leituras);

    // Métodos comuns a todas as previsões
    public boolean temLeiturasSuficientes(List<Double> leituras) {
        return leituras.size() >= this.minimoLeituras;
    }

    public void exibirDescricao() {
        System.out.println(this.nome + " (precisa de pelo menos " + this.minimoLeituras + " leituras)");
    }

    // Soma as leituras da posição inicio até antes da posição fim e divide pela quantidade
    // É protected porque só as classes filhas precisam usar
    protected double calcularMedia(List<Double> leituras, int inicio, int fim) {
        double soma = 0;
        for (int i = inicio; i < fim; i++) {
            soma = soma + leituras.get(i);
        }
        return soma / (fim - inicio);
    }

    // Getters
    public String getNome() {
        return this.nome;
    }

    public int getMinimoLeituras() {
        return this.minimoLeituras;
    }
}
