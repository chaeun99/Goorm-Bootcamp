**Spring Boot MVC를 활용하여 DB 없이 자료구조(Map)만으로 간단한 투표 시스템을 구현하세요.**

**\[요구 사항\]**

1. OptionResult 모델 클래스 작성 : 항목 이름, 득표수, 득표율, 1위 여부 필드 포함

2. PollService 서비스 작성 : ConcurrentHashMap을 사용해 투표 데이터 저장, vote(), getResults(), getTotalVotes() 메서드 구현

3. PollController 작성: / 경로에서 투표 폼과 결과 표시, /vote에서 투표 처리 (POST-Redirect-GET 패턴 적용)

4. poll.html 뷰 작성 : 투표 버튼 UI와 실시간 결과(득표율, 1위 표시, 총 투표수) 출력

**\[출력 예시\]**

프로그래밍 언어 선호도 투표

\[투표하기\]

\- Java \[투표\]

\- Python \[투표\]

\- JavaScript \[투표\]

\- C++ \[투표\]

\[실시간 결과\]

🏆 Java : 5표 (50%)

Python : 3표 (30%)

JavaScript : 2표 (20%)

총 투표수: 10표