package controller

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"strings"
	"weather-app/pkg/model"

	"github.com/gin-gonic/gin"
)

func WeatherApp(c *gin.Context) {
	apiKey := os.Getenv("API_KEY")
	baseURL := "http://api.openweathermap.org/data/2.5/weather?q="
	city := strings.TrimSpace(c.PostForm("location"))

	url := fmt.Sprintf("%s%s&appid=%s", baseURL, city, apiKey)

	response, err := http.Get(url)
	if err != nil {
		RenderTemplate(c, model.Data{Message: "Error sending the request.", Error: "HTTP 500"})
		return
	}
	defer response.Body.Close()

	body, err := ioutil.ReadAll(response.Body)
	if err != nil {
		RenderTemplate(c, model.Data{Message: "Error reading the response.", Error: "HTTP 500" })
		return
	}

	var data map[string]interface{}
	err = json.Unmarshal(body, &data)
	if err != nil {
		RenderTemplate(c, model.Data{Message: "Error decoding JSON.", Error: "HTTP 500" })
		return
	}

	mainData, ok := data["main"].(map[string]interface{})
	if !ok {
		RenderTemplate(c, model.Data{Message: "Oops! Invalid location :/", Error: "HTTP 400"})
		return
	}

	temperatureValue, ok := mainData["temp"].(float64)
	if !ok {
		RenderTemplate(c, model.Data{Message:"Error getting the temperature value.", Error: "HTTP 500"})
		return
	}

	if data["cod"] != nil {
		cod := data["cod"].(float64)
		if cod != 200 {
			RenderTemplate(c, model.Data{Message:"Oops, location not found.", Error: "HTTP 404"})
			return
		}
	}

	CelsiusTemp := fmt.Sprintf("%d°C", int(temperatureValue-273.15))

	var description string
	var icon string
	weatherDescription, ok := data["weather"].([]interface{})
	if ok && len(weatherDescription) > 0 {
		descriptionMap, ok := weatherDescription[0].(map[string]interface{})
		if ok {
			description = descriptionMap["description"].(string)
			// Obtendo o ícone
			icon, ok = descriptionMap["icon"].(string)
			if !ok {
				fmt.Println("Erro ao obter o ícone.")
				return
			}
		}
	}

	RenderTemplate(c, model.Data{
		Temperature: CelsiusTemp,
		City:        city,
		Description: description,
		Icon:        icon,
	})
}
