FROM nginx:1.17.1-alpine

RUN rm /etc/nginx/nginx.conf /etc/nginx/conf.d/default.conf

COPY frontend/content/ /usr/share/nginx/html
COPY frontend/nginx.conf /etc/nginx/

EXPOSE 80
