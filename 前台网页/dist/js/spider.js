
$(document).ready(function() {
    drawHistory()
    drawnewsType();
    drawnewsSource() 
});
function drawHistory(){

     /*
     * Flot Interactive Chart
     * -----------------------
     */
    // We use an inline data source in the example, usually data would
    // be fetched from a server
    var data = [], totalPoints = 1000;

    function getRandomData() {

      if (data.length > 0)
        data = data.slice(1);

      // Do a random walk
      while (data.length < totalPoints) {

        var prev = data.length > 0 ? data[data.length - 1] : 500,
            y = prev + Math.random() * 100 - 50;

        if (y < 0) {
          y = 0;
        } else if (y > 1000) {
          y = 1000;
        }

        data.push(y);
      }

      // Zip the generated y values with the x values
      var res = [];
      for (var i = 0; i < data.length; ++i) {
        res.push([i, data[i]]);
      }

      return res;
    }

    var interactive_plot = $.plot("#interactive", [getRandomData()], {
      grid: {
        borderColor: "#f3f3f3",
        borderWidth: 1,
        tickColor: "#cccccc"
      },
      series: {
        shadowSize: 0, // Drawing is faster without shadows
        color: "#9933cc"
      },
      lines: {
        fill: true, //Converts the line chart to area chart
        color: "#00ffff"
      },
      yaxis: {
        min: 0,
        max: 1000,
        show: true
      },
      xaxis: {
        show: false
      }
    });

    var updateInterval = 100; //Fetch data ever x milliseconds
    var realtime = "on"; //If == to on then fetch data every x seconds. else stop fetching
    function update() {

      interactive_plot.setData([getRandomData()]);

      // Since the axes don't change, we don't need to call plot.setupGrid()
      interactive_plot.draw();
      if (realtime === "on")
        setTimeout(update, updateInterval);
    }

    //INITIALIZE REALTIME DATA FETCHING
    if (realtime === "on") {
      update();
    }
    //REALTIME TOGGLE
    $("#realtime .btn").click(function () {
      if ($(this).data("toggle") === "on") {
        realtime = "on";
      }
      else {
        realtime = "off";
      }
      update();
    });
    /*
     * END INTERACTIVE CHART
     */


}

function drawnewsType() {
        var dom = document.getElementById("newsType");
        var myChart = echarts.init(dom);


        var dataAxis = ['汽车','体育','娱乐','军事','国际','财经','房产','科技','视频','健康','时尚','教育','旅游','文化','台湾','社会','时政','公益','理财','读书','家居','观点','游戏','彩票','女人','资讯','国内','领导','艺术','美食','法治','港澳','母婴','理论','金融','女性','酒业','智能','书画','地方','人事','佛教','香港','历史','手机','中国','留学','食品','数码','舆情','信息化','能源','旅行','华人','讲堂','争鸣','酒香','产经','音乐','评论','国学','保险','商业','智库','大陆','图片','曝光','泛珠三角','科普','深圳','青年','城市','人物','跑步','世相','生活']


        var data = [38813, 32473, 26053, 25082, 21923, 20663, 18696, 16703, 16356, 14385, 13922, 12130, 10747, 7326, 6948, 6532, 6325, 5455, 5264, 5208, 5170, 4786, 4580, 4259, 3852, 3671, 3468, 3287, 2582, 2413, 2408, 2352, 2320, 2247, 2235, 2176, 2127, 2083, 1853, 1799, 1736, 1728, 1558, 1552, 1507, 1465, 1455, 1427, 1424, 1410, 1351, 1325, 1322, 1255, 1239, 1213, 1206, 1153, 1145, 1080, 994, 854, 785, 711, 624, 599, 572, 568, 502, 426, 390, 214, 114, 58, 21, 20]

;
 
        option = {
            title: {
                text: '新闻种类',
                subtext: '采集大量各类新闻',
                left: 'center',
                z:9
            },
            tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
            xAxis: {
                data: dataAxis,
                axisLabel: {
                    textStyle: {
                        color: 'black'
                        ,fontWeight :'bold'
                    }
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                z: 10
            },
            yAxis: {
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    textStyle: {
                        color: '#999'
                        ,fontWeight :'bold'
                    }
                }
            },
            dataZoom: [
            {
                type: 'inside'
            }
            ],
            series: [
                {
                    type: 'bar',
                    name:'爬取数量',
                    itemStyle: {
                        normal: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                {offset: 0, color: '#ff0000'},
                                {offset: 0.5, color: '#ff9900'},
                                {offset: 1, color: '#00ffff'}
                                ]
                                )
                        },
                        emphasis: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                {offset: 0, color: '#2378f7'},
                                {offset: 0.7, color: '#2378f7'},
                                {offset: 1, color: '#83bff6'}
                                ]
                                )
                        }
                    },
                    data: data
                }
        ]
    };
    myChart.setOption(option);
    // Enable data zoom when user click bar.
    var zoomSize = 6;
    myChart.on('click', function (params) {
        console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
        myChart.dispatchAction({
            type: 'dataZoom',
            startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
            endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, data.length - 1)]
        });
    });
}

function drawnewsSource() {
        var dom = document.getElementById("newsSource");
        var myChart = echarts.init(dom);


        var dataAxis = ['凤凰网','人民网','搜狐','网易新闻','环球网','新华网','大公报','中新网','新浪新闻','星岛环球','央广网']
        var data = [75304, 70403, 52993, 46111, 43876, 33046, 27120, 22603, 13021, 10869, 10329]


 
        option = {
            title: {
                text: '新闻来源',
                subtext: '涉猎各大主流网站',
                left: 'center',
                z:9
            },
            tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
            xAxis: {
                data: dataAxis,
                axisLabel: {
                    textStyle: {
                        color: 'black'
                        ,fontWeight :'bold'
                    }
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                z: 10
            },
            yAxis: {
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    textStyle: {
                        color: '#999'
                        ,fontWeight :'bold'
                    }
                }
            },
            dataZoom: [
            {
                type: 'inside'
            }
            ],
            series: [
                {
                    type: 'bar',
                    name:'爬取数量',
                    itemStyle: {
                        normal: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                {offset: 0, color: '#ff00cc'},
                                {offset: 0.5, color: '#ff66cc'},
                                {offset: 1, color: '#ffffcc'}
                                ]
                                )
                        },
                        emphasis: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                {offset: 0, color: '#2378f7'},
                                {offset: 0.7, color: '#2378f7'},
                                {offset: 1, color: '#83bff6'}
                                ]
                                )
                        }
                    },
                    data: data
                }
        ]
    };
    myChart.setOption(option);
    // Enable data zoom when user click bar.
    var zoomSize = 6;
    myChart.on('click', function (params) {
        console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
        myChart.dispatchAction({
            type: 'dataZoom',
            startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
            endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, data.length - 1)]
        });
    });

}