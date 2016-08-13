# analytics4github
Java web application to enhance github.com search mechanism.
Provided options facilitate search of new perspective projects with good commits/stars/contributors grown.
Internally. The existed GitHub API is used.  
[http://analytics4github-lyashenkogs.rhcloud.com/](http://analytics4github-lyashenkogs.rhcloud.com/)
## Prerequisites

* Java 1.8
* Gradle >= 2.3 
* Generate OAuth token for Github [https://github.com/settings/tokens](https://github.com/settings/tokens)
and copy it to /var/token.txt
## Development
### IntellijIdea
To reload controllers after editing - press ctl + f9 and wait till application restart.
It'll execute "Make" and trigger hot-redeploy via spring-boot-devtools.
