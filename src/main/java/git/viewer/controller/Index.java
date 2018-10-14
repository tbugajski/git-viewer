package git.viewer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Index {

    @RequestMapping()
    public Map<String,String> index() {
        Map<String,String> urls = new HashMap<>();
        urls.put("FULL_URL","protocol://host:port/{user}/repos?sort={NAME}&order={DESC,ASC}&updated={true,false}");
        urls.put("NAME","http://localhost:8080/spring-projects/repos?sort=NAME");
        urls.put("NAME&DESC","http://localhost:8080/spring-projects/repos?sort=NAME&order=DESC");
        urls.put("NAME&ASC","http://localhost:8080/spring-projects/repos?sort=NAME&order=ASC");
        urls.put("UPDATED","http://localhost:8080/spring-projects/repos?updated=true");
        urls.put("REPOS","http://localhost:8080/spring-projects/repos");
        return urls;
    }

}