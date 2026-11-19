# Territory Predictive Insights
Global Solution 2º semestre de 2026  Object-Oriented Programming

## Integrantes
Daniel Silva de Castro - RM 567894
Guilherme Blanco - RM 566746
Cauã Ceolin - RM 567328

TURMA 2CCPS

## Solução e público beneficiado

O recorte escolhido foi o climático, com chuva intensa e risco de alagamento. O sistema atende os moradores das partes baixas e das margens do córrego de uma comunidade que alaga em chuvas fortes, que perdem móveis, documentos e dias de trabalho quando a água entra em casa e hoje só ficam sabendo do risco quando a rua já está cheia. Quem usa o sistema é a coordenação local (associação de moradores, agentes comunitários e Defesa Civil do bairro), e a decisão que ele apoia é se vale agir antes da água subir, limpando bueiros, avisando as famílias das áreas de margem ou acionando a Defesa Civil com algumas horas de antecedência.

## Como executar

Requisito: JDK 8 ou superior.

Pelo terminal, dentro da pasta do projeto:

```bash
javac -encoding UTF-8 *.java
java SistemaMonitoramento
```

Código de acesso para alterar os limiares de alerta: **2026**.

## Decisões da equipe

- Três sensores que juntos indicam o risco de alagamento, com chuva (mm/h), nível do córrego (cm) e umidade do solo da encosta (%), porque solo encharcado não absorve a chuva e a água desce para as partes baixas.
- Os sensores são os componentes do território. Cada sensor herda de `ComponenteTerritorial` e implementa `Sensor`.
- Limiares protegidos em `DadosTerritorio`: eles só mudam com o código de acesso. O nível do rio é medido em centímetros para que todos os limiares sejam números inteiros no menu.
- Níveis de alerta: ATENÇÃO a partir de 80% do limiar, ALERTA quando a previsão atinge o limiar e CRÍTICO quando fica 20% ou mais acima dele. O alerta usa a maior das duas previsões por precaução.
- Simulação com `Math.random()`. Cada leitura parte do valor da hora anterior e soma uma variação aleatória, com pancadas de chuva de vez em quando, para que a média móvel e a comparação de períodos façam sentido.
- Bônus: simulação de 24 horas (opção 7 do menu), com os alertas aparecendo hora a hora.
- Estrutura e dados: todos os arquivos ficam na mesma pasta, e o território é descrito por região e equipamento.

## Reflexão crítica

O sistema deve se calar sempre que não tem base para afirmar alguma coisa, porque uma previsão errada apresentada como certa é pior do que nenhuma previsão. Com menos leituras do que o método exige (5 na média móvel e 6 na comparação de períodos), uma única pancada de chuva pesa demais na conta e gera alarme falso, por isso o programa mostra "sem previsão" e informa quantas leituras faltam, em vez de mostrar um número que seria lido como ausência de risco. O sistema também se cala quando o sensor está desativado, já que uma previsão feita com leituras antigas daria uma falsa sensação de segurança justamente quando o equipamento não está medindo.

O que os dados não representam: três sensores medem três pontos de um território inteiro, então o pluviômetro da UBS não mostra a chuva do outro lado do morro e a régua do córrego não mostra o bueiro entupido que alaga uma rua duas quadras acima. Os dados também não mostram quem mora nas casas mais baixas, quem tem dificuldade para sair de casa ou se o ponto de apoio está aberto. Como os valores são simulados e os limiares foram escolhidos sem um histórico de alagamentos da Defesa Civil, por exemplo, o sistema mostra uma tendência e não a situação real de cada rua.

O alerta apoia a decisão da pessoa, em vez de decidir por ela, porque quem coordena a resposta sabe quais ruas enchem primeiro, onde moram as pessoas que precisam de ajuda para sair e quanto custa para uma família deixar a casa à noite por um alarme falso, e nenhum sensor consegue alcançar isso. Por isso cada alerta mostra o sensor, o valor previsto, a estratégia que gerou o número e o limiar atingido, para que a pessoa compare com o que está vendo na rua e decida o que fazer. Um sistema que decidisse sozinho perderia a confiança da comunidade depois de poucos alarmes falsos, e a decisão precisa ficar com pessoas que respondem por ela.

## Uso de IA

Foram usados Claude e Gemini como apoio para a pesquisa de definição da solução e ferramente de revisão dos códigos.

## Divisão de tarefas

Cauã - Sensores - `ComponenteTerritorial`, `Sensor`, `SensorChuva`, `SensorNivelRio`, `SensorUmidade` 
 
Guilherme - Previsões - `EstrategiaPrevisao`, `PrevisaoMediaMovel`, `PrevisaoComparacao` 

Daniel - Dados do território, menu e alertas - `DadosTerritorio`, `SistemaMonitoramento` 
