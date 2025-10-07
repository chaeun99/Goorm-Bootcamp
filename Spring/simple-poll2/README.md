**Spring Boot REST API를 활용해 메모리 기반의 투표 시스템을 구현하세요.**

1. VoteRequest 클래스 작성 – 사용자가 투표할 옵션을 JSON으로 전달받는 요청 DTO 구현

2. PollResult 클래스 작성 – 옵션명, 득표수, 득표율, 1위 여부를 포함하는 응답 DTO 구현

3. PollService 서비스 작성 – ConcurrentHashMap 기반 투표 저장, vote(), getResults(), resetVotes() 메서드 구현

4. PollController REST 컨트롤러 작성 – /api/polls/options(투표 가능 항목 조회), /api/polls/vote(투표), /api/polls/results(결과 조회), /api/polls/reset(초기화) 엔드포인트 구현

\[**출력 예시**(JSON) \]

{

"success": true,

"totalVotes": 3,

"results": \[

{"name": "Java", "votes": 2, "percentage": 66.7, "top": true},

{"name": "Python", "votes": 1, "percentage": 33.3, "top": false},

{"name": "JavaScript", "votes": 0, "percentage": 0.0, "top": false},

{"name": "C++", "votes": 0, "percentage": 0.0, "top": false}

\]

}