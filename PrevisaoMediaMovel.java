import java.util.List;

// A média das últimas N leituras é o valor esperado para a próxima hora

public class PrevisaoMediaMovel extends EstrategiaPrevisao {

    // Atributo próprio dessa previsão - quantas leituras recentes entram na média
    private int tamanhoJanela;

    public PrevisaoMediaMovel(int tamanhoJanela) {
        // A mãe guarda o nome e o mínimo de leituras 
        super("Média Móvel", tamanhoJanela);
        this.tamanhoJanela = tamanhoJanela;
    }

    @Override
    public double prever(List<Double> leituras) {
        int fim = leituras.size();
        int inicio = fim - this.tamanhoJanela; // Posição da primeira leitura da janela
        double media = this.calcularMedia(leituras, inicio, fim);
        return Math.round(media * 10) / 10.0; // 1 casa decimal
    }

    @Override
    public void exibirDescricao() {
        super.exibirDescricao(); // Reaproveita a descrição da classe mãe
        System.out.println("   Calcula a média das últimas " + this.tamanhoJanela + " leituras.");
    }

    public int getTamanhoJanela() {
        return this.tamanhoJanela;
    }
}
