#!/usr/bin/env bash
# Applies the toeggelomat manifests to the spoud cluster, substituting
# ${HOSTNAME} / ${OFFICE_IP} placeholders from .env into the files that
# need them. See README.md for the full step-by-step, including why
# 01-secret.yaml is deliberately skipped here.
set -euo pipefail
cd "$(dirname "$0")"

if [ ! -f .env ]; then
  echo ".env not found - copy .env.example to .env and fill in real values first." >&2
  exit 1
fi
# shellcheck disable=SC1091
source .env

kubectl --context spoud apply -f 00-namespace.yaml
kubectl --context spoud apply -f 02-postgres.yaml
kubectl --context spoud apply -f 03-backend.yaml
kubectl --context spoud apply -f 04-frontend.yaml

for f in 05-virtualservice.yaml 06-authorizationpolicy.yaml; do
  sed -e "s|\${HOSTNAME}|${HOSTNAME}|g" -e "s|\${OFFICE_IP}|${OFFICE_IP}|g" "$f" \
    | kubectl --context spoud apply -f -
done

echo
echo "Skipped 01-secret.yaml - create the real Postgres password imperatively instead:"
echo "  kubectl --context spoud -n toeggelomat create secret generic toeggelomat-secret \\"
echo "    --from-literal=POSTGRES_PASSWORD=\"\$(openssl rand -base64 24)\""
