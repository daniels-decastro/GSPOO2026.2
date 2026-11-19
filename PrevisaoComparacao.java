import java.util.List;

// Compara a média do período atual com a do anterior e supõe que a variação continua no mesmo ritmo.

public class PrevisaoComparacao extends EstrategiaPrevisao {

    // Atributo próprio dessa previsão: quantas leituras formam cada período
    private int tamanhoPeriodo;

    public PrevisaoComparacao(int tamanhoPeriodo) {
        // Precisa do período atual e do anterior, por isso o mínimo é o dobro do período
        super("Comparação de Períodos", tamanhoPeriodo * 2);
        this.tamanhoPeriodo = tamanhoPeriodo;
    }

    @Override
    public double prever(List<Double> leituras) {
        int fim = leituras.size();
        double mediaAtual = this.calcularMedia(leituras, fim - this.tamanhoPeriodo, fim);
        double mediaAnterior = this.calcularMedia(leituras, fim - 2 * this.tamanhoPeriodo, fim - this.tamanhoPeriodo);

        // Variação entre os dois períodos somada de novo para projetar o próximo
        double variacao = mediaAtual - mediaAnterior;
        double previsao = mediaAtual + variacao;

        // Nenhuma medidapode ser negativa
        if (previsao < 0) {
            previsao = 0;
        }
        return Math.round(previsao * 10) / 10.0; // 1 casa decimal
    }

    @Override
    public void exibirDescricao() {
        super.exibirDescricao(); // Reaproveita a descrição da classe mãe
        System.out.println("   Compara a média das últimas " + this.tamanhoPeriodo + " leituras com a das "
                + this.tamanhoPeriodo + " anteriores e supõe que a variação continua.");
    }

    public int getTamanhoPeriodo() {
        return this.tamanhoPeriodo;
    }
}
