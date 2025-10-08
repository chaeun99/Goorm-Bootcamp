**Spring Boot REST API를 활용하여 용돈 사용 내역을 기록하고 월별/카테고리별 통계를 제공하는 RESTful 서버를 구현하세요.** DB는 사용하지 않고 메모리 기반으로 동작합니다.

1. PocketMoneyEntry 모델 클래스 작성 – id, description, amount(BigDecimal), category, date(LocalDate), memo 필드 포함

2. ExpenseRequest DTO 작성 – 클라이언트에서 요청받는 JSON 데이터를 처리

3. PocketMoneyService 작성 – 메모리 기반 CRUD 및 통계 기능(add, findAll, findByMonth, update, delete, getMonthTotal, getDailyAverage, getCategoryTotals, getTopSpendingCategory) 구현

4. PocketMoneyController REST 컨트롤러 작성 – /api/expenses (목록 조회, 등록, 수정, 삭제), /api/expenses/stats (월별 통계), /api/expenses/categories (카테고리 목록) API 구현

**\[출력 예시 (JSON) \]**

{

"success": true,

"data": {

"month": "2024-01",

"totalAmount": 17400,

"dailyAverage": 1159,

"categoryTotals": {

"식비": 11000,

"교통비": 4200,

"간식": 3000,

"쇼핑": 0,

"문화생활": 0,

"기타": 0

},

"topCategory": "식비",

"entryCount": 4

}

}