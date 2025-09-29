# New Mottu Gestor

O Mottu Gestor é uma solução inteligente de mapeamento e gestão de pátios para frotas de motos. Utilizando RFID, nosso sistema oferece rastreamento em tempo real, otimização operacional e segurança automatizada, reduzindo custos e aumentando a eficiência da Mottu.

## Destaques da solução
- ✅ Pemitir ver quantos patios a mottu tem cadastrado, fazer edição e cadastrar patios.

- ✅ Ver todos os leitores de rfid de cada patio, dividindo o patio em areas.

- ✅ DashBoard com informações gerais e resumidas de cada patio, ver todas as motos que tem em cada leitor e movimena-los.

- ✅ Log de movimentações com filtros.

- ✅ Tabela com todas as motos com filtro, permite cadastrar nova moto e dar entrada em motos que estão fora de patio.

## Funcionalidades
WebServer em Spring/Thymeleaf

Banco de Dados posgres

Autenticação com Oauth2

## Requisitos
Java JDK 17+

gandle

Git

DockerDesktop

IntelliJ IDEA

## Execução do projeto (IntelliJ)
1. Clone o repositório :
   
git clone https://github.com/felipe-2833/New-Mottu-Gestor.git

2. Abra o DockerDeskTop

3. Crie a conexão com o dataBase:

Canto superior direito -> botão de DataBase -> cline no + -> datasource -> selecione postgres -> de download coloque o user/password/dbname que se encontram no docker compose -> selecione init

4. De run para criar o banco
   
5. Criar variaveis de ambiente GITHUB_ID = Ov23ctQs89SrC8OxizIA e GITHUB_SECRET = 3d5e41563ca66bc53264368b486c63fdfbe1de01

6. Tela se login -> http://localhost:8080/login -> 
