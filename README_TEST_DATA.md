# 테스트 데이터 삽입 가이드

## 개요
`insert_test_data.sql` 파일에는 Studiz 서버의 모든 기능을 테스트할 수 있는 예시 데이터가 포함되어 있습니다.

## 포함된 데이터
- **5명의 사용자** (admin, user1~user4)
- **3개의 스터디** (알고리즘, Spring Boot, React)
- **스터디 멤버 관계**
- **6개의 일정** (확정된 일정 포함)
- **일정 슬롯 및 가용성**
- **6개의 할 일** (완료된 것과 미완료 포함)
- **할 일 멤버 할당**
- **6개의 알림**

## 사용 방법

### 1. BCrypt 비밀번호 해시 생성

SQL 파일의 비밀번호 해시는 기본값입니다. 실제 사용하려면 BCrypt 해시를 생성해야 합니다.

**방법 1: Spring Boot 애플리케이션에서 생성**

```bash
# 로컬에서 Spring Boot 애플리케이션 실행 후
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "loginId": "test",
    "password": "Password123!",
    "name": "테스트"
  }'

# 데이터베이스에서 생성된 해시 확인
# 또는 아래 Java 코드로 해시 생성
```

**방법 2: 간단한 Java 코드로 해시 생성**

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String hash = encoder.encode("Password123!");
System.out.println(hash);
```

**방법 3: 온라인 BCrypt 해시 생성기 사용**
- https://bcrypt-generator.com/
- https://www.bcrypt.fr/

### 2. SQL 파일 수정

`insert_test_data.sql` 파일을 열고, 모든 `$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy` 부분을 위에서 생성한 실제 BCrypt 해시로 교체하세요.

### 3. 데이터베이스에 삽입

**EC2에 SSH 접속 후:**

```bash
# 1. SQL 파일을 EC2로 전송
scp -i ~/.ssh/studiz-server-key.pem insert_test_data.sql ec2-user@3.27.86.20:/tmp/

# 2. EC2에 SSH 접속
ssh -i ~/.ssh/studiz-server-key.pem ec2-user@3.27.86.20

# 3. 데이터베이스에 SQL 파일 실행
sudo -u postgres psql -d studiz -f /tmp/insert_test_data.sql

# 또는 비밀번호로 접속
PGPASSWORD=studizpassword! psql -h localhost -U studiz -d studiz -f /tmp/insert_test_data.sql
```

### 4. 데이터 확인

```bash
# EC2에서 데이터베이스 접속
sudo -u postgres psql -d studiz

# 각 테이블의 데이터 개수 확인
SELECT 'Users' as table_name, COUNT(*) as count FROM users
UNION ALL
SELECT 'Studies', COUNT(*) FROM studies
UNION ALL
SELECT 'Study Members', COUNT(*) FROM study_members
UNION ALL
SELECT 'Schedules', COUNT(*) FROM schedules
UNION ALL
SELECT 'Schedule Slots', COUNT(*) FROM schedule_slots
UNION ALL
SELECT 'Schedule Availability', COUNT(*) FROM schedule_availability
UNION ALL
SELECT 'Todos', COUNT(*) FROM todos
UNION ALL
SELECT 'Todo Members', COUNT(*) FROM todo_members
UNION ALL
SELECT 'Notifications', COUNT(*) FROM notifications;

# 특정 사용자로 로그인 테스트
# loginId: admin, user1, user2, user3, user4
# password: Password123! (SQL 파일에서 해시로 교체한 경우)
```

## 테스트 계정 정보

모든 사용자의 비밀번호는 `Password123!`입니다 (BCrypt 해시로 변환 필요).

| loginId | name | 역할 |
|---------|------|------|
| admin | 관리자 | 알고리즘 스터디 소유자 |
| user1 | 홍길동 | Spring Boot 스터디 소유자, 알고리즘 스터디 멤버 |
| user2 | 김철수 | React 스터디 소유자, 여러 스터디 멤버 |
| user3 | 이영희 | 여러 스터디 멤버 |
| user4 | 박민수 | 여러 스터디 멤버 |

## 스터디 정보

| 스터디 이름 | 초대 코드 | 소유자 | 멤버 수 |
|------------|----------|--------|---------|
| 알고리즘 스터디 | ALGO2024 | admin | 4명 |
| Spring Boot 스터디 | SPRING01 | user1 | 3명 |
| React 스터디 | REACT01 | user2 | 3명 |

## 주의사항

1. **비밀번호 해시**: SQL 파일의 비밀번호 해시는 예시입니다. 실제 사용 전에 BCrypt 해시로 교체해야 합니다.

2. **UUID 충돌**: SQL 파일의 UUID는 예시입니다. 실제 환경에서는 충돌 가능성이 낮지만, 필요시 변경하세요.

3. **날짜**: 일정과 할 일의 날짜는 현재 날짜 기준으로 설정되어 있습니다. 필요시 수정하세요.

4. **데이터 중복**: `ON CONFLICT DO NOTHING` 구문을 사용하여 중복 삽입을 방지했습니다. 기존 데이터가 있으면 건너뜁니다.

## 데이터 초기화

모든 테스트 데이터를 삭제하려면:

```sql
-- 주의: 모든 데이터가 삭제됩니다!
DELETE FROM notifications;
DELETE FROM todo_members;
DELETE FROM todos;
DELETE FROM schedule_availability;
DELETE FROM schedule_slots;
DELETE FROM schedules;
DELETE FROM study_members;
DELETE FROM studies;
DELETE FROM users;
```

## 문제 해결

### 비밀번호 해시가 맞지 않는 경우
- Spring Boot 애플리케이션에서 `PasswordEncoder`를 사용하여 새 해시 생성
- 또는 회원가입 API를 통해 사용자 생성 후 SQL로 나머지 데이터 삽입

### 외래 키 제약 조건 오류
- 데이터 삽입 순서 확인: users → studies → study_members → schedules → ...
- 참조하는 ID가 실제로 존재하는지 확인

### UUID 형식 오류
- PostgreSQL의 UUID 형식 확인: `550e8400-e29b-41d4-a716-446655440001`
- 하이픈 포함 여부 확인



