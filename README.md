# 광운대학교 학생 맞춤형 강의 및 시간표 추천 웹 서비스 Back-end
<img width="209" alt="스크린샷 2023-01-10 오후 6 29 38" src="https://user-images.githubusercontent.com/99861250/211513787-c848504d-e681-4f8c-b207-65ffd8b2d0d1.png">
<img src="https://img.shields.io/badge/version-v1.0.0-red"/>

## 🔎 기능 소개
* 수강신청 자료집을 엑셀로 데이터를 직접 가공 후, DB에 저장

* [_KLAS_](https://klas.kw.ac.kr/usr/cmn/login/LoginForm.do?redirectUrl=/std/cmn/frame/Frame.do) 데이터를 연동

* 사용자의 수강 데이터를 기반으로 하여 개인화된 대시보드를 제작 및 시간표 관련 통계자료 제공

* 각 강의 목록에서 선택 시, 시간표에 강의 추가 
   * 해당 강의를 시간표에 담은 사람들이 많이 담은 강의 추천
   * 해당 시간과 겹치는 다른 강의를 목록에서 비활성화 
   * 선이수 강의 미이수 혹은, 타과 강의일 경우 경고문구 출력 
   * 재수강이 불가능한 강의의 경우, 비활성화
* 강의 시간표 추천 (동일 학년/학부 학생들이 많이 담은 과목들에 대한 강의들로 구성) 

* 졸업이수학점 산정 + 졸업요건에 따른 사용자 강의 추천

## 🔗 관련 링크
|[Front-end](https://github.com/19-21-40/front-prototype/blob/master/README.md "광운대학교 학생 맞춤형 강의 및 시간표 추천 웹 서비스 Front-end")|[Back-end](https://github.com/19-21-40/back_prototype/blob/master/README.md "광운대학교 학생 맞춤형 강의 및 시간표 추천 웹 서비스 Back-end")|
|-------------------------------|-------------------------------|

## 📃 기술 스택
### Database
* MySQL

### Framework
* Spring Boot
* Hibernate

### Infrastructure
* AWS EC2


## ⚙️ 시스템 아키텍처
![참빛 시스템아키텍처 하얀배경](https://user-images.githubusercontent.com/89342648/211332343-fb828d8d-c0c8-4aca-979e-8346bfcb167f.png)

## 📁 DB 구조
![참빛 DB](https://user-images.githubusercontent.com/89342648/212599883-a295b786-2565-4d8d-a776-535e742f317f.png)
* `lecture` : 매학기 학교에서 제공하는 개설과목 4개년치(2019~2022)를 엑셀로 데이터 가공, 중복제거 후 저장한 모든 과목 정보 테이블
* `lecture_time_slot` : `lecture` 와 `time_slot` 을 매핑해주는 중간 테이블
* `timetable_lecture` : `timetable` 과 `lecture` 를 매핑해주는 중간 테이블
* `student_lecture` : `student` 와 `lecture` 를 매핑해주는 중간 테이블

## 🗂️ Entitiy 구조
![클래스다이어그램 drawio](https://user-images.githubusercontent.com/89342648/212615762-40056e62-ca8a-483a-8592-db779ba303dc.svg)

* `Credit` : 전공학점, 부전공학점, 교양학점과 관련된 엔티티
