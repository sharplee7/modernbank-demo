package main

import (
	"log"

	"innos.com/core-banking/config"
	"innos.com/core-banking/db"
	_ "innos.com/core-banking/docs"
	"innos.com/core-banking/routes"

	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
)

// @title Core Banking API
// @version 1.0
// @description This is the API documentation for the Core Banking system.
// @termsOfService http://swagger.io/terms/

// @contact.name API Support
// @contact.url http://www.example.com/support
// @contact.email support@example.com

// @host localhost:8091
// @BasePath /
func main() {
	// Load configuration
	cfg, err := config.LoadConfig()
	if err != nil {
		log.Fatalf("Failed to load configuration: %v", err)
	}

	// Initialize database
	db.InitDB(cfg.DatabaseURL)

	// Setup Gin router
	r := gin.Default()

	// Swagger UI 경로 추가
	r.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	routes.SetupRoutes(r)

	// Start server
	log.Printf("Server is running on %s", cfg.ServerPort)
	if err := r.Run(cfg.ServerPort); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}
