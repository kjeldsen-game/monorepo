# Stage 1: Build
FROM node:20-bullseye-slim AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build
# ENV NODE_ENV=production


ENV NEXTAUTH_URL "https://kjeldsengame.com"
ENV NEXT_PUBLIC_AUTH_URL "https://backend.kjeldsengame.com/v1"
ENV NEXT_PUBLIC_BACKEND_URL "https://backend.kjeldsengame.com/v1"
ENV NEXTAUTH_URL_INTERNAL "https://kjeldsengame.com"

# Stage 2: Development
FROM node:20-bullseye-slim
WORKDIR /app
COPY --from=builder /app ./
EXPOSE 3000
CMD ["npm", "run", "start"]