var app = angular.module('myApp', []);
app.filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});
app.controller('menu', function ($scope, $http) {

    $scope.search = function (herenewsSource, herenewstype) {

        $("#newlist").hide()
        if (herenewsSource != '') {
            $scope.newsSource = herenewsSource
        }
        if (herenewstype != '') {
            $scope.newsType = herenewstype
        }

        $("#wait").show()
        $.ajax({
            url: '/search',
            type: 'get',
            dataType: 'json',
            cache: false,
            data: {
                q: $scope.q,
                newsSource: $scope.newsSource,
                newsType: $scope.newsType,
                page: $scope.page,
                pagesize: $scope.pagesize
            }
            , contentType: "application/x-www-form-urlencoded; charset=UTF-8"

        })
            .done(function (data) {
                $("#wait").hide()
                $("#newlist").show()
                $("#tishi").show()
                $("#cebianlan").show()
                console.log(data)
                $scope.usetime = data.usetime
                $scope.total = data.total
                $scope.newsList = data.newsList
                $scope.newsSourceAggregation = data.newsSourceAggregation
                $scope.newsTypeAggregation = data.newsTypeAggregation
                $scope.page = data.page
                $scope.pagesize = data.pagesize
                $scope.$apply();
                //调用分页
                layui.use(['laypage', 'layer'], function () {
                    var laypage = layui.laypage
                    var layer = layui.layer
                    laypage({
                        cont: 'fenye',
                        curr: data.page,
                        skin: '#dd4b39',
                        pages: Math.ceil(data.total / data.pagesize),
                        skip: true,
                        // curr: location.hash.replace('#!page=', ''),     //获取hash值为fenye的当前页
                        // hash: 'page' ,//自定义hash值
                        jump: function (obj, first) {
                            // console.log(obj)
                            if (!first) {

                                $scope.page = obj.curr
                                $scope.search()

                            }
                        }
                    });
                });

            })
            .fail(function () {
                console.log("查询新闻错误error");
            });
    }

})


