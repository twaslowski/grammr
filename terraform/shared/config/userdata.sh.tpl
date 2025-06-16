#!/bin/bash
set -euxo pipefail

curl -fsSL https://tailscale.com/install.sh | sh

tailscale up --auth-key=${tailscale_auth_key} --hostname=k3s-master --advertise-tags=tag:k3s

curl -sfL https://get.k3s.io | sh -s - --write-kubeconfig-mode 644
