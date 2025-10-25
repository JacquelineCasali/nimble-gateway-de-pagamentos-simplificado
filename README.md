# 💳 Nimble Gateway de Pagamentos Simplificado

Sistema de **gateway de pagamentos** desenvolvido em **Java + Spring Boot**, com suporte a:
- Pagamentos de cobranças via **saldo interno** ou **cartão de crédito**.
- **Depósitos de saldo** com validação via autorizador externo.
- **Cancelamento de cobranças** com estorno automático (quando aplicável).
- Integração com um **serviço externo de autorização** via requisições HTTP GET.

---

## 🚀 Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3**
- **Spring Data JPA**
- **Hibernate Validator**
- **PostgreSQL**
- **Lombok**
- ** JUnit 5 + Mockito **
- ** Swagger (Springdoc OpenAPI)**
---

## ⚙️ Estrutura do Projeto

src/main/java/com/nimble
├── controller/ # Controladores REST
├── dto/ # Objetos de transferência de dados
├── entity/ # Entidades JPA
├── infra/security/ # JWT e autenticação
├── repository/ # Interfaces de repositório
├── service/ # Regras de negócio
└── NimbleApplication # Classe principal

---

## 💾 Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- [Java 17+]
- [Maven 3.8+]
-  [Postman]
---

## ▶️ Como Rodar o Projeto

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/JacquelineCasali/nimble-gateway-de-pagamentos-simplificado.git
   cd nimble-gateway-de-pagamentos-simplificado
   
2. ** Configure o banco de dados no arquivo application.yml:
spring:
datasource:
url: jdbc:postgresql://localhost:5432/nimble
username: postgres
password: sua_senha
jpa:
hibernate:
ddl-auto: update
show-sql: true

3. Compile e rode o projeto:

mvn spring-boot:run
Acesse no navegador ou via Postman:
4. Acesse a API no postman ou no swagger 

http://localhost:8080

5. Acesse a API no swagger
http://localhost:8080/swagger-ui/index.html

## 💰 Funcionalidades Principais


🔹 1. Gerenciamento de Usuários
- Cadastro : nome ,cpf , email, senha (segura)
- Login: Cpf ou email + senha que retorna o token JWT

🔹 2. Gestão de Cobrancças
- Cria cobranças de um usuário para outro via CPF
- Consulta cobranças enviadas ou recebidas , filtrando por status

🔹 3.Pagamento de Cobranças

As cobranças podem ser pagas de duas formas:

✅ Pagamento por saldo interno 
O sistema verifica se o usuário pagador tem saldo suficiente.
Em caso positivo:
Debita o valor do saldo do pagador.
Credita o valor ao destinatário.
Se não houver saldo suficiente, o pagamento é negado.

#💳 Pagamento por cartão de crédito
Integra com o autorizador externo via GET:
bash
Copiar código
https://zsy6tx7aql.execute-api.sa-east-1.amazonaws.com/authorizer
Campos obrigatórios:
numeroCartao
dataExpiracao
cvv

Apenas se o autorizador retornar “APROVADO” o pagamento é efetivado.

🔹 4. Cancelamento de Cobranças
- 🕓 Cobrança pendente: Apenas muda o estado para “CANCELADA”.
- Pagas com saldo :estorno automatico 
- Pagas com cartao consulta autorização antes de cancelar 

✅ Validações Implementadas
Valor de cobrança e depósito deve ser positivo.
Verificação de saldo suficiente antes de pagamentos via saldo.
Validação de CPF
Pagamentos e depósitos só confirmam após autorização externa.

💡 Dica extra
Se quiser testar se um CPF é válido, use sites confiáveis como:
https://www.4devs.com.br/gerador_de_cpf
https://www.geradordecpf.org
Eles geram CPFs válidos (para testes), com dígitos verificadores corretos.

## Teste Unitários 

👩‍💻 Autoria
Desenvolvido por Jacqueline Casali
