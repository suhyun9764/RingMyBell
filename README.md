# 🚑 Emergency Vehicle and User Management API

이 프로젝트는 **응급차량과 일반 사용자**의 로그인, 회원가입, 소켓 연결 기능을 제공하는 API입니다. 이를 통해 **응급차량의 활동 정보**를 관리하고, **일반 사용자가** 자신을 등록하고 로그인할 수 있습니다.

---

## 🚀 기능

- **로그인 API**: 사용자 로그인 및 인증 토큰 발급
- **응급차량 회원가입 API**: 응급차량 정보 등록
- **일반 유저 회원가입 API**: 일반 사용자 정보 등록
- **아이디/이메일 사용 가능 여부 확인 API**: 아이디 및 이메일 중복 확인
- **기관 및 하위 부서 검색 API**: 기관 및 부서 정보 검색
- **소켓 연결 API**: 응급차량과 사용자 각각의 소켓 연결 지원

---

## 🛠️ 기술 스택

- **백엔드**: Spring Boot
- **데이터베이스**: MySQL
- **캐시**: Redis
- **보안**: JWT (JSON Web Token)
- **소켓 통신**: WebSocket

---

## 💡 설치 방법

### 1. 프로젝트 클론

```bash
git clone https://github.com/suhyun9764/RingMyBell.git
cd RingMyBell
```

### 2. 종속성 설치

프로젝트의 종속성을 설치하려면 아래 명령어를 실행합니다.

```bash
./mvnw install
```

### 3. 환경 설정

`application.properties` 또는 `application.yml` 파일을 수정하여 환경 설정을 추가합니다.

```properties
# 예시 환경 설정
spring.jwt.secretKey=your-secret-key
redis.host=your-redis-server
redis.port=6379
```

---

## 📑 API 명세서

| **API 기능**                        | **HTTP 메소드** | **경로**                                     | **Request**                                                                                  | **Response**                                                                                      |
|------------------------------------|----------------|-------------------------------------------|----------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| **로그인 API**                     | POST           | `/api/auth/login`                         | `{ 'username': '아이디', 'password': '비밀번호', 'loginType': '로그인 타입(1 또는 2)' }`          | - 로그인 성공 시 `Authorization` 헤더와 `Set-Cookie`에 `Refresh Token` 포함.<br> - 인증 실패 시 오류 메시지 |
| **응급차량 회원가입 API**           | POST           | `/api/dispatch/join`                      | `{ 'vehicleNumber': '차량번호', 'password': '비밀번호', 'subUnit': '부서명', 'activityArea': '활동지역' }` | - 차량 등록 성공 시 `Response` 객체 반환.<br> - 차량번호 중복, 형식 오류, 부서 선택 오류 시 오류 메시지     |
| **기관 및 하위 부서 검색 API**      | GET            | `/api/dispatch/join/search/station-subunit` | -                                                                                           | - 검색된 기관 및 하위 부서 정보 반환                                                             |
| **일반유저 회원가입 API**           | POST           | `/api/user/join`                          | `{ 'username': '아이디', 'password': '비밀번호', 'email': '이메일', 'name': '이름', 'phone': '전화번호' }` | - 회원가입 완료 시 `Response` 객체 반환.<br> - 아이디 중복, 이메일 중복, 형식 오류 시 오류 메시지        |
| **유저 아이디 사용 가능 여부 확인 API** | GET            | `/api/user/validate/username`             | `username` 파라미터: '아이디'                                                                 | - 아이디 사용 가능 여부와 오류 메시지 반환 (사용 가능, 중복, 글자 수 오류 등)                    |
| **유저 이메일 사용 가능 여부 확인 API** | GET            | `/api/user/validate/email`                | `email` 파라미터: '이메일'                                                                    | - 이메일 사용 가능 여부와 오류 메시지 반환 (사용 가능, 중복, 형식 오류 등)                      |
| **차량 소켓 연결 API**               | WebSocket      | `/vehicle-socket`                         | - `Authorization` 헤더에 JWT 토큰 포함                                                       | - 소켓 연결 성공 시 200 OK 반환.<br> - 401 Unauthorized: 토큰이 유효하지 않거나 권한이 없는 경우 반환. |
| **사용자 소켓 연결 API**            | WebSocket      | `/user-socket`                            | - `Authorization` 헤더에 JWT 토큰 포함                                                       | - 소켓 연결 성공 시 200 OK 반환.<br> - 401 Unauthorized: 토큰이 유효하지 않거나 권한이 없는 경우 반환. |

---

## 🏃‍♂️ 실행 방법

### 로컬 서버 실행

1. **애플리케이션 실행**:
   ```bash
   ./mvnw spring-boot:run
   ```

2. **서버 확인**: `http://localhost:8080`에서 API를 테스트할 수 있습니다.

### Docker를 통한 실행

1. **이미지 빌드**:
   ```bash
   docker build -t your-image-name .
   ```

2. **컨테이너 실행**:
   ```bash
   docker run -p 8080:8080 your-image-name
   ```

---
