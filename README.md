# movies-battle-app
Movies Battle App

#### Run project
mvn clean spring-boot:run

#### Link Documents
http://localhost:8080/swagger-ui/index.html 

http://localhost:8080/v3/api-docs

#### Coverage
![img_1.png](img_1.png)

#### Apis to Test
https://api.postman.com/collections/2562032-834a9a60-250e-49c4-8ee1-076a96c47ccc?access_key=PMAT-01GS39R06A8RPRX8FR1CXW3C0S
* You need to import the ADA LOCAL ENV.postman_environment.json in your json to use the token replace, or you can do it manually


#### User acccess
* user1
* user2
* user3

All them with 123456 passw


#### Vefify sonnar
mvn verify sonar:sonar -Dsonar.login=alvesdesouzaalex -Dsonar.projectKey=movies-battle-app -Dsonar.host.url=https://sonarcloud.io -Dsonar.password=Arroba30@50

#### URl Sonar
https://sonarcloud.io/organizations/alvesdesouzaalex/projects