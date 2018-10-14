package git.viewer.service;

import git.viewer.model.Order;
import git.viewer.model.Repo;
import git.viewer.model.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    public List<Repo> getRepos(String user, Boolean updated, Sort sort, Order order) {
        List<Repo> repos = getRepos(user);
        repos = filterUpdated(updated, repos);
        repos = sort(sort, order, repos);
        return repos;
    }

    private List<Repo> sort(Sort sort, Order order, List<Repo> repos) {
        if (sort != null) {
            if (order != null && order.equals(Order.DESC)) {
                repos = repos.stream().sorted(Comparator.comparing(Repo::getName).reversed()).collect(Collectors.toList());
            } else if (order != null && order.equals(Order.ASC)) {
                repos = repos.stream().sorted(Comparator.comparing(Repo::getName)).collect(Collectors.toList());
            }
        }
        return repos;
    }

    private List<Repo> filterUpdated(Boolean updated, List<Repo> repos) {
        Predicate<Repo> isUpdated = repo -> updated.equals(repo.isUpdated());
        if (updated != null) {
            repos = repos.stream().filter(isUpdated).collect(Collectors.toList());
        }
        return repos;
    }

    private List<Repo> getRepos(String user) {
//        Map<String, String> params = new HashMap<String, String>() {{
//            put("user", user);
//            put("updated", String.valueOf(updated));
//            put("sort", String.valueOf(sort));
//            put("order", String.valueOf(order));
//        }};
        //.of("user", user, "updated", updated, "sort", sort);
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
