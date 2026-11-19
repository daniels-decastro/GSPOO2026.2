import java.util.ArrayList;
import java.util.List;

// Pluviômetro que mede a chuva em milímetros por hora (mm/h).
public class SensorChuva extends ComponenteTerritorial implements Sensor {

    private List<Double> leituras;
    private double chuvaSimulada; // Usado para gerar a próxima leitura simulada

    public SensorChuva(String id, String nome, String regiao) {
        super(id, nome, regiao); // A mãe cuida de id, nome, região e status
        this.leituras = new ArrayList<>();
        this.chuvaSimulada = 2.0;
    }

    @Override
    public double lerValor() {
        // 60% da chuva da hora anterior continua e cai uma chuva nova de 0 a 3 mm/h
        this.chuvaSimulada = this.chuvaSimulada * 0.6 + Math.random() * 3;

        // Em 6% das horas começa uma pancada forte de 20 a 50 mm/h a mais
        if (Math.random() < 0.06) {
            this.chuvaSimulada = this.chuvaSimulada + 20 + Math.random() * 30;
        }

        double leitura = Math.round(this.chuvaSimulada * 10) / 10.0; // 1 casa decimal
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
        String intensidade;
        if (ultimaLeitura < 2.5) {
            intensidade = "chuva fraca";
        } else if (ultimaLeitura < 10) {
            intensidade = "chuva moderada";
        } else {
            intensidade = "chuva forte";
        }
        System.out.println("   Última leitura: " + ultimaLeitura + " mm/h (" + intensidade + ")");
    }

    @Override
    public String getTipo() {
        return "Sensor de Chuva";
    }

    @Override
    public String getUnidade() {
        return "mm/h";
    }

    @Override
    public List<Double> getLeituras() {
        return this.leituras;
    }
}
