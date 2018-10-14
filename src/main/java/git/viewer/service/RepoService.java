package git.viewer.service;

import git.viewer.model.Order;
import git.viewer.model.Repo;
import git.viewer.model.Sort;

import java.util.List;

public interface RepoService {
    List<Repo> getRepos(String user, Boolean updated, Sort sort, Order order);
}
