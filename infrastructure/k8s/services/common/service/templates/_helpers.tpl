{{- define "default.labels" }}
app: {{ .Release.Name }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
{{- end }}

{{- define "default.annotations" }}
deploymentTime: {{ now | date "02.01.2006 15:04:05" }}
{{- end }}

{{- define "required.container.parameters" }}
terminationMessagePolicy: FallbackToLogsOnError
securityContext:
  runAsUser: 100
  runAsGroup: 101
  privileged: false
  allowPrivilegeEscalation: false
  readOnlyRootFilesystem: true
  capabilities:
    drop:
      - ALL
{{- end }}
