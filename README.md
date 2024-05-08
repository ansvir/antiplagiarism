# Antiplagiarism v1.0
## Docker database
Requires Docker installed on machine.

Use next commands to start and init MySql app database:
```shell
docker run -p 3306:3306 --name mysqldb -e MYSQL_ROOT_PASSWORD=root -d mysql:latest
```

Docker container will start on 3306 port.

```shell
docker exec -it mysqldb bash
```

MySql container will be opened in bash

```shell
mysql -p
```

Then enter password:
```root```

```mysql
CREATE DATABASE anti_plagiarism;
```

This will create empty database.

App is ready to run.