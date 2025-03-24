# Sistema de Rastreamento de Veículos

Sistema de rastreamento de veículos que recebe dados via TCP de rastreadores GPS e disponibiliza uma API REST para consulta.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.2.3
- PostgreSQL
- Docker
- Maven

## Funcionalidades

- Recebimento de dados via TCP na porta 5001
- API REST para consulta de localizações
- Armazenamento em banco de dados PostgreSQL
- Suporte a múltiplos rastreadores simultâneos

## Requisitos

- Java 21
- Maven
- Docker e Docker Compose (opcional)
- PostgreSQL (opcional, se não usar Docker)

## Configuração do Ambiente

### Usando Docker (Recomendado)

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/auto-api.git
cd auto-api
```

2. Inicie os containers:
```bash
docker-compose up -d
```

3. Compile o projeto:
```bash
mvn clean package -DskipTests
```

4. Execute o JAR:
```bash
java -jar target/auto-api-0.0.1-SNAPSHOT.jar
```

### Sem Docker

1. Configure o PostgreSQL:
```bash
# Criar banco de dados
createdb rastreamento
```

2. Compile o projeto:
```bash
mvn clean package -DskipTests
```

3. Execute o JAR:
```bash
java -jar target/auto-api-0.0.1-SNAPSHOT.jar
```

## Endpoints da API

### Receber Dados do Rastreador
```
POST /api/rastreamento/dados
```

### Consultar Localizações
```
GET /api/rastreamento/localizacoes/{imei}
```

## Formato dos Dados

O sistema espera receber dados no formato GPRMC:
```
$GPRMC,123456.789,A,1234.5678,N,5678.1234,W,0.0,0.0,240324,0.0,E,A*00
```

## Testando a Conexão TCP

Use o script PowerShell fornecido:
```powershell
.\teste-tcp.ps1
```

## Deploy

O projeto está configurado para deploy em plataformas como Railway.app, Render.com ou Heroku.

## Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes. 