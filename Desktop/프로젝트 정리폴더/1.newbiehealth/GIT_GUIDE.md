# 🧭 NEWBIEFITNESS Git 협업 가이드 (4인 팀)

## 👤 팀 구성

| 이름 | 역할 | 브랜치 예시 |
|------|------|-------------|
| 팀장 (당신) | 전체 코드 리뷰 및 PR 병합 | `develop`, `main`, `feat/운동추천` |
| 팀원 A | 로그인 기능 | `feat/login` |
| 팀원 B | 회원가입 기능 | `feat/signup` |
| 팀원 C | 통계 기능 | `feat/statistics` |

## 🗂️ 브랜치 구조

```
main                👉 실제 배포용 (절대 직접 작업 ❌)
│
├── develop         👉 검증된 코드 통합 브랜치 (PR로만 병합 가능)
│
├── feat/login      👉 팀원 A 로그인 기능 개발
├── feat/signup     👉 팀원 B 회원가입 기능 개발
├── feat/statistics 👉 팀원 C 통계 기능 개발
├── feat/운동추천   👉 팀장이 개발할 기능
```

## 🔐 브랜치 보호 설정 (팀장만 설정 가능)

| 브랜치 | 설정 |
|--------|------|
| `main` | 직접 push 금지, PR로만 병합 |
| `develop` | 최소 1인 리뷰, 직접 push 금지, PR 필수 |

## 🧑‍💻 팀원 개발 흐름

### 🔧 기능 개발 시작

```bash
git checkout develop
git pull origin develop
git checkout -b feat/기능이름
```

### 📝 커밋 & 푸시

```bash
git add .
git commit -m "feat: 로그인 기능 구현"
git push -u origin feat/기능이름
```

### 🔀 Pull Request (PR) 생성

- GitHub에서 PR 생성
- base: `develop`
- compare: `feat/기능이름`
- 제목 예시: `feat: 로그인 기능 구현`
- 설명에 구현 내용 간단히 기재

## ✅ 팀장 리뷰 & 병합 흐름

```bash
git checkout develop
git pull origin develop
git fetch origin
git checkout -b feat/기능이름 origin/feat/기능이름

# 실행 및 테스트

# 문제 없으면 GitHub에서 PR Merge
git checkout develop
git pull origin develop  # 머지된 develop 최신화
```

## 📌 브랜치 네이밍 규칙

| 접두사 | 의미 | 예시 |
|--------|------|------|
| `feat/` | 새로운 기능 | `feat/login` |
| `fix/` | 버그 수정 | `fix/login-crash` |
| `refactor/` | 리팩토링 | `refactor/signup-form` |
| `docs/` | 문서 변경 | `docs/readme` |

## ❗ 규칙 요약

- ✅ 무조건 `develop`에서 브랜치 따서 작업
- ✅ 기능 완료 시 PR 생성 (→ 팀장이 확인 후 merge)
- ❌ `main`/`develop`에 직접 push 금지
- ✅ 커밋 메시지 통일 (`feat: ~`, `fix: ~`)
- ✅ PR 내용은 명확하게 작성