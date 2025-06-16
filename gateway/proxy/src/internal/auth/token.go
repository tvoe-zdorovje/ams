package auth

import (
	"net/http"
	"strings"
)

func ExtractToken(r *http.Request) string {
	authHeader := r.Header.Get("Authorization")
	if strings.HasPrefix(authHeader, "Bearer ") {
		return strings.TrimPrefix(authHeader, "Bearer ")
	}

	if cookie, err := r.Cookie("jwt"); err == nil {
		return cookie.Value
	}

	return ""
}
