import java.util.List;

// Contrato de todo sensor. Diz O QUE o sensor faz, e cada classe de sensor decide COMO.
public interface Sensor {

    // Faz nova leitura simulada, guarda no histórico e devolve o valor lido
    double lerValor();

    // Diz se o sensor está funcionando
    boolean verificarFuncionamento();

    // Retorna o tipo do sensor 
    String getTipo();

    // Retorna a unidade de medida da leitura 
    String getUnidade();

    // Retorna as leituras guardadas, da mais antiga pra mais recente
    List<Double> getLeituras();
}
