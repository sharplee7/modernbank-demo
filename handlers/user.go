package handlers

import (
	"database/sql"
	"net/http"

	"innos.com/core-banking/models"

	"github.com/gin-gonic/gin"
)

type CreateUserRequest struct {
	UserID     string `json:"user_id"`
	CstmID     string `json:"cstm_id"`
	CstmName   string `json:"cstm_name"`
	CstmAge    string `json:"cstm_age"`
	CstmGender string `json:"cstm_gender"`
	CstmPhone  string `json:"cstm_phone"`
	CstmAddr   string `json:"cstm_addr"`
	Username   string `json:"username"`
	Password   string `json:"password"`
}

type ChangePasswordRequest struct {
	//UserID      string `json:"user_id"`      // 사용자 ID
	OldPassword string `json:"old_password"` // 기존 비밀번호
	NewPassword string `json:"new_password"` // 새 비밀번호
}

// CreateUser creates a new user in the database.
// @Summary Create a new user
// @Description Create a new user with customer information and login credentials
// @Tags Users
// @Accept json
// @Produce json
// @Param user body CreateUserRequest true "User information"
// @Success 201 {object} gin.H{"message": "User created successfully"}
// @Failure 400 {object} gin.H{"error": "Invalid request payload"}
// @Failure 500 {object} gin.H{"error": "Failed to create user"}
// @Router /users [post]
func CreateUser(db *sql.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req CreateUserRequest
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid request payload"})
			return
		}

		tx, err := db.Begin()
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to start transaction"})
			return
		}
		defer tx.Rollback()

		_, err = tx.Exec("INSERT INTO tb_cstm (cstm_id, cstm_nm, cstm_age, cstm_gnd, cstm_pn, cstm_adr) VALUES ($1, $2, $3, $4, $5, $6)",
			req.CstmID, req.CstmName, req.CstmAge, req.CstmGender, req.CstmPhone, req.CstmAddr)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to insert customer"})
			return
		}

		salt, _ := models.GenerateSalt()
		hashedPassword, _ := models.HashPassword(req.Password, salt)

		_, err = tx.Exec("INSERT INTO tb_user (user_id, cstm_id, username, password_hash, salt) VALUES ($1, $2, $3, $4, $5)",
			req.UserID, req.CstmID, req.Username, hashedPassword, salt)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to insert user"})
			return
		}

		if err := tx.Commit(); err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to commit transaction"})
			return
		}

		c.JSON(http.StatusCreated, gin.H{"message": "User created successfully"})
	}
}

// ChangePassword allows users to update their password
func ChangePassword(db *sql.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		userID := c.Param("user_id") // URL에서 user_id 추출
		var req ChangePasswordRequest
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid request payload"})
			return
		}

		// Validate the old password
		valid, err := models.ValidateUser(db, userID, req.OldPassword)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Internal server error"})
			return
		}
		if !valid {
			c.JSON(http.StatusUnauthorized, gin.H{"error": "Invalid user_id or password"})
			return
		}

		// Update the password
		err = models.ChangePassword(db, userID, req.NewPassword)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to update password"})
			return
		}

		c.JSON(http.StatusOK, gin.H{"message": "Password updated successfully"})
	}
}
