# New Mottu Gestor

O Mottu Gestor é uma solução inteligente de mapeamento e gestão de pátios para frotas de motos. Utilizando RFID (simulado), nosso sistema oferece rastreamento em tempo real, otimização operacional e segurança automatizada, reduzindo custos e aumentando a eficiência da Mottu.

---

## 🚀 Acesso à Aplicação em Produção (Deploy no Render)

A aplicação está implantada na plataforma Render e pode ser acessada publicamente. O login é feito via autenticação OAuth2 do GitHub.

**Link de Acesso:** **[https://new-mottu-gestor.onrender.com/login](https://new-mottu-gestor.onrender.com/login)**

---

## 🌟 Destaques da Solução

- ✅ **Gestão de Pátios (CRUD):** Permite cadastrar, visualizar, editar e excluir os pátios da Mottu.
- ✅ **Gestão de Leitores (CRUD):** Permite cadastrar, visualizar, editar e excluir os leitores de RFID, associando cada um a um pátio.
- ✅ **Dashboard de Pátio:** Exibe um dashboard com o total de motos no pátio (via Procedure Oracle), as últimas movimentações (do MongoDB) e um gráfico de distribuição de modelos.
- ✅ **Log de Movimentações (MongoDB):** Um log de alta performance para todas as movimentações, com filtros dinâmicos (data, placa, modelo, tipo, pátio, leitor) e paginação, lendo diretamente do MongoDB.
- ✅ **Gestão de Motos (Oracle):** Tabela com todas as motos, com filtros (JPA Specification) e paginação, lendo do Oracle. Permite cadastrar, editar, excluir e dar entrada em motos.
- ✅ **Arquitetura Híbrida (Persistência Poliglota):**
    - **Oracle:** Utilizado para dados mestres, relacionais e que exigem alta integridade (Pátios, Leitores, Motos, Usuários).
    - **MongoDB:** Utilizado para dados transacionais de alto volume (Logs de Movimentação), otimizando a escrita e a leitura de filtros complexos.
- ✅ **Integração com Procedures Oracle:** O sistema chama procedures (`PACKAGE`) customizadas do Oracle via Spring Data JPA (`@Procedure`) para executar lógicas de negócio avançadas diretamente no banco (ex: atualizar o serviço de uma moto, contar motos).

---

## 🛠️ Arquitetura e Tecnologias

* **Backend:** Java 17, Spring Boot 3, Spring Security (OAuth2), Spring Data JPA, Spring Data MongoDB.
* **Banco de Dados:** Oracle (Servidor FIAP) e MongoDB (Local para dev, Atlas para prod).
* **Frontend:** Thymeleaf, DaisyUI, Tailwind CSS, Chart.js.
* **Build:** Gradle (com Wrapper).
* **Deploy:** Docker (via Dockerfile multi-estágio) na plataforma Render.

---

## 👨‍🏫 Guia de Execução (Ambiente de Desenvolvimento Local)

Este guia destina-se ao professor ou avaliador que precisa rodar o projeto localmente.

### Requisitos de Software

1.  **JDK 17 (Java Development Kit):** [Adoptium Temurin 17 (LTS)](https://adoptium.net/temurin/releases/?version=17)
2.  **Git:** [git-scm.com](https://git-scm.com/downloads)
3.  **IDE:** IntelliJ IDEA (recomendado) ou VS Code.
4.  **MongoDB Server (Local):** Necessário para que a aplicação e o Seeder funcionem.
    * **Opção A (Recomendada - Docker):** Se você tem Docker Desktop, rode no terminal:
        ```bash
        docker run -d --name mottu-mongo -p 27017:27017 mongo
        ```
    * **Opção B (Instalação Manual):** Baixe e instale o [MongoDB Community Server](https://www.mongodb.com/try/download/community).
5.  **MongoDB Compass (Opcional):** Para visualizar o banco Mongo local.
6.  **Oracle SQL Developer (Opcional):** Para visualizar o banco Oracle da FIAP.

### Passo a Passo para Execução

**1. Clone o Repositório:**
```bash
git clone [https://github.com/felipe-2833/New-Mottu-Gestor.git](https://github.com/felipe-2833/New-Mottu-Gestor.git)
```
**2.Setar variaveis de ambiente :**

GitHub: ID e secret -> mandado junto aos links

**3. Link :**
```bash
http://localhost:8080/login
```
