## Simple setup

This example will install a single language (Russian) server with the following capabilities:
- Perform morphological analysis on Russian text
- Generate inflections for Russian words
- Export Flashcards in Anki APKG format

You can run this example on a local Kubernetes cluster 
(e.g. [kind](https://kind.sigs.k8s.io/) or [minikube](https://minikube.sigs.k8s.io/docs/)) or on any cloud provider.

### Prerequisites

- [Helm 3](https://helm.sh/docs/intro/install/) installed
- A running Kubernetes cluster (v1.19+)
- [kubectl](https://kubernetes.io/docs/tasks/tools/) installed and configured to access
- An account at [Clerk](https://clerk.dev/) to configure authentication for the core service
- An [OpenAI](https://platform.openai.com/) API key to enable LLM features

### Installing

To install the example, first add the required Helm repositories and update your local Helm chart repository cache:

```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo add grammr https://twaslowski.github.io/grammr-charts
helm repo update
```

Set the following values:
- Your Clerk JWT Public Key in the `manifest/clerk-public-key.yaml` file
- The openai_api_key in the `values/core.values.yaml` file

Install Postgres, and then the language microservices and core:

```shell
# Clone this project and navigate to the example directory
git clone git@github.com:twaslowski/grammr.git
cd grammr/examples/single-language

# Create ConfigMap for Postgres init scripts
kubectl apply -k manifest

helm install --namespace grammr \
  --set primary.initdb.scriptsConfigMap=postgres-initdb \
  postgres oci://registry-1.docker.io/bitnamicharts/postgresql
  
helm install --namespace grammr \
  --values values/core.values.yaml grammr-core grammr/grammr-core

# Microservices
helm install --namespace grammr \
  inflections-ru grammr/inflections-ru

helm install --namespace grammr --values values/morphology.values.yaml \
  morphology-ru grammr/morphology
  
helm install --namespace grammr \
  anki-exporter grammr/anki-exporter
```