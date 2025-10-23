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
- [Hibernate Validator]
- [PostgreSQL]
- [Lombok]

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

yaml
Copiar código

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
Compile e rode o projeto:

bash
Copiar código
mvn spring-boot:run
Acesse no navegador ou via Postman:

arduino
Copiar código
http://localhost:8080
💰 Funcionalidades Principais
🔹 1. Pagamento de Cobranças
As cobranças podem ser pagas de duas formas:

✅ Pagamento por saldo
O sistema verifica se o usuário pagador tem saldo suficiente.
Em caso positivo:

Debita o valor do saldo do pagador.

Credita o valor ao destinatário.

Se não houver saldo suficiente, o pagamento é negado.

💳 Pagamento por cartão de crédito
Integra com o autorizador externo via GET:
bash
Copiar código
https://zsy6tx7aql.execute-api.sa-east-1.amazonaws.com/authorizer
Campos obrigatórios:
numeroCartao
dataExpiracao
cvv

Apenas se o autorizador retornar “APROVADO” o pagamento é efetivado.

🔹 2. Depósito de Saldo
Usuário pode fazer um depósito em sua conta.
O sistema consulta o autorizador externo antes de confirmar o depósito.
Se autorizado, o saldo é creditado na conta do usuário.

🔹 3. Cancelamento de Cobranças
🕓 Cobrança pendente:
Apenas muda o estado para “CANCELADA”.



💳 Cobrança paga com cartão:
O sistema consulta o autorizador externo.
Se autorizado, o cancelamento é concluído.

🔹 4. Integração Externa — Autorizador
O sistema faz chamadas GET para:

bash
Copiar código
https://zsy6tx7aql.execute-api.sa-east-1.amazonaws.com/authorizer
O autorizador pode responder:



✅ Validações Implementadas
Valor de cobrança e depósito deve ser positivo.
Verificação de saldo suficiente antes de pagamentos via saldo.
Cancelamento somente de cobranças pendentes .
Pagamentos e depósitos só confirmam após autorização externa.

💡 Dica extra

Se quiser testar se um CPF é válido, use sites confiáveis como:

https://www.4devs.com.br/gerador_de_cpf

https://www.geradordecpf.org

Eles geram CPFs válidos (para testes), com dígitos verificadores corretos.





👩‍💻 Autoria
Desenvolvido por Jacqueline Casali
