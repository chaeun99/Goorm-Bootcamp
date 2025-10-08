**여러 줄의 문장을 입력받아 단어 등장 빈도를 세는 프로그램을 작성하세요.**\
\
1) 입력: BufferedReader를 이용해 표준 입력에서 문장을 여러 줄 읽습니다. (예: 첫 줄에 줄 개수 N, 다음 줄부터 N개의 문장)\
2) 토큰화: 각 문장은 StringTokenizer로 공백 기준 단어를 분리합니다.\
3) 집계: HashMap에 단어별 개수를 저장합니다. (이미 있으면 +1, 없으면 1로 초기화)\
4) 출력 준비: StringBuilder에 단어와 빈도를 "단어 : 횟수" 형태로 담습니다.\
5) Iterator 출력: Iterator를 사용하여 Map 내용을 순회하며 출력합니다.\
6) Stream 출력: 같은 Map을 stream()으로 순회하며 알파벳순으로 정렬된 결과를 출력합니다.\
7) 출력 완료: BufferedWriter 또는 System.out.println으로 한 번에 출력합니다.\
   \
   **\[ 예시 입력 \]**\
   3\
   Hello Java World\
   Java is fun\
   Hello Hello Java\
   \
   **\[ 예시 출력 \]**\
   (Iterator 순회)\
   Hello : 3\
   Java : 3\
   World : 1\
   is : 1\
   fun : 1\
   (Stream 정렬)\
   fun=1\
   Hello=3\
   is=1\
   Java=3\
   World=1\
   \
   **\[ 선택 심화 \]**\
- 가장 많이 나온 단어를 찾아 출력하기.\
- 단어 길이 평균을 Stream으로 계산해보기.