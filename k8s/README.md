# Deploying to the spoud cluster

Plain Kubernetes manifests for the `toeggelomat` namespace, modeled on the
existing `kafka-cost-control-*` apps in this cluster (same `k8s-app` label
convention, same shared Istio gateway for ingress).

## Prerequisites

- Images `spoud/toeggelomat-backend:latest` and `spoud/toeggelomat-frontend:latest`
  already get built and pushed to Docker Hub by `.github/workflows/build-backend.yaml`
  / `build-frontend.yaml` on every push to `main`.
- Flyway migrations run automatically on backend startup (`quarkus.flyway.migrate-at-start: true`
  in `application.yaml`), so there's no separate migration job to run.

## Apply order

1. Create the namespace:
   ```bash
   kubectl --context spoud apply -f k8s/00-namespace.yaml
   ```
2. Create the Postgres password secret **imperatively** rather than applying
   `01-secret.yaml` as-is — it only contains a placeholder value and shouldn't
   be edited in place with a real password (that would mean committing a
   secret to git):
   ```bash
   kubectl --context spoud -n toeggelomat create secret generic toeggelomat-secret \
     --from-literal=POSTGRES_PASSWORD="$(openssl rand -base64 24)"
   ```
3. Apply everything else:
   ```bash
   kubectl --context spoud apply -f k8s/02-postgres.yaml
   kubectl --context spoud apply -f k8s/03-backend.yaml
   kubectl --context spoud apply -f k8s/04-frontend.yaml
   kubectl --context spoud apply -f k8s/05-virtualservice.yaml
   ```

## Notes

- The `VirtualService` routes through the cluster's existing shared gateway
  (`default/oauth-gateway`), which already terminates TLS for `*.sdm.spoud.io`.
  Double-check that `toeggelomat.sdm.spoud.io` (or whatever hostname you pick)
  actually resolves before relying on it — pick a different host in
  `05-virtualservice.yaml` if that one's taken or DNS isn't set up for it.
- Postgres is a self-contained `StatefulSet` with its own `PersistentVolumeClaim`
  in-namespace, not a shared/managed instance.
- Resource requests/limits are rough starting points (this is a small app) —
  adjust after watching real usage.
