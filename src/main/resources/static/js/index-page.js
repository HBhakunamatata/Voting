/**
 * document loaded
 */
const PAGE_SIZE = 2

let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function(e, xhr) {
    xhr.setRequestHeader(header, token);
});

$(function() {
    deleteVoteItem()
    queryVotes(1, PAGE_SIZE, '')
    $('#button-query-votes').click(event => {
        event.preventDefault()
        let queryWord = $('#input-query-words').val().trim()
        queryVotes(1, PAGE_SIZE, queryWord)
    })

})


/**
 * query the votes and show vote info
 */
function queryVotes(pageNo, pageSize, queryWord) {

    $.get(
        '/votes',
        {'pageNo': pageNo, 'pageSize': pageSize, 'queryWord': queryWord},
        function(data) {
            showVotes(data)
            showPagination(data)
            changeActivePageNo()
        }
    )
}



/**
 * show the votes and pagination
 */
function showVotes(votesData) {
    let votes = votesData['pageData']

    let text = ``

    for (let i = 0; i < votes.length; i++) {
        let subject = votes[i]['subject']
        let endTime = votes[i]['endTime']
        let voteId = votes[i]['id']
        console.log(voteId)

        text = text.concat(
            `<li>
                <div class="row p-3" style="border-bottom: 1px solid #adb5bd">
                    <a href="/vote/${voteId}"><h3 class="m-2">${subject}</h3></a>
                    <p class="m-2 small">${endTime}</p>
                </div>
            </li>`
        )
    }
    $('#votes ul').html(text)
}

/**
 * Dynamic : show pagination
 */
function showPagination(votesData) {
    let width = 4
    let text = ``

    let pageNo = votesData['pageNo']
    let totalPage = votesData['totalPage']

    let windowLow = pageNo - width < 1 ? 1 : pageNo - width
    let windowHigh = pageNo + width > totalPage ? totalPage : pageNo + width

    if (windowLow === pageNo) {
        text = text.concat(`<li class="page-item disabled"><button class="page-link">Prev</button></li>`)
    } else {
        text = text.concat(`<li class="page-item"><button class="page-link">Prev</button></li>`)
    }

    for (let i = 1; i <= totalPage; i++) {
        if (i === pageNo) {
            text = text.concat(`<li class="page-item active"><button class="page-link">${i}</button></li>`)
        } else {
            text = text.concat(`<li class="page-item"><button class="page-link">${i}</button></li>`)
        }
    }

    if (windowHigh === pageNo) {
        text = text.concat(`<li class="page-item disabled"><button class="page-link">Next</button></li>`)
    } else {
        text = text.concat(`<li class="page-item"><button class="page-link">Next</button></li>`)
    }

    $('#pagination ul').html(text)
}


/**
 * Static : add page item listener
 */
function changeActivePageNo() {
    $('.pagination li button').each(function () {
        $(this).click(function(e){
            e.preventDefault()

            let currentPageNo = 0
            let newPageNo = 0

                $('.pagination li button').each(function () {
                if ($(this).parent().hasClass('active')) {
                    currentPageNo = Number($(this).html().trim())
                    $(this).parent().removeClass('active')
                }
            })
            // $(this).addClass('active')

            let clickPage = $(this).html().trim()
            if (isNaN(Number(clickPage))) {
                if (clickPage === 'Prev') {
                    newPageNo = currentPageNo - 1
                } else if (clickPage === 'Next') {
                    newPageNo = currentPageNo + 1
                }
            } else {
                newPageNo = Number($(this).html().trim())
            }

            let queryWord = $('#input-query-words').val().trim()

            queryVotes(newPageNo, PAGE_SIZE, queryWord)

        })
    })
}


/**
 * Static : Add new vote items
 */
$("#btnAddItem").click(event => {
    event.preventDefault();
    let itemNo = $('#voteItems div:last-of-type label').html().trim();
    let newItemNo = String.fromCharCode(itemNo.charCodeAt(0) + 1);
    // console.log(newItemNo);

    let appendText = `
            <div class="row mb-2">
                <div class="col-1">
                    <label for="item-` + newItemNo + `" class="form-label pt-1">` + newItemNo + `.</label>
                </div>
                <div class="col-10">
                    <input type="text" class="form-control" id="item-` + newItemNo + `" placeholder="Create a vote item">
                </div>
                <div class="col-1">
                    <button type="button" class="btn-close" aria-label="Close"></button>
                </div>
            </div>
        `;
    $('#voteItems').append(appendText);
    deleteVoteItem();
});

/**
 * Delete vote items
 */
function deleteVoteItem () {
    $('#voteItems .btn-close').on("click", function () {
        $(this).parent().parent().remove()
    })
}

/**
 * submit createVoteForm
 */
$('#createVoteForm').submit(function (event) {

    event.preventDefault()
    event.stopPropagation()

    let voteSubject = $('#voteSubject').val().trim()
    console.log(voteSubject)

    let voteItems = [];

    $('input[id^="item"]').each(function () {
        let voteItem = {}
        let tag = $(this).attr('id').at(-1)
        voteItem.tag = tag
        let content = $(this).val().trim()
        voteItem.content = content

        voteItems.push(voteItem)
    })
    console.log(voteItems)

    // 结束时间
    let endTime = new Date()
    endTime.setHours(Number($('#hour').val()))
    endTime.setMinutes(Number($('#minute').val()))
    endTime.setSeconds(0)

    let postData = {'voteSubject': voteSubject, 'voteItemForms': voteItems, 'endTime': moment(endTime).format('yyyy-MM-DD HH:mm:ss')}

    console.log(JSON.stringify(postData))

    $.ajax({
        url: '/vote',
        type: 'POST',
        data: JSON.stringify(postData),
        success: function (data) {
            console.log(JSON.stringify(data))
            location.reload()
        },
        contentType: 'application/json',
        error: function (data) {
            console.log(JSON.stringify(data))
        }
    })

})