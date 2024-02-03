package controller

import (
	"html/template"
	"net/http"
	"weather-app/pkg/model"

	"github.com/gin-gonic/gin"
)

var tmpl *template.Template

func WeatherAppView(c *gin.Context) {

	tmpl = template.Must(template.ParseFiles("templates/weatherapp.html")) 

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
