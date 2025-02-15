# Kotlin JWT 인증 서버

## 1. 프로젝트 개요
이 프로젝트는 Kotlin과 Spring Boot를 기반으로 한 JWT 인증 서버입니다.
특히 JWT 토큰에 버전 정보를 이용하여 유효하지 않은 토큰을 관리를 구현하는 것이 주요 특징입니다.

### 목적
- JWT를 사용하여 안전하게 사용자 인증 및 인가를 처리합니다.
- 로그인/로그아웃 시 기존 토큰 무효화를 위해 토큰 버전 관리 방식을 적용합니다.

### 주요 사용 사례
- 사용자 로그인 시 JWT 토큰 발급
- API 접근 제어를 위한 JWT 검증
- 로그인/로그아웃 시 토큰 무효화를 통한 보안 강화

## 2. 주요 기능 및 특징
### JWT 기반 인증
- 사용자 로그인 시 JWT 토큰을 발급합니다.
- JWT 유효성 검증을 이용해 API 접근 제어를 수행합니다.

### 버전 관리 기반 로그아웃
- JWT 페이로드에 `version` 필드를 추가하여, 사용자의 토큰 버전을 관리합니다.
- 로그아웃 시, 저장소(DB 또는 Redis)에 저장된 사용자의 JWT 버전을 업데이트하여 기존 토큰의 유효성을 무효화합니다.

### 토큰 검증 및 재발급
- 토큰 검증시에는 JWT 토큰의 유효성만 검사합니다.
- 토큰 재발급 시, JWT에 포함된 버전 정보와 저장소의 버전 정보를 비교하여 유효성을 확인합니다.

### Spring Security 통합
- Spring Security를 활용해 인증 및 인가 로직을 구현합니다.

## 3. 아키텍처 및 설계
### 아키텍처 개요
프로젝트는 Spring Boot 기반으로 동작하며, 주요 구성 요소는 다음과 같습니다:

- **Authentication Controller**
    - 로그인 및 로그아웃, 인증 API 엔드포인트를 제공합니다.
- **JWT Token Provider**
    - JWT 토큰의 생성, 파싱, 검증, 및 버전 확인 기능을 담당합니다.
- **User Service**
    - 사용자 정보 관리 및 JWT 버전 업데이트(로그아웃 처리)를 수행합니다.
- **Spring Security**
    - 인증 및 인가 처리를 위해 통합되어 동작합니다.

### JWT 버전 관리 설계
#### JWT 페이로드 구성
- 로그인 시 발급되는 JWT에 `version` 필드를 포함하여 사용자의 현재 토큰 버전을 명시합니다.

#### 로그아웃 처리
- 사용자가 로그아웃을 요청하면, 해당 사용자의 JWT 버전이 데이터베이스에서 업데이트됩니다.
- 기존에 발급된 토큰은 JWT 내의 `version`과 데이터베이스의 버전이 불일치하게 되어, 더 이상 유효하지 않게 됩니다.

#### 토큰 검증 로직
- 클라이언트 요청 시 전달된 JWT를 검증하고, 페이로드의 `version` 필드와 데이터베이스에 저장된 사용자 버전을 비교합니다.
- 불일치 시 인증 실패 처리를 수행합니다.

### 아키텍처 다이어그램 (예시)
```
       +------------+           +--------------------------+           +-------------------+
       |   Client   |  Login    |  Authentication Controller |  Token   |   JWT Token Provider  |
       | (Browser)  | --------> |      (로그인/로그아웃)      |  발급    |  (토큰 생성 및 검증)   |
       +------------+           +--------------------------+           +-------------------+
                |                                                        ^
                |                                                        |
                |       +------------------+                             |
                |       |    User Service  | <-- 토큰 버전 업데이트 (로그아웃)
                |       | (사용자 정보 관리)|
                |       +------------------+
                |
                | <------ JWT 토큰 포함된 응답 -------------------------
```

## 4. API 문서

### 로그인
- **URL**: `/api/auth/login`
- **Method**: `POST`
- **Request Body**:
```json
{
  "id": "사용자아이디",
  "password": "비밀번호"
}
```
- **Response**:
```json
{
  "accessToken": "발급된 JWT 토큰",
  "refreshToken": "발급된 리프레시 토큰"
}
```

### 로그아웃
- **URL**: `/api/auth/logout`
- **Method**: `POST`
- **Headers**:
    - `Authorization: Bearer <accessToken>`
- **Response**:
```json
{
  "message": "로그아웃 성공"
}
```

#### 동작 설명
- 로그아웃 요청 시, 해당 사용자의 JWT 버전이 업데이트되어 기존 토큰은 더 이상 유효하지 않습니다.

### 토큰 검증
- **URL**: `/api/auth/validate`
- **Method**: `GET`
- **Headers**:
    - `Authorization: Bearer <accessToken>`
- **Response**:
```json
{
  "seq": 1,
  "id": "사용자아이디",
  "name": "사용자이름"
}
```

#### 동작 설명
- 전달된 JWT 토큰의 유효성 및 버전 정보를 검증합니다.

### 토큰 재발급 
- **URL**: `/api/auth/refresh`
- **Method**: `POST`
- **Headers**:
    - `Authorization: Bearer <accessToken>`
    - `Refresh-Token: Bearer <refreshToken>`
- **Response**:
```json
{
  "accessToken": "재발급된 JWT 토큰",
  "refreshToken": "재발급된 리프레시 토큰"
}