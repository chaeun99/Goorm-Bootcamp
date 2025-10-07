**Spring Boot MVC를 활용하여 개인 용돈 사용 내역을 기록하고, 월별·카테고리별 통계를 제공하는 미니 가계부 앱을 구현하세요.** DB는 사용하지 않고 메모리 기반으로 동작합니다.

1. PocketMoneyEntry 모델 클래스 작성 – id, description, amount(BigDecimal), category, date(LocalDate), memo 필드 및 포맷팅 메서드 구현

2. PocketMoneyService 작성 – addEntry, findAll, findByMonth, deleteById, getMonthTotal, getCategoryTotals, getDailyAverage, getTopSpendingCategory 등 메모리 기반 CRUD 및 통계 기능 구현

3. PocketMoneyController 작성 – / 경로에서 월별 목록 및 통계 표시, /add 경로에서 지출 추가, /delete/{id} 경로에서 삭제 처리

4. pocket-money.html 뷰 작성 – 월별 조회 내비게이션, 지출 추가 폼, 통계 테이블(총 지출, 일일 평균, 카테고리별 비율, 최고 지출 카테고리), 지출 내역 테이블 출력

**\[출력 예시\]**

💰 디지털 용돈 기입장

✅ 용돈 사용 내역이 추가되었습니다!

❌ 올바른 금액을 입력해주세요.

총 지출: 24,000원 / 일일 평균: 8,000원 / 가장 많이 쓴 분야: 식비

📊 카테고리별 지출 현황

식비: 11,000원 (46%)

교통비: 1,400원 (6%)

간식: 3,000원 (12%)

쇼핑: 6,600원 (27%)

문화생활: 2,000원 (9%)

📋 이번 달 내역

1\. 10-06 \[식비\] 학식 점심 – 4,500원 (친구와 함께)

2\. 10-06 \[간식\] 아메리카노 – 3,000원 (공부하면서)

3\. 10-07 \[교통비\] 버스비 – 1,400원 (학교 왕복)
