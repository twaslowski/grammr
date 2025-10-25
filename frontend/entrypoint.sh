#!/bin/sh

# Entrypoint script for runtime environment validation
echo "🚀 Starting Next.js application..."

# Run environment validation
node validate-env.js

# If validation passes, start the Next.js server
echo "🌐 Starting server on port ${PORT:-3000}..."
exec "$@"
