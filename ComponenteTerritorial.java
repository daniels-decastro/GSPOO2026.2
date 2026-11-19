// Abstrata porque não existe um componente genérico no território, só tipos específicos de sensor.
public abstract class ComponenteTerritorial {

    // Atributos comuns a todos os componentes
    private String id;
    private String nome;
    private String regiao;
    private String status; //ATIVO ou INATIVO

    // O construtor existe, mas só é chamado pelas filhas pelo super()
    public ComponenteTerritorial(String id, String nome, String regiao) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.status = "ATIVO";
    }

    // Métodos concretos - funcionam do mesmo jeito para todos os componentes
    public void ativar() {
        this.status = "ATIVO";
        System.out.println(this.nome + " foi ativado.");
    }

    public void desativar() {
        this.status = "INATIVO";
        System.out.println(this.nome + " foi desativado e não fará novas leituras.");
    }

    // Métato - cada componente verifica e mostra o próprio status do seu jeito.

    public abstract void verificarStatus();

    // Getters 
    public String getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getRegiao() {
        return this.regiao;
    }

    public String getStatus() {
        return this.status;
    }
}
