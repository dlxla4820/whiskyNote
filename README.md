# 🥃 WhiskyReviewer
나만의 위스키를 기록하고 관리하는 앱 — 위스키 DB · 리뷰 · 공유 · 좋아요

## 📌 프로젝트 소개
사용자들이 구매하거나 마셔본 위스키에 대해 리뷰를 저장하고 관리할 수 있는 Android 어플리케이션입니다.  
휴대폰 고유 식별번호 기반으로 별도 회원가입 없이 사용할 수 있으며, 수입식품마루 기반의 공식 수입 위스키 정보를 제공합니다.  
공유 설정을 통해 다른 사용자의 위스키 컬렉션을 탐색하고 좋아요를 남길 수 있습니다.

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| Backend | Spring Boot 3.3.5, Spring Security, Spring Data JPA, Hibernate |
| 인증 | JWT (jjwt 0.11.5), Jasypt |
| DB | MySQL 8 |
| ORM | QueryDSL 5.0 |
| AOP | Spring AOP |
| Build | Gradle / Java 21 |
| Android | Android (Kotlin / Java) |

## 👥 팀 구성

| 역할 | 인원 | 담당 |
|------|------|------|
| Backend | 2명 | 인증, 위스키 DB 연동, 리뷰 관리, 공유 / 좋아요, 기기 이전 |
| Android | 1명 | Android 앱 전체 개발 |

| 앱 개발 | [@applepepsi](https://github.com/applepepsi/WhiskeyReviewer) |
| 백엔드 | [@dlxla4820](https://github.com/dlxla4820/whiskyNote) |


## ⚙️ 주요 기능

### 🔐 인증 / 기기 관리
- 회원가입 없는 로그인 — 휴대폰 고유 식별번호로 자동 등록
- 백업 코드 발급 후 새 기기에 입력 시 데이터 이전 가능

### 📦 위스키 DB
- 수입식품마루 기반 정식 수입 위스키 정보 제공
- 공식 DB에 없는 위스키(독병 등)는 기타로 직접 등록 가능

### 🥃 나만의 위스키 관리
- 위스키 등록 / 수정 / 삭제 / 조회 / 커스텀 설정
- 음용 날짜별 개별 리뷰 작성

### 🌐 공유 & 커뮤니티
- 공유 설정 시 다른 사용자가 내 위스키 컬렉션 열람 가능
- 다른 사용자의 위스키 리뷰에 좋아요 가능

## 📂 프로젝트 구조

```
src
└── main
    └── java
        └── develop
            ├── auth           # JWT 인증 필터 및 토큰 관리
            ├── config         # Spring Security 등 설정
            ├── controller     # 요청 처리
            ├── dto            # 데이터 전송 객체
            ├── entity         # JPA 엔티티
            ├── enums          # Enum 상수 정의
            ├── exception      # 커스텀 예외 및 글로벌 예외 처리
            ├── repository     # DB 접근 (JPA + QueryDSL)
            ├── service        # 비즈니스 로직
            └── utils          # 공통 유틸리티
```
## 🖥 스크린샷
