# Prizr
Prizr is small-footprint pricing module for eCommerce applications based on the Drools engine

Build with:

mvn clean package assembly:single -Denforcer.skip=true

Run with:

java -jar target/Prizr-webapp-jar-with-dependencies.jar [dbname] [port]

Configuring supervisord:

First install supervisord. 

http://supervisord.org/installing.html

On hosting environments where you can't use root to install supervisor,
the proper command to install is:

python setup.py install --user
