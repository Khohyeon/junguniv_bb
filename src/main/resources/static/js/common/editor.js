document.addEventListener('DOMContentLoaded', function () {
    // TinyMCE 초기화
    tinymce.init({
        selector: '#editor',
        height: 400,
        menubar: true,
        plugins: [
            'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'print', 'preview', 'anchor',
            'searchreplace', 'visualblocks', 'code', 'fullscreen',
            'insertdatetime', 'media', 'table', 'paste', 'help', 'wordcount'
        ],
        toolbar: 'undo redo | formatselect | bold italic backcolor | ' +
            'alignleft aligncenter alignright alignjustify | ' +
            'bullist numlist outdent indent | removeformat | help'
    });

    // 폼 제출 시 데이터 동기화 및 유효성 검사
    const form = document.getElementById('popupForm');
    form.addEventListener('submit', function (e) {
        // TinyMCE 데이터 동기화
        tinymce.triggerSave();

        // 유효성 검사
        const editorContent = tinymce.get('editor').getContent({ format: 'text' }).trim();
        if (!editorContent) {
            e.preventDefault();
            alert('내용을 입력해주세요.');
        }
    });
});
