FROM  node:11.15.0-alpine
MAINTAINER weibo
ENV HOST 0.0.0.0
COPY server /app/server
COPY dist /app/dist
COPY package.json /app
WORKDIR /app
RUN npm config set registry http://192.168.1.1:4873
RUN npm install koa
RUN npm install cross-env
RUN npm install http-proxy-middleware
RUN npm install koa-static
CMD ["node", "server/index.js"]
