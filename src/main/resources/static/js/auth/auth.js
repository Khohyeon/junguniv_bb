// 엔터키 이벤트 처리
function enterkey() {
    if (window.event.keyCode == 13) {
        doSubmit(document.logForm);
    }
}

// 로그인 폼 제출 처리
function doSubmit(form) {
    const userId = form.userId.value;
    const pwd = form.pwd.value;

    // 입력값 검증
    if (!userId || userId.length < 4 || userId.length > 15) {
        alert('아이디는 4~15자리로 입력해주세요.');
        form.userId.focus();
        return false;
    }

    if (!pwd || pwd.length < 4 || pwd.length > 20) {
        alert('비밀번호는 4~20자리로 입력해주세요.');
        form.pwd.focus();
        return false;
    }

    // API 호출을 위한 데이터 준비
    const loginData = {
        userId: userId,
        pwd: pwd
    };

    // 로그인 API 호출
    fetch('/api/v1/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData),
        credentials: 'include' // 쿠키 포함
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // 로그인 성공
            const token = data.response.token;
            const member = data.response.member;

            // 토큰을 sessionStorage에 저장
            sessionStorage.setItem('accessToken', token.accessToken);
            sessionStorage.setItem('refreshToken', token.refreshToken);
            
            // 사용자 정보 저장
            sessionStorage.setItem('userInfo', JSON.stringify(member));

            // 메인 페이지로 이동
            window.location.href = '/';
        } else {
            // 로그인 실패
            alert(data.error.message || '로그인에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('로그인 에러:', error);
        alert('로그인 처리 중 오류가 발생했습니다.');
    });

    return false; // 폼 기본 제출 방지
}

// 토큰 갱신 함수
function refreshToken() {
    fetch('/api/v1/auth/refresh-access-token', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem('accessToken')}`
        },
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        if (data.token) {
            sessionStorage.setItem('accessToken', data.token);
        } else {
            // 토큰 갱신 실패 시 로그인 페이지로 이동
            window.location.href = '/login';
        }
    })
    .catch(error => {
        console.error('토큰 갱신 에러:', error);
        window.location.href = '/login';
    });
}

// 로그아웃 함수
function logout() {
    fetch('/api/v1/auth/logout', {
        method: 'GET',
        credentials: 'include'
    })
    .then(response => {
        // 세션스토리지 클리어
        sessionStorage.clear();
        
        // 로그인 페이지로 이동
        window.location.href = '/login';
    })
    .catch(error => {
        console.error('로그아웃 에러:', error);
        alert('로그아웃 처리 중 오류가 발생했습니다.');
    });
}
