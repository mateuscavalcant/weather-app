package main

import (
	"weather-app/weather-app-server/controller"
	"weather-app/weather-app-server/middleware"

	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
)

func main() {
	godotenv.Load()
	r := gin.Default()
	r.Use(middleware.CORSMiddleware())
	r.POST("/weather", controller.WeatherApp)

	r.Run(":8080")
}
