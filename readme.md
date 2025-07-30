$팀원

- 초기 개발환경 세팅
  git clone https://github.com/kinhyo97/NEWBIEFITNESS.git
  cd NEWBIEFITNESS
  git checkout -b develop origin/develop

- 기능개발 브랜치 생성
  git checkout develop
  git pull origin develop  # 항상 최신으로
  git checkout -b feat/login-page  # 개인 작업용 브랜치 생성

- 기능개발후
# 코드 수정 후
git add .
git commit -m "feat: 로그인 페이지 구현"
git push -u origin feat/login-page -> pr요청

- 환경바뀔때 이어서 작업하고싶을떄
  (1번컴)
  git checkout -b feat/login-page develop
  git add .
  git commit -m "WIP: 로그인 UI 반쯤 구현"
  git push -u origin feat/login-page
  (2번컴)
  git clone https://github.com/kinhyo97/NEWBIEFITNESS.git
  cd NEWBIEFITNESS
  git checkout feat/login-page
------------------------
- 작업끝나고
  git add .
  git commit -m "feat: 로그인 완료 + 폼 전송 기능"
  git push


$팀장
- 팀원코드확인
  git checkout develop
  git pull origin develop
  git fetch origin
  git checkout -b feat/login-page origin/feat/login-page
- git push -u origin develop

- develop 최신화
  git checkout develop
  git pull origin develop
