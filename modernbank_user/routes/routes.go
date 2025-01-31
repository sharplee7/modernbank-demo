package routes

import (
	"modernbank_user/db"
	"modernbank_user/handlers"
	"modernbank_user/middleware"

	"github.com/gin-gonic/gin"
)

func SetupRoutes(r *gin.Engine) {


	//CORS 추가
	r.Use(middleware.CORSMiddleware())

    // 기본 경로를 /modernbank/users로 설정
    baseRouter := r.Group("/modernbank/users")
    {
        // Public routes
        baseRouter.POST("/login", handlers.LoginHandler(db.DB))
        baseRouter.POST("/user", handlers.CreateUser(db.DB)) 

        // Protected routes
        protected := baseRouter.Group("/api")
        protected.Use(middleware.JWTAuthMiddleware())
        protected.PATCH("/:user_id/password", handlers.ChangePassword(db.DB))
    }


/*  
	// 기존 소스 코드
	// Public routes
	r.POST("/login", handlers.LoginHandler(db.DB))
	r.POST("/users", handlers.CreateUser(db.DB))

	// Protected routes
	protected := r.Group("/api")
	protected.Use(middleware.JWTAuthMiddleware())
	//protected.POST("/change-password", handlers.ChangePassword(db.DB))
	protected.PATCH("/users/:user_id/password", handlers.ChangePassword(db.DB)) // 비밀번호 변경 엔드포인트
*/

}
