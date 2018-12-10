package git.viewer.service;

import git.viewer.model.Order;
import git.viewer.model.Repo;
import git.viewer.model.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class RepoServiceImpl implements RepoService {

    private static final String HTTPS_API_GITHUB_USER_REPOS = "https://api.github.com/users/{user}/repos";

    private final RestTemplate restTemplate;

    @Autowired
    public RepoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(value = "getRepos")
    public List<Repo> getRepos(String user, Boolean updated, Sort sort, Order order) {
        List<Repo> repos = getRepos(user);
        repos = filterUpdated(updated, repos);
        repos = sort(sort, order, repos);
        return repos;
    }

    private List<Repo> sort(Optional<Sort> sort, Order order, List<Repo> repos) {
        if (sort.isPresent()) {
            if (Order.DESC.equals(order)) {
                repos = repos.stream().sorted(Comparator.comparing(Repo::getName).reversed()).collect(Collectors.toList());
            } else if (Order.ASC.equals(order)) {
                repos = repos.stream().sorted(Comparator.comparing(Repo::getName)).collect(Collectors.toList());
            }
        }
        return repos;
    }

    private List<Repo> filterUpdated(Boolean updated, List<Repo> repos) {
        if (updated != null) {
            Predicate<Repo> isUpdated = repo -> updated.equals(repo.isUpdated());
            repos = repos.stream().filter(isUpdated).collect(Collectors.toList());
        }
        return repos;
    }

    private List<Repo> getRepos(String user) {
        Map<String, String> params = Collections.singletonMap("user", user);
        String uri = UriComponentsBuilder.fromHttpUrl(HTTPS_API_GITHUB_USER_REPOS).buildAndExpand(params).toUriString();

        ResponseEntity<List<Repo>> response = restTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Repo>>() {
                });
        List<Repo> repos = response.getBody();
        setUpdated(repos);
        return repos;
    }

    private void setUpdated(List<Repo> repos) {
        if (!CollectionUtils.isEmpty(repos)) {
            repos.forEach(repo -> repo.setUpdated(isUpdated(repo)));
        }
    }


    //was updated in last 3 months
    private boolean isUpdated(Repo repo) {
        return repo.getUpdatedAt().isAfter(LocalDate.now().minusMonths(3));
    }
}
