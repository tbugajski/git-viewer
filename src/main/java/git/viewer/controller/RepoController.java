package git.viewer.controller;

import git.viewer.model.Order;
import git.viewer.model.Repo;
import git.viewer.model.Sort;
import git.viewer.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RepoController {

    private final RepoService repoService;

    @Autowired
    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }

    @RequestMapping("/{user}/repos")
    public List<Repo> repos(@PathVariable(value = "user") String user, @RequestParam(value = "updated", required = false) Boolean updated,
                            @RequestParam(value = "order", required = false, defaultValue = "ASC") Order order
            , @RequestParam(value = "sort", required = false) Sort sort) {
        return repoService.getRepos(user, updated, sort, order);
    }
}
