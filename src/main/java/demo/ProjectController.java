package demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ProjectController {

    @RequestMapping(value = "/stargazers", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public String getStargazersByProject(HttpServletRequest request, HttpServletResponse response) {
       //For development purposes only. Allow to access the endpoint from another domain
        // and so develop front-end independently.
        response.setHeader("Access-Control-Allow-Origin", "*");
        return "[{\n" +
                "                \"name\": \"Stars\",\n" +
                "                \"data\": [42.4, 33.2, 34.5, 39.7, 52.6, 46.8, 51.1]\n" +
                "            }]";
    }
}
