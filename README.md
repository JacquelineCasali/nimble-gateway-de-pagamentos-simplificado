## Projeto Desafio Back-end PicPay


## Descrição do projeto
O PicPay Simplificado é uma plataforma de pagamentos simplificada. 
Nela é possível depositar e realizar transferências de dinheiro entre usuários. 
Temos 2 tipos de usuários, os comuns e lojistas, ambos têm carteira com dinheiro e realizam transferências entre eles.

[//]: # (## Imagem do Projeto)

[//]: # (![Video_2024-03-19_143950]&#40;./Video_2024-03-19_143950.gif&#41;)

## ⚙️ Funcionalidades

A seguir estão algumas regras de negócio que são importantes para o funcionamento do PicPay Simplificado:

Para ambos tipos de usuário, precisamos do Nome Completo, CPF, e-mail e Senha. CPF/CNPJ e e-mails devem ser únicos no sistema. Sendo assim, seu sistema deve permitir apenas um cadastro com o mesmo CPF ou endereço de e-mail;

Usuários podem enviar dinheiro (efetuar transferência) para lojistas e entre usuários;

Lojistas só recebem transferências, não enviam dinheiro para ninguém;

Validar se o usuário tem saldo antes da transferência;

Antes de finalizar a transferência, deve-se consultar um serviço autorizador externo, use este mock https://util.devi.tools/api/v2/authorize para simular o serviço utilizando o verbo GET;

A operação de transferência deve ser uma transação (ou seja, revertida em qualquer caso de inconsistência) e o dinheiro deve voltar para a carteira do usuário que envia;

No recebimento de pagamento, o usuário ou lojista precisa receber notificação (envio de email, sms) enviada por um serviço de terceiro e eventualmente este serviço pode estar indisponível/instável. Use este mock https://util.devi.tools/api/v1/notify)) para simular o envio da notificação utilizando o verbo POST;

Este serviço deve ser RESTFul

## 🛠 Tecnologias utilizadas

- **[Java 17]**
- **[Spring Boot 3]**
- **[Maven]**
- **[h2-console]**
- **[Lombok]**
- **[Postman]**
- **[Xampp]**

## Para Clonar o projeto
https://github.com/JacquelineCasali/-Back-end-PicPay-Java.git


# 📁 Acessar a aplicação

##### Enquanto o servidor está ligado, acesse o navegador e entre no endereço abaixo

[//]: # (http://localhost:8080/swagger-ui/index.html)


## 📝 Licença

Projeto desenvolvido por CasaliTech.
