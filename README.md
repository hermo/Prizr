# Prizr
Prizr is small-footprint pricing module for eCommerce applications based on the Drools engine

## Build instructions

`mvn clean package assembly:single -Denforcer.skip=true`

## Running it locally

`java -jar target/Prizr-webapp-jar-with-dependencies.jar [dbhost] [dbname] [port]`

## Running it in a container

The app can be run inside a Docker container.

`docker-compose up`


## Running in production

### Configuring supervisord

First install supervisord.

http://supervisord.org/installing.html

On hosting environments where you can't use root to install supervisor,
the proper command to install is:

`python setup.py install --user`
