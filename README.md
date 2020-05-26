# play-slick3 template project

## Getting Started

To run this demo using sbt:

 * `git clone` this repository
 * Update the MySQL server url, username and password in `conf/application.conf`
 * Create a `example` database on your MySQL server.

```mysql
    CREATE DATABASE example;
```

 * Launch the demo using `sbt run`
 * Open the Play web server at <http://localhost:9000>
 * You should be prompted to apply the evolution script. Apply the script.
 * You should now see the app running.
 
 To run this demo using docker:
 
 sbt clean
 sbt dist
 sbt docker:publishLocal
 docker-compose up
 docker-compose up




