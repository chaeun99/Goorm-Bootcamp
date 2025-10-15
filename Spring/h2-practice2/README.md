**ItemControllerTest 실패 케이스 추가**

목표: Controller 계층의 실패 경로를 테스트로 검증하기

1\. @WebMvcTest(ItemController.class) 테스트 클래스 생성

2\. ItemService를 @MockBean(또는 @MockitoBean)으로 등록

3\. 아래 3개 API에 대한 실패 케이스 최소 3개 이상 작성

- GET /api/items/{id}

- POST /api/items

- DELETE /api/items/{id}

4\. 각 테스트 메서드명에 기대 결과 명시 (예: getItemById_NotFound_Returns404)

5\. MockMvc 사용하여 요청·응답 검증

- when(...).thenThrow(...) 또는 thenReturn(...) 이용

6\. 테스트 실행 및 성공/실패 결과 캡처