# 🔐 인증(Auth) API

> 회원가입과 로그인 기능을 제공합니다.

## 📌 기본 정보
- **Base URL**: `http://localhost:8080/auth`
- **Content-Type**: `application/json`

---

## 1. 회원가입 📝

**새로운 사용자를 등록합니다.**

### 요청
```http
POST /auth/register
Content-Type: application/json

{
  "loginId": "user123",
  "password": "password123",
  "name": "홍길동"
}
```

### 성공 응답 (201 Created)
```json
{
  "id": 1,
  "loginId": "user123",
  "name": "홍길동",
  "createdAt": "2024-10-14T10:30:00"
}
```

### 입력 제한사항
| 필드 | 필수 | 조건 |
|------|------|------|
| loginId | ✅ | 4~20자, 영문/숫자/언더스코어(_)/하이픈(-) 가능 |
| password | ✅ | 4자 이상 |
| name | ✅ | 2~50자 |

### 에러 케이스
| 상태 코드 | 메시지 | 설명 |
|----------|--------|------|
| 409 | 이미 사용 중인 아이디입니다 | loginId 중복 |
| 400 | 유효하지 않은 입력값입니다 | 필드 검증 실패 |

---

## 2. 로그인 🔑

**등록된 사용자로 로그인합니다.**

### 요청
```http
POST /auth/login
Content-Type: application/json

{
  "loginId": "user123",
  "password": "Test1234!@#"
}
```

### 성공 응답 (200 OK)
```json
{
  "userId": 1,
  "loginId": "user123",
  "name": "홍길동",
  "message": "로그인 성공"
}
```

### 에러 케이스
| 상태 코드 | 메시지 | 설명 |
|----------|--------|------|
| 404 | 사용자를 찾을 수 없습니다 | 존재하지 않는 아이디 |
| 401 | 비밀번호가 일치하지 않습니다 | 잘못된 비밀번호 |
| 400 | 유효하지 않은 입력값입니다 | 필드 검증 실패 |

---

## 💡 프론트엔드 사용 예시

### JavaScript (Fetch)
```javascript
// 회원가입
async function signup(loginId, password, name) {
  const response = await fetch('http://localhost:8080/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ loginId, password, name })
  });
  
  if (response.ok) {
    const data = await response.json();
    console.log('회원가입 성공:', data);
    return data;
  } else {
    const error = await response.json();
    alert(error.message);
    throw error;
  }
}

// 로그인
async function login(loginId, password) {
  const response = await fetch('http://localhost:8080/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ loginId, password })
  });
  
  if (response.ok) {
    const data = await response.json();
    console.log('로그인 성공:', data);
    // userId를 저장해서 다른 API 호출 시 사용
    localStorage.setItem('userId', data.userId);
    return data;
  } else {
    const error = await response.json();
    alert(error.message);
    throw error;
  }
}
```

### React 컴포넌트 예시
```jsx
import { useState } from 'react';

function SignupForm() {
  const [formData, setFormData] = useState({
    loginId: '',
    password: '',
    name: ''
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const response = await fetch('http://localhost:8080/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        const data = await response.json();
        alert('회원가입 성공!');
        // 로그인 페이지로 이동
      } else {
        const error = await response.json();
        alert(error.message);
      }
    } catch (error) {
      console.error('오류:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        placeholder="아이디 (4-20자)"
        value={formData.loginId}
        onChange={(e) => setFormData({...formData, loginId: e.target.value})}
      />
      <input
        type="password"
        placeholder="비밀번호 (4자 이상)"
        value={formData.password}
        onChange={(e) => setFormData({...formData, password: e.target.value})}
      />
      <input
        type="text"
        placeholder="이름 (2-50자)"
        value={formData.name}
        onChange={(e) => setFormData({...formData, name: e.target.value})}
      />
      <button type="submit">회원가입</button>
    </form>
  );
}
```

---

## 🎨 에러 응답 형식

```json
{
  "status": 400,
  "code": "INVALID_INPUT",
  "message": "유효하지 않은 입력값입니다.",
  "timestamp": "2024-10-14T10:30:00"
}
```

---

## ✅ 테스트 방법

### Postman으로 테스트
1. POST 요청 생성
2. URL: `http://localhost:8080/auth/register` 또는 `/auth/login`
3. Headers: `Content-Type: application/json`
4. Body (raw JSON):
   ```json
   {
     "loginId": "testuser",
     "password": "password123",
     "name": "테스터"
   }
   ```
5. Send 클릭

### 브라우저 콘솔에서 테스트
```javascript
// F12 눌러서 콘솔 열고 실행
fetch('http://localhost:8080/auth/register', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    loginId: 'testuser',
    password: 'Test1234!@#',
    name: '테스터'
  })
})
.then(res => res.json())
.then(data => console.log(data));
```

---

## 📝 참고사항

- ✅ 비밀번호는 암호화되어 저장됩니다 (BCrypt 사용)
- ✅ loginId는 중복 불가능합니다
- ✅ 로그인 성공 시 받은 userId를 저장해서 사용하세요
- ❌ 현재 JWT 토큰은 미구현 (다음 단계 예정)

