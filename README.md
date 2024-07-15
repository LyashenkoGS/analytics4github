# analytics4github
[![Build Status](http://195.211.154.179:8081/view/analytics4github/job/master-branch-poling%20and%20redeploy/badge/icon)](http://195.211.154.179:8081/view/analytics4github/job/master-branch-poling%20and%20redeploy/)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d3a472531c4b46749c7eda1439d746db)](https://www.codacy.com/app/lyashenkogs/analytics4github?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=LyashenkoGS/analytics4github&amp;utm_campaign=Badge_Grade)
[![codecov](https://codecov.io/gh/LyashenkoGS/analytics4github/branch/master/graph/badge.svg)](https://codecov.io/gh/LyashenkoGS/analytics4github)
[![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/LyashenkoGS/analytics4github/blob/master/LICENCE)  


[http://195.211.154.179](http://195.211.154.179)
Java web application to enhance github.com search mechanism.
Provided options facilitate search of new perspective projects    Preview changes
with good commits/stars/contributors grown.
Internally, the existed GitHub API is using.
To access a REST API documentation - run the application and access
 [http://195.211.154.179/swagger-ui.html](http://195.211.154.179/swagger-ui.html)
 
![Demo](./documentation/demo.gif) 


## Prerequisites

* JDK 1.8
* access to [GitHub REST API ](https://developer.github.com/v3/)
* access to [GitHub trending page](https://github.com/trending)
* Generate OAuth token for GitHub [https://github.com/settings/tokens](https://github.com/settings/tokens) and copy it to /var/token.txt or export to an environment variable GITHUB_TOKEN. 
* MongoDB 3.6 (docker run --name someMongo -p 27017:27017 mongo:3.6.0-jessie)

## Deployment
To run locally execute

     ./mvnw install -Dmaven.test.skip=true
      java -jar target/analytics4github*.jar 

accessible by default at http://127.0.0.1:8080

     
## Development
![architecture](./documentation/Arhitecture.png)

Tests stopped to work properly via maven but you can run them via Intellij
##### IntellijIdea

###### reload backend
To reload controllers after editing - press ctl + f9 and wait till application restart.
It'll execute "Make" and trigger hot-redeploy via spring-boot-devtools.

### working with frontend
adjust application.properties to use static resources from a file system, not a jar file. It will simplify frontend 
development

