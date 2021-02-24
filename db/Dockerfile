FROM mysql
## Set working directory to /tmp.
WORKDIR /tmp

## Copy the world.sql file and execute it on container startup.
COPY db_source/*.sql /tmp/
COPY db_source/world.sql /docker-entrypoint-initdb.d

## Set the database connection passwword to "example".
ENV MYSQL_ROOT_PASSWORD example