/**
 * create pagination and register click event
 * @param pageNo
 * @param pageTotal
 */
function createRegisterPagination(pageNo, pageTotal) {
    createPagination(pageNo, pageTotal)
    onPageNoChange()
}

/**
 * create and show pagination
 */
function createPagination(pageNo, pageTotal) {
    $('#pagination ul').html(() => {
        let text = ``

        let windowLow = pageNo - PAGE_WIDTH < 1 ? 1 : pageNo - PAGE_WIDTH
        if (windowLow === pageNo) {
            text = text.concat(`<li class="page-item disabled"><button class="page-link">Prev</button></li>`)
        } else {
            text = text.concat(`<li class="page-item"><button class="page-link">Prev</button></li>`)
        }

        for (let i = 1; i <= pageTotal; i++) {
            if (i === pageNo) {
                text = text.concat(`<li class="page-item active"><button class="page-link">${i}</button></li>`)
            } else {
                text = text.concat(`<li class="page-item"><button class="page-link">${i}</button></li>`)
            }
        }

        let windowHigh = pageNo + PAGE_WIDTH > pageTotal ? pageTotal : pageNo + PAGE_WIDTH
        if (windowHigh === pageNo) {
            text = text.concat(`<li class="page-item disabled"><button class="page-link">Next</button></li>`)
        } else {
            text = text.concat(`<li class="page-item"><button class="page-link">Next</button></li>`)
        }
        return text
    })
}

/**
 * get current activate pageNo
 * @return currentPageNo
 */
function getActivePageNo() {
    let currentPageNo = 0
    $('.pagination li button').each(function () {
        if ($(this).parent().hasClass('active')) {
            currentPageNo = Number($(this).html().trim())
            $(this).parent().removeClass('active')
        }
    })
    return currentPageNo
}

/**
 * Add pageNo change listener
 * 1. find and remove current active pageNo
 * 2. reaction query votes and set active pageNo
 */
function onPageNoChange() {
    $('.pagination li button').each(function () {
        $(this).click(function(e){
            e.preventDefault()

            let currentPageNo = getActivePageNo()
            let newPageNo = 0
            let clickPageNo = $(this).html().trim()

            if (isNaN(Number(clickPageNo))) {
                if (clickPageNo === 'Prev') {
                    newPageNo = currentPageNo - 1
                } else if (clickPageNo === 'Next') {
                    newPageNo = currentPageNo + 1
                }
            } else {
                newPageNo = Number(clickPageNo)
            }
            let queryWord = getQueryWord()
            createPageableVotes(newPageNo, undefined, queryWord)
        })
    })
}