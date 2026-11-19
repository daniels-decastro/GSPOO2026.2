import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Classe principal - mota o território, instala os sensores e as previsões e controla o menu
// Faz o sistema de alertas, comparando as previsões com os limiares do território
public class SistemaMonitoramento {

    // Faixas dos níveis de alerta em relação ao limiar de cada sensor
    private final double FATOR_ATENCAO = 0.8; // ATENÇÃO a partir de 80% do limiar
    private final double FATOR_CRITICO = 1.2; // CRÍTICO a partir de 20% acima do limiar

    private Scanner scanner;
    private DadosTerritorio dadosTerritorio;

    private SensorChuva sensorChuva;
    private SensorNivelRio sensorNivelRio;
    private SensorUmidade sensorUmidade;

    // Os mesmos três sensores guardados de dois jeitos 
    private List<Sensor> sensores;                   // Vistos como Sensor (interface) pra fazer leituras
    private List<ComponenteTerritorial> componentes; // Vistos como componente (classe abstrata) pra status

    private List<EstrategiaPrevisao> estrategias;
    private int horaAtual; // Cada rodada de leituras representa 1 hora

    public SistemaMonitoramento() {
        this.scanner = new Scanner(System.in);
        this.dadosTerritorio = new DadosTerritorio("Comunidade FIAP Fundão", 12400, "2026");

        this.sensorChuva = new SensorChuva("CHV-01", "Pluviômetro da UBS", "Centro");
        this.sensorNivelRio = new SensorNivelRio("RIO-01", "Régua do Córrego Azul", "Margem do córrego", 400);
        this.sensorUmidade = new SensorUmidade("UMD-01", "Sensor de solo", "Encosta Norte");

        this.sensores = new ArrayList<>();
        this.sensores.add(this.sensorChuva);
        this.sensores.add(this.sensorNivelRio);
        this.sensores.add(this.sensorUmidade);

        this.componentes = new ArrayList<>();
        this.componentes.add(this.sensorChuva);
        this.componentes.add(this.sensorNivelRio);
        this.componentes.add(this.sensorUmidade);

        this.estrategias = new ArrayList<>();
        this.estrategias.add(new PrevisaoMediaMovel(5));
        this.estrategias.add(new PrevisaoComparacao(3));

        this.horaAtual = 0;
    }

    public static void main(String[] args) {
        SistemaMonitoramento sistema = new SistemaMonitoramento();
        sistema.iniciar();
    }

    // Loop principal do programa
    public void iniciar() {
        System.out.println("=== TERRITORY PREDICTIVE INSIGHTS ===");
        System.out.println("Monitoramento de chuva e risco de alagamento");
        System.out.println("Território: " + this.dadosTerritorio.getNomeRegiao());
        System.out.println("Ainda não há leituras. Use a opção 4 para simular as próximas horas.");

        int opcao;
        do {
            this.exibirMenu();
            opcao = this.lerInteiro();

            switch (opcao) {
                case 1:
                    this.verificarSensores();
                    break;
                case 2:
                    this.rodarPrevisoes();
                    break;
                case 3:
                    this.gerenciarDadosTerritorio();
                    break;
                case 4:
                    this.simularProximasLeituras();
                    break;
                case 5:
                    this.exibirStatusCompleto();
                    break;
                case 6:
                    this.ativarOuDesativarSensor();
                    break;
                case 7:
                    this.simularVinteQuatroHoras();
                    break;
                case 0:
                    System.out.println("Sistema encerrado.");
                    break;
                default:
                    System.out.println("Erro: opção inválida.");
            }
        } while (opcao != 0);

        this.scanner.close();
    }

    private void exibirMenu() {
        System.out.println("\n========================================");
        System.out.println(" MENU PRINCIPAL (Hora " + this.horaAtual + ")");
        System.out.println("========================================");
        System.out.println("1 - Verificar sensores");
        System.out.println("2 - Rodar previsões e alertas");
        System.out.println("3 - Gerenciar dados do território");
        System.out.println("4 - Simular próximas leituras");
        System.out.println("5 - Exibir status completo");
        System.out.println("6 - Ativar ou desativar sensor");
        System.out.println("7 - Simular 24 horas");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    // Verificar sensores

    private void verificarSensores() {
        System.out.println("\n=== VERIFICAÇÃO DOS SENSORES (Hora " + this.horaAtual + ") ===");
        this.mostrarLeituraAtual(this.sensorChuva, this.dadosTerritorio.getLimiarChuva());
        this.mostrarLeituraAtual(this.sensorNivelRio, this.dadosTerritorio.getLimiarNivelRio());
        this.mostrarLeituraAtual(this.sensorUmidade, this.dadosTerritorio.getLimiarUmidade());
    }

    // Mostra se o sensor funciona e se a última leitura passou do limiar
    private void mostrarLeituraAtual(Sensor sensor, double limiar) {
        if (!sensor.verificarFuncionamento()) {
            System.out.println(sensor.getTipo() + ": INATIVO (não está fazendo leituras)");
            return;
        }

        List<Double> leituras = sensor.getLeituras();
        if (leituras.isEmpty()) {
            System.out.println(sensor.getTipo() + ": funcionando, ainda sem leituras");
            return;
        }

        double ultimaLeitura = leituras.get(leituras.size() - 1);
        String texto = sensor.getTipo() + ": funcionando | última leitura " + ultimaLeitura + " " + sensor.getUnidade();
        if (ultimaLeitura >= limiar) {
            System.out.println(texto + " | PASSOU do limiar de " + limiar + " " + sensor.getUnidade());
        } else {
            System.out.println(texto + " | abaixo do limiar de " + limiar + " " + sensor.getUnidade());
        }
    }

    // Previsões e alertas

    private void rodarPrevisoes() {
        System.out.println("\n=== PREVISÕES E ALERTAS (Hora " + this.horaAtual + ") ===");
        this.verificarAlertas(true);
    }

    // Cada sensor é comparado com o seu próprio limiar
    private void verificarAlertas(boolean mostrarDetalhes) {
        this.analisarSensor(this.sensorChuva, this.dadosTerritorio.getLimiarChuva(), mostrarDetalhes);
        this.analisarSensor(this.sensorNivelRio, this.dadosTerritorio.getLimiarNivelRio(), mostrarDetalhes);
        this.analisarSensor(this.sensorUmidade, this.dadosTerritorio.getLimiarUmidade(), mostrarDetalhes);
    }

    private void analisarSensor(Sensor sensor, double limiar, boolean mostrarDetalhes) {
        if (mostrarDetalhes) {
            System.out.println("\n>> " + sensor.getTipo());
        }

        // O sistema se cala quando o sensor está desligado porque prever com dado antigo engana
        if (!sensor.verificarFuncionamento()) {
            if (mostrarDetalhes) {
                System.out.println("   Sensor inativo. Sem previsão.");
            }
            return;
        }

        List<Double> leituras = sensor.getLeituras();
        double maiorPrevisao = -1; // -1 significa nenhuma previsão feita 
        String estrategiaDaMaiorPrevisao = "";

        // Polimorfismo - cada estratégia faz o prever() do seu jeito
        for (EstrategiaPrevisao estrategia : this.estrategias) {
            // O sistema se cala sem leituras suficientes para a estratégia
            if (!estrategia.temLeiturasSuficientes(leituras)) {
                if (mostrarDetalhes) {
                    System.out.println("   " + estrategia.getNome() + ": sem previsão (há " + leituras.size()
                            + " leituras e são necessárias " + estrategia.getMinimoLeituras() + ")");
                }
                continue;
            }

            double previsao = estrategia.prever(leituras);
            if (mostrarDetalhes) {
                System.out.println("   " + estrategia.getNome() + ": " + previsao + " " + sensor.getUnidade());
            }

            if (previsao > maiorPrevisao) {
                maiorPrevisao = previsao;
                estrategiaDaMaiorPrevisao = estrategia.getNome();
            }
        }

        if (maiorPrevisao < 0) {
            if (mostrarDetalhes) {
                System.out.println("   O sistema não emite alerta até juntar leituras suficientes.");
            }
            return;
        }

        // Por precaução o alerta usa a maior previsão entre as estratégias
        this.emitirAlerta(sensor, maiorPrevisao, limiar, estrategiaDaMaiorPrevisao, mostrarDetalhes);
    }

    // Compara a previsão com o limiar, define o nível e explica o alerta
    private void emitirAlerta(Sensor sensor, double previsao, double limiar, String estrategia,
                              boolean mostrarDetalhes) {
        String unidade = sensor.getUnidade();
        String textoPrevisao = "a previsão de " + previsao + " " + unidade + " (" + estrategia + ")";
        String textoLimiar = limiar + " " + unidade;

        if (previsao >= limiar * FATOR_CRITICO) {
            System.out.println("   [!!!] CRÍTICO - " + sensor.getTipo() + ": " + textoPrevisao
                    + " ficou 20% ou mais acima do limiar de " + textoLimiar + ".");
        } else if (previsao >= limiar) {
            System.out.println("   [!!] ALERTA - " + sensor.getTipo() + ": " + textoPrevisao
                    + " atingiu o limiar de " + textoLimiar + ".");
        } else if (previsao >= limiar * FATOR_ATENCAO) {
            System.out.println("   [!] ATENÇÃO - " + sensor.getTipo() + ": " + textoPrevisao
                    + " chegou a 80% ou mais do limiar de " + textoLimiar + ".");
        } else if (mostrarDetalhes) {
            System.out.println("   Normal: " + textoPrevisao + " está abaixo de 80% do limiar de "
                    + textoLimiar + ".");
        }
    }

    // Gerenciar dados do território

    private void gerenciarDadosTerritorio() {
        int opcao;
        do {
            System.out.println("\n=== DADOS DO TERRITÓRIO ===");
            this.dadosTerritorio.exibirDados();
            System.out.println();
            System.out.println("1 - Alterar nome da região");
            System.out.println("2 - Alterar população");
            System.out.println("3 - Alterar limiar de chuva (pede código de acesso)");
            System.out.println("4 - Alterar limiar do nível do rio (pede código de acesso)");
            System.out.println("5 - Alterar limiar de umidade do solo (pede código de acesso)");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = this.lerInteiro();

            switch (opcao) {
                case 1:
                    System.out.print("Novo nome da região: ");
                    this.dadosTerritorio.setNomeRegiao(this.lerTexto());
                    break;
                case 2:
                    System.out.print("Nova população: ");
                    this.dadosTerritorio.setPopulacao(this.lerInteiro());
                    break;
                case 3:
                    System.out.print("Novo limiar de chuva em mm/h (5 a 100): ");
                    int novoLimiarChuva = this.lerInteiro();
                    System.out.print("Código de acesso: ");
                    this.dadosTerritorio.setLimiarChuva(novoLimiarChuva, this.scanner.next());
                    break;
                case 4:
                    System.out.print("Novo limiar do nível do rio em cm (50 a 500): ");
                    int novoLimiarRio = this.lerInteiro();
                    System.out.print("Código de acesso: ");
                    this.dadosTerritorio.setLimiarNivelRio(novoLimiarRio, this.scanner.next());
                    break;
                case 5:
                    System.out.print("Novo limiar de umidade do solo em % (30 a 100): ");
                    int novoLimiarUmidade = this.lerInteiro();
                    System.out.print("Código de acesso: ");
                    this.dadosTerritorio.setLimiarUmidade(novoLimiarUmidade, this.scanner.next());
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Erro: opção inválida.");
            }
        } while (opcao != 0);
    }

    // Simular próximas leituras

    private void simularProximasLeituras() {
        System.out.print("Quantas horas deseja simular (1 a 12)? ");
        int quantidade = this.lerInteiro();
        if (quantidade < 1 || quantidade > 12) {
            System.out.println("Erro: escolha entre 1 e 12 horas.");
            return;
        }

        System.out.println("\n=== NOVAS LEITURAS ===");
        for (int i = 0; i < quantidade; i++) {
            this.realizarLeituras();
        }

        // Depois das leituras o sistema verifica as previsões automaticamente
        System.out.println("\n=== VERIFICAÇÃO AUTOMÁTICA DE ALERTAS ===");
        this.verificarAlertas(true);
    }

    // Uma rodada de leituras - cada sensor ativo faz uma leitura - 1 hora
    private void realizarLeituras() {
        this.horaAtual++;
        String linha = "Hora " + this.horaAtual;

        // Polimorfismo - o mesmo lerValor() funciona para os três tipos de sensor
        for (Sensor sensor : this.sensores) {
            if (sensor.verificarFuncionamento()) {
                double valor = sensor.lerValor();
                linha = linha + " | " + sensor.getTipo() + ": " + valor + " " + sensor.getUnidade();
            } else {
                linha = linha + " | " + sensor.getTipo() + ": inativo";
            }
        }
        System.out.println(linha);
    }

    // Status completo do sistema - mostra território, sensores, previsões e alertas
    private void exibirStatusCompleto() {
        System.out.println("\n=== STATUS COMPLETO (Hora " + this.horaAtual + ") ===");

        System.out.println("\n--- Território ---");
        this.dadosTerritorio.exibirDados();

        System.out.println("\n--- Componentes instalados ---");
        // Polimorfismo - cada componente mostra o próprio status do seu jeito
        for (ComponenteTerritorial componente : this.componentes) {
            componente.verificarStatus();
        }

        System.out.println("\n--- Previsões ---");
        for (EstrategiaPrevisao estrategia : this.estrategias) {
            estrategia.exibirDescricao();
        }

        System.out.println("\n--- Níveis de alerta ---");
        System.out.println("ATENÇÃO: previsão a partir de 80% do limiar");
        System.out.println("ALERTA: previsão igual ou acima do limiar");
        System.out.println("CRÍTICO: previsão 20% ou mais acima do limiar");
    }

    // Ativar ou desativar sensor - o usuário escolhe qual sensor e se quer ativar ou desatvar

    private void ativarOuDesativarSensor() {
        System.out.println("\n=== ATIVAR OU DESATIVAR SENSOR ===");
        for (int i = 0; i < this.componentes.size(); i++) {
            ComponenteTerritorial componente = this.componentes.get(i);
            System.out.println((i + 1) + " - " + componente.getNome() + " (" + componente.getStatus() + ")");
        }
        System.out.print("Escolha o sensor: ");
        int escolha = this.lerInteiro();
        if (escolha < 1 || escolha > this.componentes.size()) {
            System.out.println("Erro: sensor inválido.");
            return;
        }

        ComponenteTerritorial componenteEscolhido = this.componentes.get(escolha - 1);
        System.out.print("1 - Ativar | 2 - Desativar: ");
        int acao = this.lerInteiro();
        if (acao == 1) {
            componenteEscolhido.ativar();
        } else if (acao == 2) {
            componenteEscolhido.desativar();
        } else {
            System.out.println("Erro: ação inválida.");
        }
    }

    // Simulação de 24 horas - faz 24 rodadas de leituras e mostra os alertas de cada hora

    private void simularVinteQuatroHoras() {
        System.out.println("\n=== SIMULAÇÃO DE 24 HORAS ===");
        System.out.println("A cada hora o sistema lê os sensores e mostra os alertas daquela hora.");
        for (int i = 0; i < 24; i++) {
            this.realizarLeituras();
            this.verificarAlertas(false); // mostra só os alertas, sem os detalhes
        }
        System.out.println("Fim da simulação. Use a opção 2 para ver as previsões detalhadas da última hora.");
    }

    // Leitura do teclado

    private int lerInteiro() {
        // Enquanto o que foi digitado não for um número inteiro pede de novo
        while (!this.scanner.hasNextInt()) {
            System.out.print("Erro: digite um número inteiro: ");
            this.scanner.next(); // descarta o que foi digitado
        }
        return this.scanner.nextInt();
    }

    private String lerTexto() {
        this.scanner.nextLine(); 
        return this.scanner.nextLine();
    }
}
