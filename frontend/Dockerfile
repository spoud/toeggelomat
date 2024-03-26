FROM nginx

# Configure for angular fallback routes
COPY nginx.conf /etc/nginx/nginx.conf

# Copy built app to wwwroot
COPY /dist/toeggelomat/browser /usr/share/nginx/html

HEALTHCHECK --interval=5s CMD curl http://localhost:80
