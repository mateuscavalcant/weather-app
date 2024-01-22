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

var CelsiusTemp string
var description string
var icon string

func WeatherApp(c *gin.Context) {
	apiKey := os.Getenv("API_KEY")
	baseURL := "http://api.openweathermap.org/data/2.5/weather?q="
	city := strings.TrimSpace(c.PostForm("location"))

	url := fmt.Sprintf("%s%s&appid=%s", baseURL, city, apiKey)

	response, err := http.Get(url)
	if err != nil {
		fmt.Println("Erro ao enviar a solicitação:", err)
		return
	}
	defer response.Body.Close()

	body, err := ioutil.ReadAll(response.Body)
	if err != nil {
		fmt.Println("Erro ao ler a resposta:", err)
		return
	}

	var data map[string]interface{}
	err = json.Unmarshal(body, &data)
	if err != nil {
		fmt.Println("Erro ao decodificar JSON:", err)
		return
	}

	mainData, ok := data["main"].(map[string]interface{})
	if !ok {
		RenderTemplate(c, model.Data{Message: "Oops! Invalid location :/"})
		return
	}

	temperatureValue, ok := mainData["temp"].(float64)
	if !ok {
		fmt.Println("Erro ao obter o valor da temperatura.")
		return
	}

	if data["cod"] != nil {
		cod := data["cod"].(float64)
		if cod != 200 {
			fmt.Println("Ops, localização não encontrada. Verifique se o nome da cidade é válido.")
			return
		}
	}

	CelsiusTemp = fmt.Sprintf("%d°C", int(temperatureValue-273.15))

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
