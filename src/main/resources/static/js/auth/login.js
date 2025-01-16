    document.addEventListener('DOMContentLoaded', function() {
        const loginForm = document.getElementById('logForm');
        const userIdInput = document.getElementById('userId');
        const pwdInput = document.getElementById('pwd');
        const loginBtn = document.getElementById('loginBtn');

        // 필요한 DOM 요소가 모두 존재하는지 확인
        if (!loginForm || !userIdInput || !pwdInput || !loginBtn) {
            console.error('필요한 DOM 요소를 찾을 수 없습니다.');
            return;
        }

        // 각 요소가 실제로 DOM에 존재하는지 확인
        if (!document.body.contains(loginForm) ||
            !document.body.contains(userIdInput) ||
            !document.body.contains(pwdInput) ||
            !document.body.contains(loginBtn)) {
            console.error('필요한 DOM 요소가 DOM에 존재하지 않습니다.');
            return;
        }

        // 엔터키 이벤트 처리
        userIdInput.addEventListener('keyup', function(event) {
            if (event.key === 'Enter') {
                doSubmit(loginForm);
            }
        });

        pwdInput.addEventListener('keyup', function(event) {
            if (event.key === 'Enter') {
                doSubmit(loginForm);
            }
        });

        // 로그인 버튼 클릭 이벤트
        loginBtn.addEventListener('click', function(event) {
            event.preventDefault();
            doSubmit(loginForm);
        });
    });

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