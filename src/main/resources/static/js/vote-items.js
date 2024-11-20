/**
 * register vote and items managing
 * 1. vote adding listener
 * 2. vote deleting listener
 */
function registerVoteManagers() {
    addVoteItems()
    deleteVoteItem()
}

/**
 * Static : Add new vote items
 */
function addVoteItems() {
    $("#btnAddItem").click(event => {
        event.preventDefault();
        let itemNo = $('#voteItems div:last-of-type label').html().trim();
        let newItemNo = String.fromCharCode(itemNo.charCodeAt(0) + 1);

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
}

/**
 * Delete vote items
 */
function deleteVoteItem () {
    $('#voteItems .btn-close').click(function () {
        $(this).parent().parent().remove()
    })
}

