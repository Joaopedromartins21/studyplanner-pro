'''
# StudyPlanner Pro

![License](https://img.shields.io/badge/license-MIT-blue.svg)

**StudyPlanner Pro** é um sistema completo de planejamento e acompanhamento de aprendizado, projetado para ajudar estudantes a organizar suas rotinas, maximizar a produtividade e alcançar suas metas acadêmicas através de uma abordagem moderna e gamificada.

## Funcionalidades Principais

O sistema oferece um conjunto robusto de funcionalidades para uma gestão de estudos eficiente:

- **Gerenciamento de Matérias e Tópicos:** Crie, edite, visualize e delete matérias e os tópicos associados a cada uma, permitindo uma organização granular do conteúdo a ser estudado.
- **CRUD de Tarefas de Estudo:** Administre suas tarefas com status (Pendente, Em Andamento, Concluído), prioridades e prazos, garantindo que nada seja esquecido.
- **Timer de Estudo Pomodoro:** Utilize um timer integrado de 25 minutos de foco intenso seguido por 5 minutos de pausa para otimizar a concentração e evitar o esgotamento.
- **Dashboard de Progresso:** Acompanhe seu desempenho com um painel que exibe o tempo total estudado por semana, tarefas concluídas e outras métricas relevantes.
- **Autenticação Segura:** Sistema de login simples e seguro utilizando **Spring Security** com **JSON Web Tokens (JWT)** para proteger os dados do usuário.
- **Gamificação:** Mantenha-se motivado com um sistema de níveis, pontos de experiência (XP) e conquistas que recompensam seu esforço e dedicação contínua.
- **Integração com Google Calendar:** Sincronize suas tarefas e prazos diretamente com sua agenda do Google para uma visão unificada de seus compromissos.
- **Recomendações Inteligentes:** Receba sugestões de estudo com base em suas metas, progresso e áreas que necessitam de mais atenção.
- **Upload de Anotações:** Faça o upload de anotações, resumos e outros documentos em formato PDF para centralizar todo o seu material de estudo.

## Stack Tecnológico

O projeto foi construído utilizando tecnologias modernas e robustas, tanto no back-end quanto no front-end, para garantir escalabilidade, segurança e uma ótima experiência de usuário.

| Camada      | Tecnologia          | Descrição                                                                 |
|-------------|---------------------|---------------------------------------------------------------------------|
| **Back-end**  | Spring Boot 3       | Framework principal para a construção da API REST.                        |
|             | Spring Security     | Implementação da camada de segurança e autenticação com JWT e OAuth2.     |
|             | Spring Data JPA     | Persistência de dados e comunicação com o banco de dados.                 |
|             | PostgreSQL          | Banco de dados relacional para armazenamento dos dados da aplicação.      |
|             | Maven               | Gerenciador de dependências e build do projeto.                           |
| **Front-end** | React 18 (Vite)     | Biblioteca para a construção da interface de usuário reativa e moderna.   |
|             | TypeScript          | Superset do JavaScript que adiciona tipagem estática ao código.           |
|             | Tailwind CSS        | Framework CSS utility-first para estilização rápida e customizável.       |
|             | React Router        | Biblioteca para gerenciamento de rotas na aplicação single-page.          |
|             | Axios               | Cliente HTTP para realizar requisições à API do back-end.                 |
|             | Context API         | Gerenciamento de estado global para autenticação e outros dados.          |

## Como Executar o Projeto

Para executar o projeto em seu ambiente local, siga os passos abaixo.

### Pré-requisitos

- **Java 17+**
- **Maven 3.8+**
- **Node.js 18+** e **npm**
- **PostgreSQL**

### 1. Configuração do Back-end

Clone o repositório e navegue até a pasta do projeto:

```bash
git clone <URL_DO_REPOSITORIO>
cd studyplanner-pro
```

Crie um banco de dados no PostgreSQL chamado `studyplanner`.

Configure as credenciais do banco de dados e outras configurações no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/studyplanner
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# Chave secreta para JWT (gere uma chave segura)
jwt.secret=SUA_CHAVE_SECRETA_AQUI

# Credenciais do Google para a integração com o Calendar
spring.security.oauth2.client.registration.google.client-id=SEU_CLIENT_ID_DO_GOOGLE
spring.security.oauth2.client.registration.google.client-secret=SEU_CLIENT_SECRET_DO_GOOGLE
```

Execute o back-end:

```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

### 2. Configuração do Front-end

Navegue até o diretório do front-end:

```bash
cd frontend
```

Instale as dependências:

```bash
npm install
```

Crie um arquivo `.env` na raiz do diretório `frontend` e configure a URL da API:

```
VITE_API_URL=http://localhost:8080/api
```

Execute o front-end:

```bash
npm run dev
```

A aplicação estará disponível em `http://localhost:5173`.

## Estrutura da API

A API REST segue uma estrutura organizada por recursos, com endpoints para cada funcionalidade principal:

- `/api/auth`: Autenticação (registro, login).
- `/api/subjects`: CRUD de matérias.
- `/api/topics`: CRUD de tópicos.
- `/api/tasks`: CRUD de tarefas.
- `/api/sessions`: Gerenciamento de sessões de estudo (Pomodoro).
- `/api/achievements`: Listagem de conquistas.
- `/api/files`: Upload e gerenciamento de arquivos.
- `/api/recommendations`: Recomendações de estudo.

Todos os endpoints, exceto os de autenticação, são protegidos e requerem um token JWT no cabeçalho `Authorization`.

## Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
'''
