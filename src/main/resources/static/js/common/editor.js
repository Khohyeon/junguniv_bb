document.addEventListener('DOMContentLoaded', function () {
    // TinyMCE 초기화
    tinymce.init({
        selector: '#editor',
        height: 400,
        menubar: true,
        plugins: [
            'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'print', 'preview', 'anchor',
            'searchreplace', 'visualblocks', 'code', 'fullscreen', 'paste image imagetools',
            'insertdatetime', 'media', 'table', 'paste', 'help', 'wordcount'
        ],
        paste_data_images: true, // 이미지 붙여넣기 설정 활성화
        file_picker_types: 'image', // TinyMCE에서 이미지를 선택할 때, 이미지 파일만 선택 (옵션 : media, file 등)
        images_upload_handler(blobInfo, success) { // 이미지를 업로드하는 핸들러 함수
            // blobInfo : TinyMCE에서 이미지 업로드 시 사용되는 정보를 담고 있는 객체
            const file = new File([blobInfo.blob()], blobInfo.filename());
            const fileName = blobInfo.filename();

            if (fileName.includes("blobid")) {
                success(URL.createObjectURL(file));
            } else {
                imageFiles.push(file);
                success(URL.createObjectURL(file)); // Blob 객체의 임시 URL을 생성해 이미지 미리보기 적용
            }
        },
        toolbar: 'undo redo | formatselect | bold italic backcolor | ' +
            'alignleft aligncenter alignright alignjustify | ' +
            'bullist numlist outdent indent | removeformat | help'
    });


});
