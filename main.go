package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"os"
	"strings"

	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
)

func main() {
	godotenv.Load()
	r := gin.Default()
	r.Use(CORSMiddleware())
	r.POST("/weather", WeatherApp)

	r.Run(":8080")
}

func CORSMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		c.Writer.Header().Set("Access-Control-Allow-Origin", "*")
		c.Writer.Header().Set("Access-Control-Allow-Credentials", "true")
		c.Writer.Header().Set("Access-Control-Allow-Headers", "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization, accept, origin, Cache-Control, X-Requested-With")
		c.Writer.Header().Set("Access-Control-Allow-Methods", "POST, OPTIONS, GET, PUT")

		if c.Request.Method == "OPTIONS" {
			c.AbortWithStatus(204)
			return
		}

		c.Next()
	}
}

func WeatherApp(c *gin.Context) {
	apiKey := os.Getenv("API_KEY")
	baseURL := "http://api.openweathermap.org/data/2.5/weather?q="

	// Recebendo a cidade do JSON enviado pelo cliente
	var requestData struct {
		Location string `json:"location"`
	}
	if err := c.ShouldBindJSON(&requestData); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid request payload"})
		return
	}
	city := strings.TrimSpace(requestData.Location)

	// Codificando a cidade para URL
	encodedCity := url.QueryEscape(city)
	url := fmt.Sprintf("%s%s&appid=%s", baseURL, encodedCity, apiKey)

	response, err := http.Get(url)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Error sending the request."})
		return
	}
	defer response.Body.Close()

	body, err := ioutil.ReadAll(response.Body)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Error reading the response."})
		return
	}

	var data map[string]interface{}
	err = json.Unmarshal(body, &data)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Error decoding JSON."})
		return
	}

	// Verificando o código de resposta da API
	if cod, ok := data["cod"].(float64); ok && cod != 200 {
		c.JSON(http.StatusNotFound, gin.H{"error": "Oops, location not found."})
		return
	}

	mainData, ok := data["main"].(map[string]interface{})
	if !ok {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid location."})
		return
	}

	temperatureValue, ok := mainData["temp"].(float64)
	if !ok {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Error getting the temperature value."})
		return
	}

	CelsiusTemp := fmt.Sprintf("%d°C", int(temperatureValue-273.15))

	var description, icon string
	weatherDescription, ok := data["weather"].([]interface{})
	if ok && len(weatherDescription) > 0 {
		descriptionMap, ok := weatherDescription[0].(map[string]interface{})
		if ok {
			description = descriptionMap["description"].(string)
			icon = descriptionMap["icon"].(string)
		}
	}

	// Respondendo com JSON
	c.JSON(http.StatusOK, gin.H{
		"weatherdata": gin.H{"temperature": CelsiusTemp, "city": city, "description": description, "icon": icon},
	})
}
