const COLOR_PAN = ['#4587E7','#35AB33','#F5AD1D','#ff7f50','#da70d6','#32cd32','#6495ed']

$(function () {
    configCsrfHeaders()
    let voteChart = initChart()
    queryAndShowVoteResult()
    loadChartData(voteChart)

    let endDate = Date.parse($('#countTime time').attr('datetime'))
    setInterval(countDownTime, 1000, endDate)

    handleResultSubmit()
})

/**
 * initialize vote chart
 * @return voteChart
 */
function initChart() {
    let voteChart = echarts.init(document.getElementById('vote-chart'))
    let option = {
        title: {
            text: 'The percentage for vote items',
        },
        tooltip: {},
        legend: {},
        xAxis: {
            type: 'category'
        },
        yAxis: {},
        series: [
            {
                type: 'bar',
                itemStyle: {
                    color: function (e) {
                        let index = e.dataIndex % COLOR_PAN.length
                        return COLOR_PAN[index]
                    }
                },
                barWidth: '45%',
                label: {
                    show: true,
                    position: 'top',
                    color: 'blank',
                    fontSize: 12
                }
            }
        ]
    };
    voteChart.setOption(option);
    window.addEventListener('resize', function () {
        voteChart.resize()
    })
    return voteChart
}

/**
 * A countdown task for calculate and show vote deadline
 * @param endDate
 */
function countDownTime(endDate) {
    let nowDate = new Date()
    if (endDate - nowDate <= 1000) {
        $('.form-check input[type="checkbox"]').each(function () {
            $(this).attr('disabled', true)
        })
        $('#voteResultBtn').attr('disabled', true)

        $('#countTime time').html('Vote has ended')
        clearInterval(countDownTime)
    }
    else {
        let time = endDate - nowDate
        let hour = Math.floor( time / (1000 * 60 * 60) % 60 )
        let minute = Math.floor( time / (1000 * 60) % 60 )
        let second = Math.floor( time / 1000 % 60 )

        let html = (hour > 9 ? hour : "0" + hour)
            + ":" + (minute > 9 ? minute : "0" + minute)
            + ":" + (second > 9 ? second : "0" + second)
        $('#countTime time').html('Left Time : ' + html)
    }
}

/**
 * query the vote results and modify the checkbox status
 *  if answered, disable checkbox and submit button
 *   and modify the selected checkbox
 *  if not, leave it alone
 */
function queryAndShowVoteResult() {
    let index = location.pathname.lastIndexOf("/") + 1
    let voteId = location.pathname.substring(index)
    $.getJSON('/vote/' + voteId + '/result', function (voteResults) {
        if (Array.isArray(voteResults) && voteResults.length === 0) {
            // leave it alone
        }
        else {
            let itemIdList = voteResults.map(item => item.itemId)
            $('.form-check input[type="checkbox"]').each(function () {
                let itemId = Number($(this).val())
                if (itemIdList.indexOf(itemId) !== -1) {
                    $(this).attr('checked', true)
                }
                $(this).attr('disabled', true)
            })
            $('#voteResultBtn').attr('disabled', true)
        }
    })
}

/**
 * add listener to submit vote results
 * then rewrite the vote result data and reload chart data
 */
function handleResultSubmit() {
    $('#vote-info').submit(function(e) {
        e.preventDefault()
        e.stopPropagation()

        let voteId = $('#voteSubject').attr('data')
        let voteItems = []

        $('input[type="checkbox"]').each(function () {
            if (this.checked) {
                voteItems.push($(this).val())
            }
        })

        let postData = {'voteId': voteId, 'voteItems': voteItems}

        $.ajax({
            type: "PATCH",
            url: '/vote/result',
            data: JSON.stringify(postData),
            contentType: 'application/json',
            success: function(data) {
                queryAndShowVoteResult()
                loadChartData()
            }
        })
    })
}

/**
 * query vote results and load data into chart
 * @param voteChart
 */
function loadChartData(voteChart) {
    let voteId = $('#voteSubject').attr('data')

    $.get('/vote/' + voteId + '/summary', function (data) {

        let total = 0
        let tags = []
        let summaries = []

        for (const i of data) {
            tags.push(i['tag'])
            summaries.push(i['summary'])
            total += i['summary']
        }

        voteChart.setOption({
            xAxis: {
                type: 'category',
                data: tags
            },
            series: [
                {
                    type: 'bar',
                    data: summaries,
                    barWidth: '45%',
                    itemStyle: {
                        color: function (e) {
                            let index = e.dataIndex % COLOR_PAN.length
                            return COLOR_PAN[index]
                        },
                    },
                    label: {
                        show: true,
                        position: 'top',
                        color: '#000',
                        fontSize: 12,
                        formatter: function (v) {
                            let percentage = 0
                            if (total !== 0) {
                                percentage = Math.round(v.data / total * 10000) / 100
                            }
                            return v.data + ', ' + percentage + '%'
                        }
                    }
                }
            ]
        });
    })
}
