# Guia de Instalação e Uso - StudyPlanner Pro

Bem-vindo ao **StudyPlanner Pro**! Este guia irá ajudá-lo a configurar e executar o sistema completo de planejamento e acompanhamento de aprendizado em seu computador.

## Requisitos do Sistema

Antes de começar, certifique-se de ter os seguintes softwares instalados em seu computador:

- **Java Development Kit (JDK) 17 ou superior**
- **Maven 3.8 ou superior**
- **Node.js 18 ou superior** (inclui npm)
- **PostgreSQL** (banco de dados)
- **Git** (para clonar o repositório)

## Passo 1: Clonar o Repositório

Abra o terminal e execute o seguinte comando para clonar o projeto:

```bash
git clone https://github.com/Joaopedromartins21/studyplanner-pro.git
cd studyplanner-pro
```

## Passo 2: Configurar o Banco de Dados PostgreSQL

### 2.1. Criar o Banco de Dados

Acesse o PostgreSQL e crie um banco de dados chamado `studyplanner`:

```sql
CREATE DATABASE studyplanner;
```

### 2.2. Configurar as Credenciais

Abra o arquivo `src/main/resources/application.properties` e ajuste as seguintes linhas com suas credenciais do PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/studyplanner
spring.datasource.username=seu_usuario_postgres
spring.datasource.password=sua_senha_postgres
```

### 2.3. Configurar JWT Secret

Gere uma chave secreta segura para o JWT (você pode usar um gerador online) e substitua no arquivo:

```properties
jwt.secret=SUA_CHAVE_SECRETA_AQUI
```

### 2.4. (Opcional) Configurar Google Calendar

Se você deseja usar a integração com o Google Calendar, será necessário:

1. Criar um projeto no [Google Cloud Console](https://console.cloud.google.com/)
2. Habilitar a API do Google Calendar
3. Criar credenciais OAuth 2.0
4. Adicionar as credenciais no `application.properties`:

```properties
spring.security.oauth2.client.registration.google.client-id=SEU_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=SEU_CLIENT_SECRET
```

## Passo 3: Executar o Back-end

No diretório raiz do projeto, execute:

```bash
mvn spring-boot:run
```

O servidor back-end será iniciado em `http://localhost:8080`.

Você verá mensagens no console indicando que a aplicação está rodando. Aguarde até ver algo como:

```
Started StudyPlannerApplication in X.XXX seconds
```

## Passo 4: Configurar e Executar o Front-end

### 4.1. Navegar para o Diretório do Front-end

Abra um **novo terminal** e execute:

```bash
cd frontend
```

### 4.2. Instalar as Dependências

```bash
npm install
```

### 4.3. Configurar a URL da API

Crie um arquivo `.env` no diretório `frontend` com o seguinte conteúdo:

```
VITE_API_URL=http://localhost:8080/api
```

### 4.4. Executar o Front-end

```bash
npm run dev
```

O front-end será iniciado em `http://localhost:5173`.

## Passo 5: Acessar a Aplicação

Abra seu navegador e acesse:

```
http://localhost:5173
```

Você verá a tela de login do **StudyPlanner Pro**!

### Criar uma Conta

1. Clique em **"Registre-se"**
2. Preencha os campos:
   - Usuário
   - Email
   - Senha (mínimo 6 caracteres)
3. Clique em **"Registrar"**

Você será automaticamente redirecionado para o dashboard!

## Como Usar o Sistema

### Dashboard

No dashboard, você verá:

- **Nível e Experiência (XP):** Seu progresso no sistema de gamificação
- **Estatísticas da Semana:** Tempo total estudado, número de pomodoros e sessões
- **Timer Pomodoro:** Use para cronometrar suas sessões de estudo (25 min foco + 5 min pausa)
- **Ações Rápidas:** Acesso rápido às principais funcionalidades

### Timer Pomodoro

1. Clique em **"Iniciar"** para começar uma sessão de 25 minutos
2. Foque nos seus estudos durante o timer
3. Quando terminar, você ganhará XP automaticamente
4. Após 25 minutos, o sistema sugere uma pausa de 5 minutos
5. Use **"Pausar"** para interromper temporariamente
6. Use **"Resetar"** para reiniciar o timer

### Sistema de Gamificação

- **Ganhe XP** completando tarefas e sessões de estudo
- **Suba de nível** acumulando experiência
- **Desbloqueie conquistas** atingindo marcos específicos
- Cada nível requer 100 XP × nível atual

### Matérias e Tópicos

*(Funcionalidade completa disponível no código - interface pode ser expandida)*

- Crie matérias para organizar seus estudos
- Adicione tópicos específicos dentro de cada matéria
- Marque tópicos como concluídos conforme avança

### Tarefas

- Crie tarefas com título, descrição, prioridade e prazo
- Organize por status: Pendente, Em Andamento, Concluído
- Ganhe XP ao completar tarefas

### Upload de Arquivos

- Faça upload de PDFs e outros documentos
- Anexe arquivos às suas tarefas e anotações
- Centralize todo seu material de estudo

## Solução de Problemas

### Erro ao conectar com o banco de dados

- Verifique se o PostgreSQL está rodando
- Confirme que as credenciais no `application.properties` estão corretas
- Certifique-se de que o banco `studyplanner` foi criado

### Erro ao fazer login

- Verifique se o back-end está rodando em `http://localhost:8080`
- Confirme que você criou uma conta primeiro
- Verifique o console do navegador para mensagens de erro

### Front-end não carrega

- Certifique-se de que executou `npm install` no diretório `frontend`
- Verifique se o arquivo `.env` foi criado corretamente
- Confirme que não há outro processo usando a porta 5173

## Tecnologias Utilizadas

- **Back-end:** Spring Boot, Spring Security, JWT, PostgreSQL
- **Front-end:** React, TypeScript, Tailwind CSS, Vite
- **Integrações:** Google Calendar API

## Suporte

Se você encontrar problemas ou tiver dúvidas, consulte o arquivo `README.md` ou abra uma issue no repositório do GitHub.

---

**Bons estudos! 📚✨**
