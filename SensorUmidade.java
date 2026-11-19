import java.util.ArrayList;
import java.util.List;

// Mede a umidade do solo da encosta (%). Solo encharcado não absorve a chuva,
// e a água desce para as partes baixas.

public class SensorUmidade extends ComponenteTerritorial implements Sensor {

    private List<Double> leituras;
    private double umidadeSimulada; // Usado pra gerar a próxima leitura simulada

    public SensorUmidade(String id, String nome, String regiao) {
        super(id, nome, regiao); // A mãe cuida de id, nome, região e status
        this.leituras = new ArrayList<>();
        this.umidadeSimulada = 40.0; // Umidade do solo em dias sem chuva
    }

    @Override
    public double lerValor() {
        // O solo seca devagar - volta aos poucos para 40% - e oscila entre -3 e +3 pontos por hora
        this.umidadeSimulada = this.umidadeSimulada + (40 - this.umidadeSimulada) * 0.10 + (Math.random() * 6 - 3);

        // Em 8% das horas uma chuva forte encharca o solo - sobe de 8 a 18 pontos
        if (Math.random() < 0.08) {
            this.umidadeSimulada = this.umidadeSimulada + 8 + Math.random() * 10;
        }

        // Umidade só vai de 0% a 100%
        if (this.umidadeSimulada > 100) {
            this.umidadeSimulada = 100;
        }
        if (this.umidadeSimulada < 0) {
            this.umidadeSimulada = 0;
        }

        double leitura = Math.round(this.umidadeSimulada * 10) / 10.0; // 1 casa decimal
        this.leituras.add(leitura);
        return leitura;
    }

    @Override
    public boolean verificarFuncionamento() {
        return this.getStatus().equals("ATIVO");
    }

    @Override
    public void verificarStatus() {
        System.out.println("[" + this.getId() + "] " + this.getNome() + " - " + this.getRegiao());
        System.out.println("   Tipo: " + this.getTipo() + " | Status: " + this.getStatus()
                + " | Leituras guardadas: " + this.leituras.size());

        if (this.leituras.isEmpty()) {
            System.out.println("   Ainda sem leituras.");
            return;
        }

        double ultimaLeitura = this.leituras.get(this.leituras.size() - 1);
        String situacaoDoSolo;
        if (ultimaLeitura < 30) {
            situacaoDoSolo = "solo seco";
        } else if (ultimaLeitura < 70) {
            situacaoDoSolo = "solo úmido";
        } else {
            situacaoDoSolo = "solo encharcado, absorve pouca água";
        }
        System.out.println("   Última leitura: " + ultimaLeitura + " % (" + situacaoDoSolo + ")");
    }

    @Override
    public String getTipo() {
        return "Sensor de Umidade do Solo";
    }

    @Override
    public String getUnidade() {
        return "%";
    }

    @Override
    public List<Double> getLeituras() {
        return this.leituras;
    }
}
