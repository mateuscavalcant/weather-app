package controller

import (
	"html/template"
	"net/http"
	"weather-app/pkg/model"

	"github.com/gin-gonic/gin"
)

var tmpl *template.Template

func WeatherAppView(c *gin.Context) {
    htmlTemplate := `
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link
        href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800;900&family=Roboto:wght@300;400;500;700;900&display=swap"
        rel="stylesheet">
    <link rel="stylesheet" href="public/css/style.css">
    <title>Weather App</title>
</head>

<body>

<section class="container forms">
<div class="form weather-link">
    <div class="form-content">
        <header></header>
        <form id="weather-form" method="post" action="/weather">
        <div class="search-box">
        <input type="text" name="location" placeholder="Enter your location" value="{{.City}}">
        <button class="search-button">
        <img src="public/images/search.png" alt="search" >
        </button>
        </div>
            <div class="weather-box">
            {{if .Icon}}
            <img src="http://openweathermap.org/img/wn/{{.Icon}}@2x.png" alt="Weather Icon">
            {{end}}
            {{if .Message}}
            <img src="public/images/error.jpeg" alt="invalid location">
            <p class="error">{{.Error}}</p>
            <p class="message">{{.Message}}</p>
            {{end}}
            <p class="temperature">{{.Temperature}}</p>
            <p class="description">{{.Description}}</p>
            </div>
        </form>
    </div>
</div>
</section>

</body>

</html>

`

	tmpl = template.Must(template.New("WeatherApp").Parse(htmlTemplate)) 

	data := model.Data {
        Icon: "",
		Temperature: "",
		City: "",
		Description: "",
        Message: "",
        Error: "",
	}
    	
	RenderTemplate(c, data)
}

func RenderTemplate(c *gin.Context, data model.Data) {
	// Renderizar o template
	err := tmpl.Execute(c.Writer, data)
	if err != nil {
		c.String(http.StatusInternalServerError, "Erro interno do servidor")
		return
	}
}
