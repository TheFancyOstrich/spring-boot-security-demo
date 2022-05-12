Simple start for a secured api. Following https://www.youtube.com/watch?v=her_7pa0vrg&t=2317s. (Should probably check out his repo instead.)

Mariadb docker container:
docker run -d --rm --name mariadb --network host --env MARIADB_DATABASE=security_demo --env MARIADB_USER=dev --env MARIADB_PASSWORD=secret --env MARIADB_ROOT_PASSWORD=secret mariadb:latest
