    // src/main/resources/static/js/auth/auth-common.js
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