package auth

import (
	"log"
	"time"

	"github.com/MicahParks/keyfunc"
	"github.com/golang-jwt/jwt/v4"
)

type JWTVerifier struct {
	keyFunc jwt.Keyfunc
}

func NewJWTVerifier(jwksURL string, jwksRefreshInterval int) (*JWTVerifier, error) {
	jwks, err := keyfunc.Get(jwksURL, keyfunc.Options{
		RefreshInterval: time.Duration(jwksRefreshInterval) * time.Minute,
		RefreshErrorHandler: func(err error) {
			log.Printf("Error refreshing JWKS: %v", err)
			panic(err)
		},
	})
	if err != nil {
		return nil, err
	}

	return &JWTVerifier{keyFunc: jwks.Keyfunc}, nil
}

func (v *JWTVerifier) Verify(tokenString string) (*jwt.Token, error) {
	return jwt.Parse(tokenString, v.keyFunc)
}
