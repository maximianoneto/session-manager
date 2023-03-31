<h2>Descrição do projeto</h2>
<p>Este projeto foi desenvolvido em Java 11 e Spring Boot, utilizando o banco de dados MySQL. O objetivo deste desafio técnico é desenvolver um sistema para gerenciamento de sessões de votação. O projeto inclui uma API para criar pautas, abrir sessões de votação, registrar votos e obter resultados da votação.</p>

<h2>Como rodar localmente</h2>
<p>Para rodar o projeto localmente, siga os seguintes passos:</p>

<ol>
    <li>Clone este repositório em sua máquina:</li>
    <li>git clone https://github.com/maximianoneto/seu-repositorio.git</li>
    <li>Certifique-se de ter o Java 11 e o Maven instalados em sua máquina.</li>
    <li>Utilize o usuário e senha do banco de dados no arquivo <code>src/main/resources/application.properties</code>.</li>
	<li>Baixe e instale o XAMPP, que será utilizado para conectar a aplicação ao banco de dados local. Você pode baixá-lo em https://www.apachefriends.org/pt_br/index.html.</li>
	<li>Abra o XAMPP e inicie os serviços de Apache e MySQL.</li>
    <li>Abra o navegador e acesse http://localhost/phpmyadmin/. Clique em "Novo" e crie um banco de dados com o nome "session_manager".</li>
    <li>Execute o comando <code>mvn clean install</code> para instalar as dependências do projeto.</li>
    <li>Execute o comando <code>mvn spring-boot:run</code> para iniciar a aplicação.</li>
    <li>Acesse o site oficial do RabbitMQ em https://www.rabbitmq.com/download.html e baixe a versão adequada para o seu sistema operacional.</li>
    <li>Instale o RabbitMQ de acordo com as instruções específicas do seu sistema operacional.</li>
    <li> Execute o seguinte comando para verificar se o RabbitMQ está instalado corretamente: </li>
    <li> sc query RabbitMQ </li>
    <li> Caso o serviço não esteja em execução, você pode iniciar o serviço do RabbitMQ usando o seguinte comando: </li>
    <li> net start RabbitMQ </li>
    <li>A aplicação deve iniciar e estará disponível em http://localhost:8080/api/pautas</li>
</ol>

<h2> Explicação das Escolhas Tomadas durante o processo de Desenvolvimento da Solução </h2>
<p>O projeto em questão é um sistema de gerenciamento de votação em pautas, desenvolvido em Java com o framework Spring. Ele é composto por vários módulos, incluindo a API REST, o serviço de gerenciamento de votos, o serviço de gerenciamento de pautas e o serviço de envio de mensagens para o RabbitMQ.

O objetivo do sistema é permitir que os usuários possam criar e votar em pautas, com validação do CPF do votante e contabilização dos votos em tempo real. O sistema também publica o resultado da votação no RabbitMQ, permitindo que outras partes do sistema possam consumir o resultado em tempo real.

Algumas das principais características e tecnologias utilizadas no projeto incluem:

Utilização do framework Spring: o projeto utiliza o framework Spring para gerenciamento de dependências, injeção de dependência e criação de endpoints REST.

Utilização do banco de dados MySQL: o projeto utiliza o banco de dados MySQL para armazenamento de dados, incluindo informações sobre as pautas e votos registrados.

Validação do CPF do votante: o sistema utiliza um serviço externo para validar o CPF do votante antes de registrar o voto, garantindo que apenas votantes aptos possam participar da votação.

Verificação do encerramento da sessão de votação: antes de registrar o voto, o sistema verifica se a sessão de votação da pauta está encerrada. Se estiver, o sistema encerra a sessão de votação e publica o resultado da votação no RabbitMQ.

Utilização do padrão de projeto Repository: o projeto utiliza o padrão de projeto Repository para abstrair a camada de acesso a dados, tornando o sistema mais modular e fácil de testar.

Utilização de testes unitários e integrados: o projeto utiliza testes unitários e integrados para garantir a qualidade e integridade do código.</p>

<h2>Estrutura do projeto</h2>
<p>O projeto segue a estrutura padrão do Spring Boot MVC, dividido em camadas de controller, service e repository. Além disso, foram adicionadas as seguintes dependências:</p>

<ul>
	<li>spring-boot-starter-data-jpa</li>
	<li>spring-boot-starter-web</li>
	<li>spring-boot-starter-webflux</li>
	<li>spring-boot-starter-amqp</li>
	<li>mysql-connector-java</li>
	<li>spring-boot-starter-test</li>
	<li>spring-security-oauth2-autoconfigure</li>
	<li>spring-security-oauth2-client</li>
	<li>spring-security-oauth2-jose</li>
	<li>httpclient</li>
	<li>gson</li>
	<li>lombok</li>
	<li>commons-lang3</li>
	<li>jackson-databind</li>
	<li>junit-jupiter-engine</li>
	<li>powermock-module-junit4</li>
	<li>powermock-api-mockito2</li>
	<li>mockito-junit-jupiter</li>
</ul>

<h2>Endpoints da API</h2>
<p>API possui os seguintes endpoints:</p>

<ul>
	<li>POST /api/pautas/name/ - Cria uma nova pauta.</li>
     <p>Exemplo de requisição:</p>
     <pre>
     {
	"descricao":"Lorem ipsum",
	"titulo":"Ajuste na contabilidade"
     }
     </pre>
     <p>Exemplo de resposta:</p>
     <pre>
     {
	"id": 31,
	"descricao": "Lorem ipsum",
	"titulo": "Ajuste na contabilidade",
	"sessaoAberta": false,
	"duracaoSessao": null,
	"dataHoraInicioSessao": "2023-03-30T15:01:20.1506343"
     }
     </pre>
	<li>POST /api/pautas/{pautaId}/abrir-sessao - Abre uma sessão de votação para uma pauta.</li>
     <p>Exemplo de requisição:</p>
     <pre>
     /api/pautas/31/abrir-sessao?duracaoSessao=PT1M
     </pre>
     <p>Exemplo de resposta:</p>
     <pre>
     {
	"id": 31,
	"descricao": "Lorem ipsum",
	"titulo": "Ajuste na contabilidade",
	"sessaoAberta": true,
	"duracaoSessao": "PT1M",
	"dataHoraInicioSessao": "2023-03-31T15:01:26.8167335"
     }
     </pre>
	<li>POST /api/pautas/{pautaId}/votar - Registra um voto em uma pauta.</li>
     <p>Exemplo de requisição:</p>
     <pre>
     /api/pautas/31/votar
     {
	"idAssociado": 17,
	"voto": true,
	"cpf": 6258951422
     }
     </pre>
     <p>Exemplo de resposta:</p>
     <pre>
     {
	"id": 431,
	"idAssociado": 17,
	"voto": true,
	"pauta": {
		"id": 31,
		"descricao": "Pauta30",
                "titulo":"Elevação de Status",
                "sessaoAberta":true,
                "duracaoSessao":"PT1M",
                "dataHoraInicioSessao":"2023-03-30T15:01:26"},
        "cpf":6258951422
    }
</code>
</pre>

<li>GET /api/pautas/{pautaId}/resultado - Retorna o resultado da votação de uma pauta.</li>

<pre><code>GET /api/pautas/31/resultado
Resposta: HTTP/1.1 200 OK


{
    "votosSim":153,
    "votosNao":147
}</code></pre>

<h2>Integração com sistema externo</h2>
<p>Como uma das tarefas bônus, realizamos a integração com um sistema externo que valida CPF. Utilizamos a aplicação <a href="https://github.com/maximianoneto/cpf-validator" target="_blank">https://github.com/maximianoneto/cpf-validator</a>, que está deployada na Vercel para ser nossa API de integração.</p>
<p>Para validar um CPF, basta fazer uma requisição HTTP POST para o endpoint <code>https://cpf-validator-nextjs.vercel.app/api/cpf</code>, passando o "CPF" no body da requisição a ser validado. 
<p> Exemplo: </p>

<pre>
{ 
  "cpf": "684.369.022-03" 
}
</pre>


</p> API retornará um objeto JSON com o resultado da validação.</ul>



<h2>Performance</h2>
<p>Realizei testes de performance utilizando a ferramenta JMETER para medir como a API se comporta com um alto número de requisições por segundo. Por conta da integração externa com a API na Vercel, temos um gargalo quando as requisições passam de 500/Minutos, pois a API da Vercel, por ser gratuita, tem um limite de requisições por segundo.</p>

<h2>Versionamento da API</h2>
<p>Para versionar a API, utilizei a estratégia de versionamento no Swagger. Cada versão da API possui sua própria documentação gerada automaticamente pelo Swagger e é acessível por meio de um endpoint específico. Para acessar a documentação da versão atual, acesse <code>http://localhost:8080/swagger-ui.html</code>. Para acessar a documentação de uma versão anterior, substitua o número da versão no endpoint: <code>http://localhost:8080/swagger-ui.html?v=1</code>.</p>
