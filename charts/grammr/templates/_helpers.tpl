{{/*
Expand the name of the chart.
*/}}
{{- define "grammr-core.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "grammr-core.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "grammr-core.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "grammr-core.labels" -}}
helm.sh/chart: {{ include "grammr-core.chart" . }}
{{ include "grammr-core.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "grammr-core.selectorLabels" -}}
app.kubernetes.io/name: {{ include "grammr-core.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{- define "utils.secret.keepOrCreate" -}}
{{- /*
    Template function to lookup a secret value or generate a new one if it doesn't exist.

    Parameters:
      - .namespace: The namespace of the secret to lookup
      - .name: The name of the secret to lookup
      - .key: The key within the secret to look up

    Usage:
    {{ include "utils.secret.lookup" (dict "namespace" "my-namespace" "name" "my-secret" "key" "secret-key") }}
*/}}
{{- $secretObj := (lookup "v1" "Secret" .namespace .name) | default dict }}
{{- $secretData := (get $secretObj "data") | default dict }}
{{- $value := (get $secretData .key) | default (randAlphaNum 64 | b64enc) }}
{{- $value | quote }}
{{- end }}

{{- define "utils.secret.keepOrCreateHex" -}}
{{- /*
    Template function to lookup a secret value or generate a new one if it doesn't exist.
    Specifically calls `randNumeric` over `randAlphaNum` to generate a hex-compatible value [0-9a-f].

    Parameters:
      - .namespace: The namespace of the secret to lookup
      - .name: The name of the secret to lookup
      - .key: The key within the secret to look up

    Usage:
    {{ include "utils.secret.lookup" (dict "namespace" "my-namespace" "name" "my-secret" "key" "secret-key") }}
*/}}
{{- $secretObj := (lookup "v1" "Secret" .namespace .name) | default dict }}
{{- $secretData := (get $secretObj "data") | default dict }}
{{- $value := (get $secretData .key) | default (randNumeric 64 | b64enc) }}
{{- $value | quote }}
{{- end }}