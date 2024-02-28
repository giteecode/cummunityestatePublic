$(function () {
    layui.use(['upload', 'element', 'layer'], function () {
        let $ = layui.jquery
            , upload = layui.upload
            , element = layui.element
            , layer = layui.layer;

        /**
         * 公告栏上一页按钮
         */
        $("#prev").click(function () {
            let currPage = $("input[name='currPage']").val();
            if (currPage - 1 < 1) {
                layer.msg("已经是第一页啦！");
                return;
            }
            let liTotal = $(".pagination li:eq(" + parseInt(parseInt(currPage) - 1) + ")");
            liTotal.addClass("active");
            liTotal.next().removeClass("active");
            $("input[name='currPage']").val(parseInt(parseInt(currPage) - 1));
            // 查找上一页信息
            $.getJSON(
                "notice/getDataByPage?page=" + parseInt(parseInt(currPage) - 1) + "&limit=4",
                function (res) {
                    if (res.code == 0) {
                        handleData(res);
                    }
                }
            )
        })

        /**
         * 公告栏下一页按钮
         */
        $("#next").click(function () {
            let currPage = $("input[name='currPage']").val();
            let totalPage = $("input[name='totalPage']").val();
            if (currPage + 1 >= totalPage) {
                layer.msg("已经是最后一页啦！");
                return;
            }
            let liTotal = $(".pagination li:eq(" + parseInt(parseInt(currPage) + 1) + ")");
            liTotal.addClass("active");
            liTotal.prev().removeClass("active");
            $("input[name='currPage']").val(parseInt(parseInt(currPage) + 1));
            // 查找下一页信息
            $.getJSON(
                "notice/getDataByPage?page=" + parseInt(parseInt(currPage) + 1) + "&limit=4",
                function (res) {
                    if (res.code == 0) {
                        handleData(res);
                    }
                }
            )
        })

        /**
         * 为页码绑定单机事件
         */
        $(".pageNum").click(function () {
            var than = $(this);
            // 查找信息
            var pageNum = than.find("a").text();
            $.getJSON(
                "notice/getDataByPage?page=" + pageNum + "&limit=4",
                function (res) {
                    if (res.code == 0) {
                        than.addClass("active");
                        than.siblings().removeClass("active");
                        handleData(res);
                    }
                }
            )
        })

        /**
         * 公告查看详情单击事件
         * @param that
         */
        function noticeBtnClick(that) {
            var noticeId = that.attr("data-id");
            var userId = $("#userId").val();
            if (!userId) {
                userId = "";
            }
            layer.open({
                type: 2,
                title: false,
                scrollbar: false,
                shadeClose: true,
                closeBtn: true,
                shade: 0.5,
                area: ['500px', '600px'],
                content: "notice/toFindPage?id=" + noticeId + "&userId=" + userId,
            });
        }

        /**
         * 处理分页查询的数据
         * @param res
         */
        function handleData(res) {
            if (res.code == 0) {
                // 重新动态渲染公告
                // 获取到查询数据
                let selectResult = res.data;
                // 获取到公告栏容器
                let parent = $("#accordion");
                parent.empty();
                for (let i = 0; i < selectResult.length; i++) {
                    let subElem = $("<div class=\"panel panel-default\"></div>");
                    let panelHead = $("<div class=\"panel-heading\"></div>");
                    let panelTitle = $("<h4 class=\"panel-title\"></h4>");
                    let a = $("<a data-toggle=\"collapse\" data-parent=\"#accordion\" style='font-size: 15px' href='#title" + selectResult[i].id + "'>" + selectResult[i].title + "&emsp;-&emsp;</a>");
                    let dateSpan = $("<span style=\"font-size: 14px\">" + selectResult[i].createDate + "</span>")
                    a.append(dateSpan);
                    panelTitle.append(a);
                    panelHead.append(panelTitle);
                    subElem.append(panelHead);
                    var title = $("<div id='title" + selectResult[i].id + "' class='panel-collapse collapse'></div>");
                    if (i == 0) {
                        title.addClass("in");
                    }
                    var panelBody = $("<div class=\"panel-body\" style=\"padding: 8px 12px;\"></div>");
                    var textSpan = $("<span class=\"text_span\">" + selectResult[i].content + "</span>");
                    var div = $("<div style=\"margin-top: 2px\"></div>");
                    var button = $("<button type=\"button\" class=\"btn btn-primary noticeInfoBtn\" data-id='" + selectResult[i].id + "'>查看详细</button>")
                    button.onclick = function (e) {
                        alert(1)
                    }
                    div.append(button);
                    panelBody.append(textSpan);
                    panelBody.append(div);
                    title.append(panelBody);
                    subElem.append(title);
                    parent.append(subElem);
                }
            }
        }

        /**
         * 登录按钮
         */
        $("#toLogin").click(function () {
            // 点击登录弹出登录窗
            layer.open({
                type: 2,
                title: false,
                scrollbar: false,
                shadeClose: true,
                closeBtn: false,
                shade: 0.5,
                area: ['800px', '500px'],
                content: "webPage/loginPage",
            });
        })

        /**
         * 点击查看公告详情按钮
         */
        $("#accordion").on("click", ".noticeInfoBtn", function () {
            noticeBtnClick($(this))
        })

        /**
         * 购买房屋
         */
        $("#buyHouse").click(function () {
            var userId = $("#userId").val();
            var userName = $("#userName").val();
            // 验证登录
            if (verifyLogin() == -1) {
                return;
            }
            // 已登录, 弹出房屋信息弹窗
            layer.open({
                type: 2,
                title: false,
                scrollbar: false,
                shadeClose: true,
                closeBtn: false,
                shade: 0.5,
                area: ['1200px', '650px'],
                content: `/housing/MyPropertyChargeVisit/toPropertyChargeVisitPage?userId=${userId}&clientName=${userName}`
            });
        })

        /**
         * 购买车位
         */
        $("#buyCarPark").click(function () {
            var userId = $("#userId").val();
            var userName = $("#userName").val();
            // 验证登录
            if (verifyLogin() == -1) {
                return;
            }
            // 已登录, 弹出车位信息弹窗
            layer.open({
                type: 2,
                title: false,
                scrollbar: false,
                shadeClose: true,
                closeBtn: false,
                shade: 0.5,
                area: ['1100px', '650px'],
                content: `carPark/toWebCarParkPage?userId=${userId}`
            });
        })

        /**
         * 点击新增公告按钮
         */
        $("#createNotice").click(function () {
            var userId = $("#userId").val();
            // 验证登录
            if (verifyLogin() == -1) {
                return;
            }
            var userName = $("#userName").val();
            layer.open({
                type: 2,
                title: false,
                scrollbar: false,
                shadeClose: true,
                closeBtn: true,
                shade: 0.5,
                area: ['400px', '510px'],
                content: "notice/toNoticeAddWindow?userId=" + userId + "&userName=" + userName
            });
        })

        /**
         * 点击发布评论按钮
         */
        $("#createComment").click(function () {
            var userId = $("input[name='userId']").val();
            var noticeId = $("input[name='noticeId']").val();
            // 验证登录
            if (verifyLogin() == -1) {
                return;
            }
            layer.open({
                type: 2,
                title: '发表评论',
                scrollbar: false,
                shadeClose: true,
                resize: false,
                shade: 0.3,
                area: ['400px', '300px'],
                content: "../comment/toAddPage?userId=" + userId + "&noticeId=" + noticeId
            });
        })

        /**
         * 查看登记信息
         */
        $("#toAccessPage").click(function () {
            var userId = $("#userId").val();
            // 验证登录
            if (verifyLogin() == -1) {
                return;
            }
            // 点击登录弹出登录窗
            layer.open({
                type: 2,
                title: "登记信息",
                shadeClose: true,
                shade: 0.5,
                area: ['1200px', '700px'],
                content: "webPage/toAccessPage?userId=" + userId,
            });
        })

        /**
         * 查看我的评价
         */
        $("#meComment").click(function () {
            var userId = $("#userId").val();
            // 验证登录
            if (verifyLogin() == -1) {
                return;
            }
            // 点击登录弹出登录窗
            layer.open({
                type: 2,
                title: "我的评价",
                shadeClose: true,
                shade: 0.5,
                area: ['1000px', '485px'],
                content: "comment/toMeComment?userId=" + userId,
            });
        })

        /**
         * 查看我的公告
         */
        $("#meNotice").click(function () {
            var userId = $("#userId").val();
            // 验证登录
            if (verifyLogin() == -1) {
                return;
            }
            // 点击登录弹出登录窗
            layer.open({
                type: 2,
                title: "我的公告",
                shadeClose: true,
                shade: 0.5,
                area: ['1000px', '600px'],
                content: "notice/toMeNotice?userId=" + userId,
                end: function () {
                    window.location.reload();
                }
            });
        })

        /**
         * 查看投诉信息
         */
        $("#toComplaint").click(function () {
            var userId = $("#userId").val();
            // 验证登录
            if (verifyLogin() == -1) {
                return;
            }
            // 点击登录弹出登录窗
            layer.open({
                type: 2,
                title: "投诉信息",
                shadeClose: true,
                shade: 0.5,
                area: ['1200px', '600px'],
                content: "webPage/toComplaintPage?userId=" + userId,
            });
        })

        /**
         * 查看报修信息
         */
        $("#toRepair").click(function () {
            var userId = $("#userId").val();
            // 验证登录
            if (verifyLogin() == -1) {
                return;
            }
            // 点击登录弹出登录窗
            layer.open({
                type: 2,
                title: "报修信息",
                shadeClose: true,
                shade: 0.5,
                area: ['1200px', '600px'],
                content: "webPage/toRepairPage?userId=" + userId,
            });
        })

        /**
         * 退出按钮
         */
        $("#webOut").click(function () {
            window.location.href = getPath() + "/webPage/webLoginOut";
        })

        /**
         * 修改手机号按钮
         */
        $("#editPhone").click(function () {
            var id = $("#userId").val();
            toWindow("user/toUserEditWindow?id=" + id, "修改住户信息", "400px", "600px");
        })

        /**
         * 健康码上传
         */
        var uploadInst = upload.render({
            elem: '#test1'
            , url: 'api/file/upload/image'  // 图片上传接口
            , before: function (obj) {
                //预读本地文件示例，不支持ie8
                obj.preview(function (index, file, result) {
                    $('#demo1').attr('src', result); //图片链接（base64）
                });

                layer.msg('上传中', {icon: 16});
            }
            , done: function (res) {
                //如果上传失败
                if (res.code > 0) {
                    return layer.msg('上传失败');
                }
                //上传成功的一些操作
                layer.msg('上传完毕', {icon: 1})
                $('#healthCodePath').val(res.msg);
                $('#demoText').html('')
            }
            , error: function () {
                //演示失败状态，并实现重传
                $('#demoText').html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            }
        });

        /**
         * 行程卡上传
         */
        var uploadInst2 = upload.render({
            elem: '#test2'
            , url: 'api/file/upload/image'  // 图片上传接口
            , before: function (obj) {
                //预读本地文件示例，不支持ie8
                obj.preview(function (index, file, result) {
                    $('#demo2').attr('src', result); //图片链接（base64）
                });

                layer.msg('上传中', {icon: 16});
            }
            , done: function (res) {
                //如果上传失败
                if (res.code > 0) {
                    return layer.msg('上传失败');
                }
                //上传成功的一些操作
                layer.msg('上传完毕', {icon: 1})
                $('#travelCodePath').val(res.msg);
                $('#demoText2').html('')
            }
            , error: function () {
                //演示失败状态，并实现重传
                $('#demoText2').html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            }
        });

        /**
         * 登记按钮
         */
        $("#accessBtn").click(function () {
            var userId = $("#userId").val();
            // 验证登录
            if (verifyLogin() == -1) {
                return;
            }
            // 判断是否为空
            var userName = $("input[name='accessUserName']").val()
            var phone = $("input[name='accessPhone']").val()
            var type = $("select[name='type']").val()
            var type1 = $("select[name='type1']").val()
            var type2 = $("select[name='type2']").val()
            var desc = $("textarea[name='desc']").val()
            var healthCodePath = $("input[name='healthCodePath']").val()
            var travelCodePath = $("input[name='travelCodePath']").val()
            if (!healthCodePath || !travelCodePath) {
                layer.confirm("请务必上传健康码和行程码", {
                    btn: ['确定'] //按钮
                    , icon: 5
                    , anim: 6
                    , title: '错误信息'
                }, function (index) {
                    layer.close(index)
                })
                return;
            }
            // 新增信息
            var jsonObj = {
                "userId": userId,
                "userName": userName,
                "phone": phone,
                "type": type,
                "type1": type1,
                "type2": type2,
                "desc": desc,
                "healthCodePath": healthCodePath,
                "travelCodePath": travelCodePath,
            }
            console.log(jsonObj)
            $.ajax({
                url: "accessVisit/insertInfo",
                type: 'POST',
                async: false,
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify(jsonObj),
                success: function (res) {
                    if (res.code == "0") {
                        layer.confirm(res.msg, {
                            btn: ['确定']  //按钮
                            , icon: 6
                        }, function () {
                            window.parent.location.reload();    //刷新父页面
                        });
                    } else {
                        layer.confirm(res.msg, {
                            btn: ['确定']  //按钮
                            , icon: 5
                            , anim: 6
                        }, function (index) {
                            layer.close(index);
                        });
                    }
                },
                error: function (res) {
                    layer.confirm('啊哦！访问出问题了！快找开发狗算账！', {
                        btn: ['确定']  //按钮
                        , icon: 5
                        , anim: 6
                    }, function (index) {
                        layer.close(index);
                    });
                }
            })
        })

        /**
         * 报修按钮
         */
        $("#repairBtn").click(function () {
            var userId = $("#userId").val();
            // 验证登录
            if (verifyLogin() == -1) {
                return;
            }
            // 判断是否为空
            var userName = $("input[name='repairUserName']").val()
            var phone = $("input[name='repairPhone']").val()
            var repairInfo = $("textarea[name='repairInfo']").val()
            if (!userName || !phone || !repairInfo) {
                layer.confirm("请填写完整报修信息！", {
                    btn: ['确定'] //按钮
                    , icon: 5
                    , anim: 6
                    , title: '错误信息'
                }, function (index) {
                    layer.close(index)
                })
                return;
            }
            // 新增报修信息
            var jsonObj = {
                "userId": userId,
                "userName": userName,
                "phone": phone,
                "repairInfo": repairInfo
            }
            console.log(jsonObj)
            $.ajax({
                url: "userRepair/insertInfo",
                type: 'POST',
                async: false,
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify(jsonObj),
                success: function (res) {
                    if (res.code == "0") {
                        layer.confirm(res.msg, {
                            btn: ['确定']  //按钮
                            , icon: 6
                        }, function () {
                            window.parent.location.reload();    //刷新父页面
                        });
                    } else {
                        layer.confirm(res.msg, {
                            btn: ['确定']  //按钮
                            , icon: 5
                            , anim: 6
                        }, function (index) {
                            layer.close(index);
                        });
                    }
                },
                error: function (res) {
                    layer.confirm('啊哦！访问出问题了！快找开发狗算账！', {
                        btn: ['确定']  //按钮
                        , icon: 5
                        , anim: 6
                    }, function (index) {
                        layer.close(index);
                    });
                }
            })
        })

        /**
         * 投诉按钮
         */
        $("#complaintBtn").click(function () {
            var userId = $("#userId").val();
            // 验证登录
            if (verifyLogin() == -1) {
                return;
            }
            var userName = $("input[name='complaintUserName']").val();
            var phone = $("input[name='complaintPhone']").val();
            var textInfo = $("textarea[name='complaintInfo']").val();
            if (!userName || !phone || !textInfo) {
                layer.confirm("请填写完整投诉信息！", {
                    btn: ['确定'] //按钮
                    , icon: 5
                    , anim: 6
                    , title: '错误信息'
                }, function (index) {
                    layer.close(index)
                })
                return;
            }
            // 新增投诉信息
            var jsonObj = {
                "userId": userId,
                "userName": userName,
                "phone": phone,
                "complaintInfo": textInfo
            }
            $.ajax({
                url: "userComplaint/insertInfo",
                type: 'POST',
                async: false,
                contentType: 'application/json;charset=utf-8', //设置请求头信息
                data: JSON.stringify(jsonObj),
                success: function (res) {
                    if (res.code == "0") {
                        layer.confirm(res.msg, {
                            btn: ['确定']  //按钮
                            , icon: 6
                        }, function () {
                            window.parent.location.reload();    //刷新父页面
                        });
                    } else {
                        layer.confirm(res.msg, {
                            btn: ['确定']  //按钮
                            , icon: 5
                            , anim: 6
                        }, function (index) {
                            layer.close(index);
                        });
                    }
                },
                error: function (res) {
                    layer.confirm('啊哦！访问出问题了！快找开发狗算账！', {
                        btn: ['确定']  //按钮
                        , icon: 5
                        , anim: 6
                    }, function (index) {
                        layer.close(index);
                    });
                }
            })
        })

        function getPath() {
            //获取项目路径
            var curRequestPath = window.document.location.href;
            //获取项目请求路径
            var pathName = window.document.location.pathname;
            var ipAndPort = curRequestPath.indexOf(pathName);
            var localhostPath = curRequestPath.substring(0, ipAndPort);
            var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
            basePath = localhostPath + projectName;
            return basePath;
        }

        /**
         * 判断是否登录
         */
        function verifyLogin() {
            var userId = $("#userId").val();
            if (!userId) {
                layer.confirm("您还未登录！", {
                    btn: ['确定'] //按钮
                    , icon: 5
                    , anim: 6
                    , title: '错误信息'
                }, function (index) {
                    layer.close(index)
                })
                return -1;
            }
        }
    })
})