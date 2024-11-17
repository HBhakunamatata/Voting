let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
let mychart = null
let colorPan = ['#4587E7','#35AB33','#F5AD1D','#ff7f50','#da70d6','#32cd32','#6495ed']

$(document).ajaxSend(function(e, xhr) {
    xhr.setRequestHeader(header, token);
});

$(document).ready(function () {

    mychart = echarts.init(document.getElementById('vote-chart'))

    let option = {
        title: {
            text: 'The percentage for vote items',
        },
        tooltip: {},
        legend: {},
        xAxis: { type: 'category' },
        yAxis: {},
        series: [
            {
                type: 'bar',
                itemStyle: {
                    color: function (e) {
                        let index = e.dataIndex % colorPan.length
                        return colorPan[index]
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

    mychart.setOption(option);

    window.addEventListener('resize', function () {
        mychart.resize()
    })

    let endDate = Date.parse($('#countTime time').attr('datetime'))
    setInterval(countDownTime, 1000, endDate)

    getAndShowVoteResult()
    refillChartData()
})


function countDownTime(endDate) {
    let nowDate = new Date()
    if (endDate - nowDate <= 0) {
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


function getAndShowVoteResult() {
    let index = location.pathname.lastIndexOf("/") + 1
    let voteId = location.pathname.substring(index)
    $.getJSON('/vote/' + voteId + '/result', function (data) {
        // console.log(data)
        if (Array.isArray(data) && data.length === 0) {

        } else {
            // 关闭多选和提交按钮  修改选中状态
            let itemIdList = data.map(item => item.itemId)
            // console.log(itemIdList)
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

    console.log(JSON.stringify(postData))

    $.ajax({
        type: "PATCH",
        url: '/vote/result',
        data: JSON.stringify(postData),
        contentType: 'application/json',
        success: function(data) {
            console.log(data)
            getAndShowVoteResult()
            refillChartData()
        }
    })
})


function refillChartData() {
    let voteId = $('#voteSubject').attr('data')

    $.get('/vote/' + voteId + '/summary', function (data) {

        let total = 0
        let items = []
        let summaries = []

        for (const i of data) {
            items.push(i['tag'])
            summaries.push(i['summary'])
            total += i['summary']
        }

        mychart.setOption({
            xAxis: {
                type: 'category',
                data: items
            },
            series: [
                {
                    type: 'bar',
                    data: summaries,
                    barWidth: '45%',
                    itemStyle: {
                        color: function (e) {
                            let index = e.dataIndex % colorPan.length
                            return colorPan[index]
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
