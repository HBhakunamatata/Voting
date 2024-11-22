const PAGE_WIDTH = 4
const PAGE_SIZE = 9

/**
 * document loaded
 * 1. query the votes and show the pagination
 * 2. add click listener for the button of creating votes
 * 3. add operations (creating, deleting) for vote items
 */
$(function() {
    configCsrfHeaders()
    createPageableVotes()
    searchBtnListener()
    submitVoteListener()
    registerVoteManagers()
})

/**
 * add search button click listener and define its default behavior
 */
function searchBtnListener() {
    $('#button-query-votes').click(event => {
        event.preventDefault()
        let queryWord = getQueryWord()
        createPageableVotes(undefined, undefined, queryWord)
    })
}

/**
 * get query word from search input
 * @returns queryWord
 */
function getQueryWord() {
    return $('#input-query-words').val().trim()
}

/**
 * query the votes and show vote info
 */
function createPageableVotes(pageNo=1, pageSize=PAGE_SIZE, queryWord='') {
    $.get(
        '/votes',
        {'pageNo': pageNo, 'pageSize': pageSize, 'queryWord': queryWord},
        function(votesData) {
            let votes = votesData['pageData']
            showVotes(votes)

            let pageNo = votesData['pageNo']
            let pageTotal = votesData['pageTotal']
            createRegisterPagination(pageNo, pageTotal)
        }
    )
}

/**
 * show the votes data
 */
function showVotes(votesPageData) {
    $('#votes ul').html(() => {
        let resultStr = ``
        for (const vote of votesPageData) {
            let subject = vote['subject']
            let endTime = vote['endTime']
            let voteId = vote['id']

            resultStr = resultStr.concat(
                `<li>
                    <div class="row p-3" style="border-bottom: 1px solid #adb5bd">
                        <a href="/vote/${voteId}"><h3 class="m-2">${subject}</h3></a>
                        <p class="m-2 small">${endTime}</p>
                    </div>
                </li>`
            )
        }
        return resultStr
    })
}

/**
 * submit createVoteForm
 */
function submitVoteListener() {
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
}

