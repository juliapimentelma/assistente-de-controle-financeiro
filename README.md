# Coop Finances | Assistente Financeiro Pessoal com IA

[![License](https://img.shields.io/badge/license-MIT-green)](https://opensource.org/licenses/MIT)
[![Angular](https://img.shields.io/badge/Angular_v20-DD0031?style=flat&logo=angular&logoColor=white)](https://angular.io/)
[![Spring](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![AI](https://img.shields.io/badge/OpenAI_LLM-000000?style=flat&logo=openai&logoColor=white)](#)

Uma plataforma Full-Stack de controle financeiro pessoal (Pessoa Física - CPF) projetada para ir além das planilhas tradicionais. O sistema permite o gerenciamento completo de receitas, despesas, orçamentos e metas, integrando uma Inteligência Artificial interativa que avalia hábitos de consumo e ajuda o usuário a tomar decisões financeiras mais inteligentes no dia a dia.

---

## Tecnologias Utilizadas

### Frontend
![Angular](https://img.shields.io/badge/angular_20-%23DD0031.svg?style=for-the-badge&logo=angular&logoColor=white) ![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white) ![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)
* **Angular v20:** Arquitetura moderna utilizando Standalone Components, o novo Control Flow e Signals para reatividade e performance otimizada.
* **Design Customizado (Dark UI):** Interface construída 100% do zero, utilizando estética Dark Mode de alto contraste com detalhes em verde-limão e roxo, focando em uma experiência de usuário imersiva.
* **RxJS / Observables:** Consumo assíncrono e gerenciamento de estado transparente das APIs do backend.

### Backend
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
* **Spring Boot & Java:** Construção de uma API RESTful robusta, com orquestração de cálculos financeiros e controle de parcelamentos.
* **Spring Data JPA:** Persistência relacional complexa (transações unificadas por grupos, categorias pai/filha, soft deletes).
* **Integração LLM:** Consumo de APIs de Inteligência Artificial para análise semântica e respostas dinâmicas no chat.
* **Segurança:** Autenticação via JWT Bearer Token, APIs Stateless e encriptação de dados sensíveis com `BCryptPasswordEncoder`.

---

## Funcionalidades e Interface

* **Módulo "Posso Comprar?":** Uma interface de chat inovadora (estilo *mad-libs*) onde o usuário insere o item e o valor que deseja gastar. A IA avalia o pedido em tempo real e emite um veredito baseado na saúde financeira.
* **Personalidade Adaptável da IA:** O usuário pode configurar o tom das respostas do assistente, alternando entre "Conselheiro" (educativo e gentil) e "Sargento" (linha dura e direto).
* **Motor de Transações:** Lançamento de despesas e receitas com suporte avançado a parcelamentos e agrupamento de dívidas longo prazo.
* **Orçamentos e Metas:** Definição de limites mensais por categoria e acompanhamento de metas financeiras progressivas.
* **Gestão de Perfil & Segurança:** Tela de configurações para edição de credenciais e Zona de Perigo (inativação lógica da conta preservando a integridade contábil do banco).

---

## Arquitetura e Padrões de Projeto (Backend)

O projeto adota uma **Arquitetura em Camadas (Layered Architecture)** para escalabilidade e manutenção segura dos dados dos usuários.

* **Controller:** Camada exclusiva para lidar com requests HTTP e delegar chamadas (`@RestController`).
* **Service:** Encapsulamento das regras de negócio (validações de limite, senhas e lógicas de IA).
* **Repository:** Comunicação com o banco de dados PostgreSQL.

### Práticas Adotadas
* **Injeção de Dependências:** Via construtor utilizando `@RequiredArgsConstructor` do Lombok.
* **Separação de Dados (DTOs):** Utilização intensiva de `Records` do Java para separar os modelos de banco de dados (`Entities`) do que trafega na rede.
* **Fail-fast:** Validação via anotações (`@Valid`, `@NotBlank`, etc.) direto na entrada dos endpoints.

---

## Como executar o projeto localmente

### 1. Pré-requisitos
* **Java 18** (ou superior).
* **Node.js** e **Angular CLI (v20)**.
* **PostgreSQL** rodando localmente (porta 5432).
* **Chave de API (OpenAI/LLM)** para o funcionamento do "Posso Comprar?".

### 2. Configurando o Backend
1. Navegue até a pasta da API.
2. Configure as credenciais no `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/coopfinances
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ai.llm.api-key=sua_chave_de_api_aqui
3. Rode a aplicação. As tabelas relacionais serão sincronizadas automaticamente pelo Hibernate.

### 3. Configurando o Frontend
1. Acesse a pasta do projeto Angular via terminal.
2. Instale as dependências:
   ```
   npm install
   ```
3. Suba o servidor local:
   ```
   ng serve
   ```


4. Acesse o assistente financeiro em: `http://localhost:4200`

---

## Referência Rápida da API

Abaixo estão os endpoints centrais da aplicação. Todas as rotas (exceto autenticação) exigem o header `Authorization: Bearer <token>`.

### Autenticação & Usuários

| Método | Rota | Funcionalidade |
| --- | --- | --- |
| **POST** | `/api/auth/registrar` | Criação de conta de usuário com configurações default. |
| **POST** | `/api/auth/login` | Autenticação e geração do Token JWT. |
| **GET** | `/api/usuarios/me` | Retorna os dados do perfil logado (Nome, E-mail, Tom da IA). |
| **PUT** | `/api/usuarios/me` | Atualização de Perfil (Nome e Preferências da IA). |
| **PUT** | `/api/usuarios/me/credenciais` | Atualização segura de senha com validação dupla. |
| **DELETE** | `/api/usuarios/me/desativar` | Soft Delete (Zona de Perigo). |

### Finanças

| Método | Rota | Funcionalidade |
| --- | --- | --- |
| **GET** | `/api/transacoes` | Listagem dinâmica do extrato do usuário. |
| **POST** | `/api/transacoes` | Lançamento de nova despesa/receita (única ou parcelada). |
| **GET** | `/api/orcamentos` | Resumo de gastos x limite planejado no mês. |
| **POST** | `/api/conselheiro/posso-comprar` | Envio dos dados da compra para avaliação do LLM. |

---

## Desenvolvedora:
* Júlia Pimentel

