// Toda alteração passa por um setter que valida o valor antes de aceitar,
// e os limiares de alerta só mudam com o código de acesso.
public class DadosTerritorio {

    private String nomeRegiao;
    private int populacao;
    private double limiarChuva;    // mm/h
    private double limiarNivelRio; // cm
    private double limiarUmidade;  // %
    private String codigoAcesso;   // dado sensível e por isso não existe getCodigoAcesso()

    public DadosTerritorio(String nomeRegiao, int populacao, String codigoAcesso) {
        // Setters para validar os dados no nascimento do objeto
        this.setNomeRegiao(nomeRegiao);
        this.setPopulacao(populacao);
        this.codigoAcesso = codigoAcesso;

        // Limiares iniciais definidos
        this.limiarChuva = 30;     // chuva forte que já sobrecarrega os bueiros
        this.limiarNivelRio = 300; // 1 metro abaixo da cota em que o córrego transborda (400 cm)
        this.limiarUmidade = 80;   // solo perto de ficar encharcado
    }

    // Getters - apenas leitura

    public String getNomeRegiao() {
        return this.nomeRegiao;
    }

    public int getPopulacao() {
        return this.populacao;
    }

    public double getLimiarChuva() {
        return this.limiarChuva;
    }

    public double getLimiarNivelRio() {
        return this.limiarNivelRio;
    }

    public double getLimiarUmidade() {
        return this.limiarUmidade;
    }

    // Setters com validação de dados. Se o valor for inválido, mostra o erro e sai com return, matendo o valor anterior.
    

    public void setNomeRegiao(String novoNome) {
        if (novoNome == null || novoNome.trim().length() < 3) {
            System.out.println("Erro: o nome da região precisa ter pelo menos 3 letras. Valor anterior mantido.");
            return;
        }
        this.nomeRegiao = novoNome.trim();
    }

    public void setPopulacao(int novaPopulacao) {
        if (novaPopulacao <= 0) {
            System.out.println("Erro: a população precisa ser maior que zero. Valor anterior mantido.");
            return;
        }
        this.populacao = novaPopulacao;
    }

    public void setLimiarChuva(double novoLimiar, String codigoInformado) {
        if (!this.codigoCorreto(codigoInformado)) {
            return;
        }
        if (novoLimiar < 5 || novoLimiar > 100) {
            System.out.println("Erro: o limiar de chuva deve ficar entre 5 e 100 mm/h. Valor anterior mantido.");
            return;
        }
        this.limiarChuva = novoLimiar;
    }

    public void setLimiarNivelRio(double novoLimiar, String codigoInformado) {
        if (!this.codigoCorreto(codigoInformado)) {
            return;
        }
        if (novoLimiar < 50 || novoLimiar > 500) {
            System.out.println("Erro: o limiar do nível do rio deve ficar entre 50 e 500 cm. Valor anterior mantido.");
            return;
        }
        this.limiarNivelRio = novoLimiar;
    }

    public void setLimiarUmidade(double novoLimiar, String codigoInformado) {
        if (!this.codigoCorreto(codigoInformado)) {
            return;
        }
        if (novoLimiar < 30 || novoLimiar > 100) {
            System.out.println("Erro: o limiar de umidade do solo deve ficar entre 30 e 100 %. Valor anterior mantido.");
            return;
        }
        this.limiarUmidade = novoLimiar;
    }

    // Mostra os dados do território; o código de acesso nunca é exibido.
    public void exibirDados() {
        System.out.println("Região: " + this.nomeRegiao);
        System.out.println("População: " + this.populacao + " moradores");
        System.out.println("Limiar de chuva: " + this.limiarChuva + " mm/h");
        System.out.println("Limiar do nível do rio: " + this.limiarNivelRio + " cm");
        System.out.println("Limiar de umidade do solo: " + this.limiarUmidade + " %");
        System.out.println("Código de acesso: protegido");
    }

    // Método privado; só essa classe usa para conferir o código antes de mexer nos limiares
    private boolean codigoCorreto(String codigoInformado) {
        if (this.codigoAcesso.equals(codigoInformado)) {
            return true;
        }
        System.out.println("Erro: código de acesso incorreto. Valor anterior mantido.");
        return false;
    }
}
