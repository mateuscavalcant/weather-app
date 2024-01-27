# Weather App

Projeto de obtenção da temperatura e da descrição do tempo com base na localidade inserida pelo usuário.

## Tecnologias

- Go
- CSS

## Acesse o App

[Weather App](https://weather-app-qt59.onrender.com/weather)

## Descrição

O Weather App é um aplicativo que permite aos usuários obterem informações sobre a temperatura e a descrição do tempo com base na localidade inserida. O projeto utiliza as seguintes tecnologias:

- **Front-end Integrado**: O front-end está integrado junto ao servidor para garantir segurança contra ataques de injeção HTML.

- **Framework Gin**: O projeto utiliza o framework Gin para criar rotas próprias e gerenciar a lógica da aplicação.

- **OpenWeatherMap API**: Para obtenção das condições climáticas, o aplicativo consome a API do OpenWeatherMap.

## Configuração
Obtenha uma chave de API do OpenWeatherMap em https://openweathermap.org/
substitua o valor da variável "apiKey" no código pelo valor da sua chave de API.

## Como Executar o Projeto

Certifique-se de ter o Go instalado em sua máquina. Clone o repositório e execute os seguintes comandos:

```bash
# Crie um modulo
go mod init

# Instale as dependências
go mod tidy

# Execute o aplicativo
go run main.go
```

## Aviso Legal
Este projeto é apenas para fins educacionais e não se destina a fins comerciais.
