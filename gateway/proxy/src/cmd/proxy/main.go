package main

import (
	"log"
	"net/http"

	"proxy/config"
	"proxy/internal/auth"
	"proxy/internal/proxy"
)

func main() {
	_config := config.Load()

	verifier, err := auth.NewJWTVerifier(_config.JwksUrl)
	if err != nil {
		log.Fatalf("Failed to create JWT verifier: %v", err)
	}

	tokenVerifierProxy := proxy.NewProxy(_config.TargetHost, verifier)

	log.Printf("Starting proxy at %s, forwarding to %s", _config.ListenPort, _config.TargetHost)
	http.ListenAndServe(_config.ListenPort, tokenVerifierProxy)
}
