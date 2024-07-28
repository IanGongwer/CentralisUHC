call mvn clean package
cd ..
docker compose down
move /Y ".\CentralisUHC\target\centralisuhc-1.0.0.jar" ".\server\plugins\"
cd CentralisUHC
docker compose up -d