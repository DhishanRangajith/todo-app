#!/bin/sh

# Defaults if not provided
API_URL="${API_URL}"

# Replace placeholders with actual values
sed "s|__API_URL__|$API_URL|g" \
  /usr/share/nginx/html/assets/env.template.js \
  > /usr/share/nginx/html/assets/env.js

# Start Nginx
exec nginx -g "daemon off;"
