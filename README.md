# analytics4github

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d3a472531c4b46749c7eda1439d746db)](https://www.codacy.com/app/lyashenkogs/analytics4github?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=LyashenkoGS/analytics4github&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/LyashenkoGS/analytics4github.svg?branch=master)](https://travis-ci.org/LyashenkoGS/analytics4github)
[![codecov](https://codecov.io/gh/LyashenkoGS/analytics4github/branch/master/graph/badge.svg)](https://codecov.io/gh/LyashenkoGS/analytics4github)
[![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/LyashenkoGS/analytics4github/blob/master/LICENCE)  


[http://analytics4github.org](http://analytics4github.org)
Java web application to enhance github.com search mechanism.
Provided options facilitate search of new perspective projects with good commits/stars/contributors grown.
Internally, the existed GitHub API is using.
To access a REST API documentation - run the application and access
 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
 
![Demo](https://github.com/LyashenkoGS/analytics4github/blob/master/demo.gif) 


## Prerequisites

* Java 1.8
* Gradle >= 2.3 
* access to [GitHub REST API ](https://developer.github.com/v3/)
* access to [GitHub trending page](https://github.com/trending)
* Generate OAuth token for GitHub [https://github.com/settings/tokens](https://github.com/settings/tokens) and copy it to /var/token.txt or export to an environment variable GITHUB_TOKEN. 
* Ensure that /tmp/mongodb directory exists and can be accessed by a docker daemon

## Deployment
To run locally execute

      gradle build -x test
      java -jar build/libs/*.jar 
      
### Docker
 
* Generate a GitHub token and save to the file token.txt in the project directory    
* To build and run the application with a MongoDB container (access it via [localhost:8081](localhost:8081)): 
 
        ./start_all_In_Docker.sh
 
* To build and run the application container only(access it via [localhost:8080](localhost:8080)):
      
        docker build -t lyashenkogs/analytics4github:0.0.2 .
        docker run -p 8080:8080 lyashenkogs/analytics4github:0.0.2
      
              
## Development
![architecture](./documentation/Arhitecture.png)

##### IntellijIdea
To reload controllers after editing - press ctl + f9 and wait till application restart.
It'll execute "Make" and trigger hot-redeploy via spring-boot-devtools.


