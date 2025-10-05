package com.example.bookmark.bookmark_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// http://localhost:8080/

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    private Map<String, String> createErrorResponse(String message) {
        return Collections.singletonMap("error", message);
    }

    // 전체 조회, 검색 필터링
    @GetMapping
    public List<Bookmark> getAllBookmarks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag) {
        return bookmarkService.search(keyword, tag);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookmarkById(@PathVariable Long id) {
        Optional<Bookmark> bookmark = bookmarkService.findById(id);

        if (bookmark.isPresent()) {
            return ResponseEntity.ok(bookmark.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("ID " + id + "를 가진 북마크를 찾을 수 없습니다."));
        }
    }

    // 북마크 생성
    @PostMapping
    public ResponseEntity<?> createBookmark(@RequestBody BookmarkRequest request) {
        Optional<String> error = bookmarkService.add(
                request.getUrl(),
                request.getTitle(),
                request.getDescription(),
                request.getTags()
        );

        if (error.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(error.get()));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "북마크가 성공적으로 생성되었습니다."));
        }
    }

    // 북마크 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBookmark(@PathVariable Long id, @RequestBody BookmarkRequest request) {
        Optional<String> error = bookmarkService.update(
                id,
                request.getUrl(),
                request.getTitle(),
                request.getDescription(),
                request.getTags()
        );

        if (error.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(error.get()));
        } else {
            return ResponseEntity.ok(Collections.singletonMap("message", "북마크가 성공적으로 수정되었습니다."));
        }
    }

    // 북마크 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookmark(@PathVariable Long id) {
        boolean deleted = bookmarkService.deleteById(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("삭제하려는 북마크(ID: " + id + ")를 찾을 수 없습니다."));
        }
    }

    // 태그 목록 조회
    @GetMapping("/tags")
    public List<String> getPopularTags() {
        return bookmarkService.getPopularTags();
    }

    // 통계 정보 제공
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return bookmarkService.getStats();
    }
}