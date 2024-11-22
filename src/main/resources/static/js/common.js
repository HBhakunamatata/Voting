/**
 * For boostrap : check form validation
 */
function checkFormSubmit() {
    const form = document.querySelector('.needs-validation')
    form.addEventListener('submit', event => {
        if (!form.checkValidity()) {
            event.preventDefault()
            event.stopPropagation()
        }
        form.classList.add('was-validated')
    }, false)
}

/**
 * Provide csrf-token and csrf-header for jquery ajax request
 */
function configCsrfHeaders() {
    const csrfToken = $("meta[name='_csrf']").attr("content");
    const csrfHeader = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr) {
        xhr.setRequestHeader(csrfHeader, csrfToken)
    })
}
