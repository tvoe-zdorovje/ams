{{- define "deployment.container.volume" }}
- name: {{ .name }}
  {{- if .claimName }}
  persistentVolumeClaim:
    claimName: {{ .claimName }}
  {{- else }}
  emptyDir: { }
  {{- end }}
{{- end }}
