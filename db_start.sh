docker run -d \
  --name mysql-spring \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=spring \
  -p 3306:3306 \
  mysql:8.0.28