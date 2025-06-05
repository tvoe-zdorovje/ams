package auth

import (
	"time"

	"github.com/MicahParks/keyfunc"
	"github.com/golang-jwt/jwt/v4"
)

type JWTVerifier struct {
	keyFunc jwt.Keyfunc
}

func NewJWTVerifier(jwksURL string) (*JWTVerifier, error) {
	jwks, err := keyfunc.Get(jwksURL, keyfunc.Options{
		RefreshInterval: time.Hour, // TODO move to env settings
		RefreshErrorHandler: func(err error) {
			// TODO
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
