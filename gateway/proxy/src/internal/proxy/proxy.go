package proxy

import (
	"log"
	"net/http"
	"net/http/httputil"
	"net/url"

	"proxy/internal/auth"
)

func NewProxy(target string, verifier *auth.JWTVerifier) http.Handler {
	targetURL, err := url.Parse(target)
	if err != nil {
		log.Fatalf("Invalid target host: %v", err)
	}

	proxy := httputil.NewSingleHostReverseProxy(targetURL)

	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		tokenStr := auth.ExtractToken(r)
		if tokenStr != "" {
			_, err := verifier.Verify(tokenStr)
			if err != nil {
				http.Error(w, "Invalid token", http.StatusForbidden)
				return
			}
		}

		proxy.ServeHTTP(w, r)
	})
}
