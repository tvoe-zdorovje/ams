package config

import (
	"os"
)

type Config struct {
	JwksUrl    string
	ListenPort string
	TargetHost string
}

func Load() Config {
	return Config{
		// TODO move to env vars
		JwksUrl:    getEnv("JWKS_URL", "http://auth-service:8080/.well-known/jwks"),
		ListenPort: getEnv("LISTEN_ADDR", ":8080"),
		TargetHost: getEnv("BACKEND_TARGET", "http://localhost:4000"),
	}
}

func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}
