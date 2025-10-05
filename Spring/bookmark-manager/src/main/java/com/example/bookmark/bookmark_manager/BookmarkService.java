package com.example.bookmark.bookmark_manager;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookmarkService {
    private final ConcurrentHashMap<Long, Bookmark> bookmarks = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);
    private static final String URL_REGEX = "^(http|https)://.*";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    @PostConstruct
    public void init() {
        Bookmark b1 = new Bookmark(
                idGenerator.incrementAndGet(),
                "http://spring.io",
                "Spring Framework",
                "Java 기반 엔터프라이즈 프레임워크",
                List.of("Java", "Spring", "Framework")
        );
        bookmarks.put(b1.getId(), b1);

        Bookmark b2 = new Bookmark(
                idGenerator.incrementAndGet(),
                "https://github.com",
                "GitHub",
                "세계 최대 소스코드 호스팅 서비스",
                List.of("Git", "개발도구", "협업")
        );
        bookmarks.put(b2.getId(), b2);

        Bookmark b3 = new Bookmark(
                idGenerator.incrementAndGet(),
                "https://stackoverflow.com",
                "Stack Overflow",
                "개발자 Q&A 커뮤니티",
                List.of("개발", "Q&A", "커뮤니티")
        );
        bookmarks.put(b3.getId(), b3);
    }

    // 북마크 추가
    public Optional<String> add(String url, String title, String description, String tagsString) {
        if (!isValidUrl(url)) {
            return Optional.of("올바른 URL 형식이 아닙니다.");
        }
        if (isUrlDuplicate(url)) {
            return Optional.of("이미 등록된 URL입니다.");
        }


        Bookmark newBookmark = new Bookmark(
                idGenerator.incrementAndGet(),
                url,
                title,
                description,
                parseTags(tagsString)
        );
        bookmarks.put(newBookmark.getId(), newBookmark);
        return Optional.empty();
    }

    public Optional<Bookmark> findById(Long id) {
        return Optional.ofNullable(bookmarks.get(id));
    }

    // 북마크 최신순 반환
    public List<Bookmark> findAll() {
        return bookmarks.values().stream()
                .sorted((b1, b2) -> b2.getId().compareTo(b1.getId()))
                .collect(Collectors.toList());
    }

    // 북마크 수정
    public Optional<String> update(Long id, String url, String title, String description, String tagsString) {
        if (!bookmarks.containsKey(id)) {
            return Optional.of("수정할 북마크를 찾을 수 없습니다.");
        }

        if (isUrlDuplicate(url) && !bookmarks.get(id).getUrl().equalsIgnoreCase(url.trim())) {
            return Optional.of("이미 다른 북마크에 등록된 URL입니다.");
        }

        Bookmark updatedBookmark = new Bookmark(id, url, title, description, parseTags(tagsString));
        bookmarks.put(id, updatedBookmark);

        return Optional.empty();
    }

    // 북마크 삭제
    public boolean deleteById(Long id) {
        return bookmarks.remove(id) != null;
    }

    // 북마크 목록 반환(검색 키워드 및 태그로)
    public List<Bookmark> search(String keyword, String tag) {
        return findAll().stream()
                .filter(b -> {
                    boolean keywordMatch = true;
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        String lowerKeyword = keyword.trim().toLowerCase();
                        keywordMatch = b.getUrl().toLowerCase().contains(lowerKeyword) ||
                                b.getTitle().toLowerCase().contains(lowerKeyword) ||
                                b.getDescription().toLowerCase().contains(lowerKeyword);
                    }

                    boolean tagMatch = true;
                    if (tag != null && !tag.trim().isEmpty()) {
                        String lowerTag = tag.trim().toLowerCase();
                        tagMatch = b.getTags().stream()
                                .anyMatch(t -> t.toLowerCase().contains(lowerTag));
                    }

                    return keywordMatch && tagMatch;
                })
                .collect(Collectors.toList());
    }

    // 인기 태그
    public List<String> getPopularTags() {
        return bookmarks.values().stream()
                .flatMap(b -> b.getTags().stream())
                .collect(Collectors.groupingBy(
                        String::toLowerCase,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 통계 정보 제공
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBookmarks", bookmarks.size());

        Map<String, Long> tagCounts = bookmarks.values().stream()
                .flatMap(b -> b.getTags().stream())
                .collect(Collectors.groupingBy(
                        String::toLowerCase,
                        Collectors.counting()
                ));
        stats.put("tagCounts", tagCounts);
        return stats;
    }

    private boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) return false;
        Matcher matcher = URL_PATTERN.matcher(url.trim());
        return matcher.matches();
    }

    private boolean isUrlDuplicate(String url) {
        return bookmarks.values().stream()
                .anyMatch(b -> b.getUrl().equalsIgnoreCase(url.trim()));
    }

    private List<String> parseTags(String tagsString) {
        if (tagsString == null || tagsString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Stream.of(tagsString.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toList());
    }
}