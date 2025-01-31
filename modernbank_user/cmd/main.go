package main

import (
	"log"

	"modernbank_user/config"
	"modernbank_user/db"
	"modernbank_user/routes"

	"github.com/gin-gonic/gin"
)

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
	//r.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	routes.SetupRoutes(r)

	// Start server
	log.Printf("Server is running on %s", cfg.ServerPort)
	if err := r.Run(cfg.ServerPort); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}
