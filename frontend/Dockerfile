FROM nginx:1.16.0-alpine

# copy artifact build from the 'build environment'
COPY /dist/frontend /usr/share/nginx/html
COPY /nginx.conf /etc/nginx/nginx.conf

# expose port 80
EXPOSE 80

# run nginx
CMD ["nginx", "-g", "daemon off;"]



FROM nginx

# Configure for angular fallback routes
COPY nginx.conf /etc/nginx/nginx.conf

# Copy built app to wwwroot
COPY /dist/frontend/browser /usr/share/nginx/html

HEALTHCHECK --interval=5s CMD curl http://localhost:80
