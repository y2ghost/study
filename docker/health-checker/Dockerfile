FROM node:24-alpine

ENV MAX_ALLOCATION_MB=4096 \
    LOOP_ALLOCATION_MB=512 \
    LOOP_INTERVAL_MS=2000

COPY --chmod=755 <<EOT /entrypoint.sh
#!/bin/sh
node memory-check.js && node memory-hog.js
EOT
ENTRYPOINT ["/entrypoint.sh"]

HEALTHCHECK --interval=5s \
 CMD ["node", "memory-check.js"]

WORKDIR /app
COPY src/ .

