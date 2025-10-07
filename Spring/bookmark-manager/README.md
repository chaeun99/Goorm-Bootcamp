**Spring Boot REST API를 활용하여 북마크를 관리하는 RESTful 서버를 구현하세요. DB는 사용하지 않고 메모리 기반으로 동작합니다.**

1\. Bookmark 모델 클래스 작성 – id, url, title, description, tags(List), createdAt 필드 포함

2\. BookmarkRequest DTO 작성 – 클라이언트 요청 데이터를 받기 위한 JSON DTO 구현

3\. BookmarkService 작성 – 메모리 기반 CRUD 로직(add, findAll, search, update, delete), 태그 정규화 및 URL 중복 검사 포함

4\. BookmarkController 작성 – /api/bookmarks REST 엔드포인트 구현

\- GET /api/bookmarks : 전체 조회, 검색/필터링

\- GET /api/bookmarks/{id} : 단건 조회

\- POST /api/bookmarks : 북마크 생성

\- PUT /api/bookmarks/{id} : 수정

\- DELETE /api/bookmarks/{id} : 삭제

\- GET /api/bookmarks/tags : 태그 목록 조회

\- GET /api/bookmarks/stats : 통계 정보 제공

**\[ 출력 예시 (JSON) \]**

{

"success": true,

"total": 3,

"data": \[

{

"id": 1,

"url": "<https://spring.io>",

"title": "Spring Framework",

"description": "Java 기반의 엔터프라이즈 프레임워크",

"tags": \["Java", "Spring", "Framework"\],

"createdAt": "2025-10-06T10:30:00"

}

\]

}