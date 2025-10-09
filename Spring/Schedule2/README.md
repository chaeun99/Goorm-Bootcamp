**Spring Boot REST API를 활용하여 개인 시간표를 주간 단위로 관리하고 JSON으로 데이터를 제공하는 API 서버를 구현합니다**. DB 없이 메모리 기반으로 동작합니다.

1. 1\. ScheduleEntry 모델 및 ScheduleRequest DTO 작성 – 제목, 요일(DayOfWeek), 시작/종료 시간(LocalTime), 장소, 메모 필드 포함

2. 2\. ScheduleService 구현 – 일정 CRUD, 시간 충돌 검사, 주간 시간표(30분 단위) JSON 변환, 제목 기반 색상 자동 할당, 통계 API 구현

3. 3\. ScheduleController 작성 – REST 엔드포인트(/api/schedules, /api/schedules/{id}, /api/schedules/weekly-table, /api/schedules/stats, /api/schedules/colors/{title}, /api/schedules/days-of-week) 구현

4. 4\. Postman로 API 호출 테스트 – 전체 조회, 단건 조회, 생성, 수정, 삭제, 주간 시간표, 통계 확인

**\[출력 예시 (JSON)\]**

{

"success": true,

"data": {

"totalSchedules": 4,

"schedulesByDay": {

"MONDAY": 2,

"TUESDAY": 1,

"WEDNESDAY": 1,

"THURSDAY": 0,

"FRIDAY": 0,

"SATURDAY": 0,

"SUNDAY": 0

},

"minutesByDay": {

"MONDAY": 150,

"TUESDAY": 90,

"WEDNESDAY": 120,

"THURSDAY": 0,

"FRIDAY": 0,

"SATURDAY": 0,

"SUNDAY": 0

},

"averageDurationMinutes": 90

}

}