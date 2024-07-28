call mvn clean package
cd ..
move /Y ".\CentralisUHC\target\centralisuhc-1.0.0.jar" ".\server\plugins\"
cd CentralisUHC
docker compose down
docker compose up -d