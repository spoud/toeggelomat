# Deploying to the spoud cluster

Plain Kubernetes manifests for the `toeggelomat` namespace, using the shared
Istio gateway (`default/oauth-gateway`) already used by other apps like
`kafka-cost-control`, which terminates TLS for the wildcard `*.sdm.spoud.io`
— so no dedicated Gateway/Certificate is needed here. Access is restricted
to a single office IP via an `AuthorizationPolicy`.

No real hostname or IP is committed to the repo — see **Configuration** below.

## Prerequisites

- Images `spoud/toeggelomat-backend:latest` and `spoud/toeggelomat-frontend:latest`
  already get built and pushed to Docker Hub by `.github/workflows/build-backend.yaml`
  / `build-frontend.yaml` on every push to `main`.
- Flyway migrations run automatically on backend startup (`quarkus.flyway.migrate-at-start: true`
  in `application.yaml`), so there's no separate migration job to run.

## Configuration

`05-virtualservice.yaml` and `06-authorizationpolicy.yaml` use `${HOSTNAME}`
and `${OFFICE_IP}` placeholders instead of real values. Copy `.env.example`
to `.env` (gitignored) and fill in the real ones:

```bash
cp k8s/.env.example k8s/.env
# edit k8s/.env - HOSTNAME (e.g. toeggelomat.sdm.spoud.io) and OFFICE_IP
# (check yours with: curl ifconfig.me)
```

## Apply

```bash
k8s/apply.sh
```

This applies everything except `01-secret.yaml` (a placeholder only — see
below) and substitutes the two placeholders above from `.env` before
applying `05-virtualservice.yaml` / `06-authorizationpolicy.yaml`.

First time only, create the namespace and the real Postgres password secret
before running `apply.sh` (it doesn't create either of these for you):

```bash
kubectl --context spoud apply -f k8s/00-namespace.yaml
kubectl --context spoud -n toeggelomat create secret generic toeggelomat-secret \
  --from-literal=POSTGRES_PASSWORD="$(openssl rand -base64 24)"
```

`01-secret.yaml` only contains a placeholder password and should never be
edited in place with a real one (that would mean committing a secret to git)
— the imperative command above is the actual way to create it.

## Notes

- **Access is restricted to `OFFICE_IP`** (`06-authorizationpolicy.yaml`).
  It uses Istio's `remoteIpBlocks` (original client IP via the trusted XFF
  chain) rather than `ipBlocks` (which would only ever see the shared
  ingress gateway's own IP, since that gateway sits in front of every app in
  the cluster) — the mesh already trusts proxies 2 hops deep and the
  gateway's load balancer has PROXY protocol enabled, so this should reflect
  the real client IP, but it's worth confirming from outside the office once
  applied (e.g. over a VPN or phone hotspot) rather than just trusting it blind.
- Postgres is a self-contained `StatefulSet` with its own `PersistentVolumeClaim`
  in-namespace, not a shared/managed instance. `PGDATA` is pointed at a
  subdirectory of the mount (not the mount root) because fresh EBS-backed
  PVCs often come with a pre-existing `lost+found` directory, and `initdb`
  refuses to init into a non-empty directory.
- Resource requests/limits are rough starting points (this is a small app) —
  adjust after watching real usage.
- If/when this moves to its own subdomain (e.g. `toeggelomat.spoud.io`
  instead of piggy-backing on `*.sdm.spoud.io`), that needs a dedicated
  Istio `Gateway` and a cert-manager `Certificate` (the shared gateway's
  wildcard cert only covers `*.sdm.spoud.io`) — deliberately left out for
  now to keep this simple.
