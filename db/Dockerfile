FROM mysql
WORKDIR /tmp

COPY db_source/*.sql /tmp/
COPY db_source/world.sql /docker-entrypoint-initdb.d
ENV MYSQL_ROOT_PASSWORD example