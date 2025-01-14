package routes

import (
	"innos.com/core-banking/db"
	"innos.com/core-banking/handlers"
	"innos.com/core-banking/middleware"

	"github.com/gin-gonic/gin"
)

func SetupRoutes(r *gin.Engine) {
	// Public routes
	r.POST("/login", handlers.LoginHandler(db.DB))
	r.POST("/users", handlers.CreateUser(db.DB))

	// Protected routes
	protected := r.Group("/api")
	protected.Use(middleware.JWTAuthMiddleware())
	//protected.POST("/change-password", handlers.ChangePassword(db.DB))
	protected.PATCH("/users/:user_id/password", handlers.ChangePassword(db.DB)) // 비밀번호 변경 엔드포인트
}
