FROM node:14.15.1
ENV NODE_ENV=dev
WORKDIR /app
COPY ["package.json", "package-lock.json*", "./"]
RUN npm install --dev
COPY . .
EXPOSE 3000
CMD [ "npm", "run", "dev"]
