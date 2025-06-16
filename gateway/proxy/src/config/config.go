package config

import (
	"os"
	"strconv"
)

type Config struct {
	JwksUrl             string
	JwksRefreshInterval int
	ListenPort          string
	TargetHost          string
}

func Load() Config {
	return Config{
		JwksUrl:             getEnv("JWKS_URL", "http://auth-service:8080/.well-known/jwks"),
		JwksRefreshInterval: getEnvAsInt("JWKS_REFRESH_INTERVAL", 30),
		ListenPort:          getEnv("LISTEN_ADDR", ":8080"),
		TargetHost:          getEnv("BACKEND_TARGET", "http://localhost:4000"),
	}
}

func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}

func getEnvAsInt(key string, fallback int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return fallback
}
