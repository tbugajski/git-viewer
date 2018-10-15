# git-viewer

Projekt git-viewer spełnia założenia:

Funkcjonalne:

  Wystawiony jest endpoint ("protocol://host:port/{user}/repos?sort={NAME}&order={DESC,ASC}&updated={true,false}"). 
  Pod adresem "/" są wypisane wszystkie url-e z parametrami - przykładowe dane.
  Dane do logowania są konfigurowalne z poziomu pliku application.properties, domyślnie user:user, user1:user1,
  do którego ścieżka musi być podana w zmiennej środowiskowej np. -Dspring.config.location=file:///D://application.properties
    
Techniczne:

  Github zwraca 403 w przypadku przekroczenia limitu zapytań(https://developer.github.com/v3/#rate-limiting), 
  dlatego dorzuciłem prostego cache'a (ttl 5sek), który umożliwi wywołanie większej ilości zapytań.

Technologiczne:

    Java 9 lub Kotlin, Spring Boot 1.5.9 lub 2.0.0, Gradle lub Maven

Opcjonalne (dodatkowo punktowane):

    Przygotowałem Dockerfile-a który umożliwia uruchomienie aplikacji
    
Pliki:

    Binarka - https://github.com/tbugajski/git-viewer/releases/download/v1.0/git-viewer.jar
    Dockerfile - https://github.com/tbugajski/git-viewer/blob/master/Dockerfile
    Plik konfiguracyjny z danymi do logowania - https://github.com/tbugajski/git-viewer/blob/master/application.properties 
