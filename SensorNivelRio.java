import java.util.ArrayList;
import java.util.List;

// Régua eletrônica que mede o nível da água do córrego em centímetros (cm).

public class SensorNivelRio extends ComponenteTerritorial implements Sensor {

    private List<Double> leituras;
    private double nivelSimulado;       // Usado para gerar a próxima leitura simulada
    private double cotaTransbordamento; // Altura em que a água sai do leito do córrego

    public SensorNivelRio(String id, String nome, String regiao, double cotaTransbordamento) {
        super(id, nome, regiao); // A mãe cuida de id, nome, região e status
        this.leituras = new ArrayList<>();
        this.nivelSimulado = 150.0; // nível normal do córrego
        this.cotaTransbordamento = cotaTransbordamento;
    }

    @Override
    public double lerValor() {
        // O córrego tende a voltar para o nível normal (150 cm) e oscila entre -20 e +20 cm por hora
        this.nivelSimulado = this.nivelSimulado + (150 - this.nivelSimulado) * 0.15 + (Math.random() * 40 - 20);

        // Em 6% das horas chega água de chuva que caiu rio acima - sobe de 50 a 120 cm
        if (Math.random() < 0.06) {
            this.nivelSimulado = this.nivelSimulado + 50 + Math.random() * 70;
        }

        // A água nunca fica abaixo do fundo da régua
        if (this.nivelSimulado < 30) {
            this.nivelSimulado = 30;
        }

        double leitura = Math.round(this.nivelSimulado * 10) / 10.0; // 1 casa decimal
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
        double margem = Math.round((this.cotaTransbordamento - ultimaLeitura) * 10) / 10.0;
        if (margem > 0) {
            System.out.println("   Última leitura: " + ultimaLeitura + " cm (faltam " + margem
                    + " cm para o córrego transbordar)");
        } else {
            System.out.println("   Última leitura: " + ultimaLeitura + " cm (o córrego já transbordou)");
        }
    }

    @Override
    public String getTipo() {
        return "Sensor de Nível do Rio";
    }

    @Override
    public String getUnidade() {
        return "cm";
    }

    @Override
    public List<Double> getLeituras() {
        return this.leituras;
    }
}
