수업 중 실습한 h2-practice프로젝트의 Controller 계층을 MockMvc로 테스트하세요.

서비스/리포지토리는 MockBean으로 대체하고, 실제 HTTP 요청-응답 흐름을 검증합니다.

**1. 테스트 클래스 생성**: [ItemControllerTest.java](http://ItemControllerTest.java)

@WebMvcTest(ItemController.class)

class ItemControllerTest {

@Autowired

private MockMvc mockMvc;

@MockBean

private ItemService itemService;

}

**2. 전체 상품 조회 테스트** (GET /api/items)

**3. 단일 상품 조회 테스트** (GET /api/items/{id})

✔ 존재하는 id → 200 OK / 존재하지 않으면 → 404 Not Found

**4. 상품 등록 테스트** (POST /api/items)

✔ JSON 요청 전송 후 201 Created 응답 및 필드 매핑 확인

**5. 상품 삭제 테스트** (DELETE /api/items/{id})

✔ 존재하는 id → 204 No Content / 존재하지 않으면 → 404 Not Found