String.prototype.render = function (context) {
    var tokenReg = /(\\)?\{([^\{\}\\]+)(\\)?\}/g;

    return this.replace(tokenReg, function (word, slash1, token, slash2) {
        if (slash1 || slash2) {
            return word.replace('\\', '');
        }

        var variables = token.replace(/\s/g, '').split('.');
        var currentObject = context;
        var i, length, variable;

        for (i = 0, length = variables.length; i < length; ++i) {
            variable = variables[i];
            currentObject = currentObject[variable];
            if (currentObject === undefined || currentObject === null) return '';
        }
        return currentObject;
    });
};

var re = /x/;
console.log(re);
re.toString = function() {
    showMessage('哈哈，你打开了控制台，是想要看看我的秘密吗？', 5000, true);
    return '';
};

$(document).on('copy', function (){
    showMessage('你都复制了些什么呀，转载要记得加上出处哦', 5000, true);
});

$('#hitokoto').mouseover(function (){
    var text = '这句一言出处是 <span style="color:#0099cc;">『{source}』</span>，是 <span style="color:#0099cc;">FGHRSH</span> 在 {date} 收藏的！';
    var hitokoto = JSON.parse($(this)[0].dataset.raw);
    text = text.render({source: hitokoto.source, author: hitokoto.author, date: hitokoto.date});
    showMessage(text, 3000);
});

$('.waifu-tool .fui-home').click(function (){
    //window.location = 'https://www.fghrsh.net/';
    window.location = window.location.protocol+'//'+window.location.hostname+'/'
});

$('.waifu-tool .fui-eye').click(function (){
    loadOtherModel();
});

$('.waifu-tool .fui-chat').click(function (){
    showHitokoto();
});

$('.waifu-tool .fui-user').click(function (){
    loadRandModel();
    showMessage('我的新衣服好看嘛', 3000, true);
});

$('.waifu-tool .fui-info-circle').click(function (){
    window.open('https://www.cnblogs.com/yjlblog/p/8724881.html');
});

$('.waifu-tool .fui-cross').click(function (){
    sessionStorage.setItem('waifu-dsiplay', 'none');
    showMessage('愿你有一天能与重要的人重逢', 1300, true);
    window.setTimeout(function() {$('.waifu').hide();}, 1300);
});

$('.waifu-tool .fui-photo').click(function (){
    showMessage('照好了嘛，是不是很可爱呢？', 5000, true);
    window.Live2D.captureName = 'Live2D.png';
    window.Live2D.captureFrame = true;
});

function waifuWelcome(){
    var text;
    var SiteIndexUrl = 'https://www.fghrsh.net/';  // 手动指定主页
    //var SiteIndexUrl = window.location.protocol+'//'+window.location.hostname+'/';  // 自动获取主页

    if (window.location.href == SiteIndexUrl) {      // 如果是主页
        var now = (new Date()).getHours();
        if (now > 23 || now <= 5) {
            text = '你是夜猫子呀？这么晚还不睡觉，明天起的来嘛';
        } else if (now > 5 && now <= 7) {
            text = '早上好！一日之计在于晨，美好的一天就要开始了';
        } else if (now > 7 && now <= 11) {
            text = '上午好！工作顺利嘛，不要久坐，多起来走动走动哦！';
        } else if (now > 11 && now <= 14) {
            text = '中午了，工作了一个上午，现在是午餐时间！';
        } else if (now > 14 && now <= 17) {
            text = '午后很容易犯困呢，今天的运动目标完成了吗？';
        } else if (now > 17 && now <= 19) {
            text = '傍晚了！窗外夕阳的景色很美丽呢，最美不过夕阳红~';
        } else if (now > 19 && now <= 21) {
            text = '晚上好，今天过得怎么样？';
        } else if (now > 21 && now <= 23) {
            text = '已经这么晚了呀，早点休息吧，晚安~';
        } else {
            text = '嗨~ 快来逗我玩吧！';
        }
    } else {
        if(document.referrer !== ''){
            var referrer = document.createElement('a');
            referrer.href = document.referrer;
            var domain = referrer.hostname.split('.')[1];
            if (window.location.hostname == referrer.hostname) {
                text = '欢迎来到<span style="color:#0099cc;">『' + document.title.split(' - ')[0] + '』</span>';
            } else if (domain == 'baidu') {
                text = 'Hello! 来自 百度搜索 的朋友<br>你是搜索 <span style="color:#0099cc;">' + referrer.search.split('&wd=')[1].split('&')[0] + '</span> 找到的我吗？';
            } else if (domain == 'so') {
                text = 'Hello! 来自 360搜索 的朋友<br>你是搜索 <span style="color:#0099cc;">' + referrer.search.split('&q=')[1].split('&')[0] + '</span> 找到的我吗？';
            } else if (domain == 'google') {
                text = 'Hello! 来自 谷歌搜索 的朋友<br>欢迎阅读<span style="color:#0099cc;">『' + document.title.split(' - ')[0] + '』</span>';
            } else {
                text = 'Hello! 来自 <span style="color:#0099cc;">' + referrer.hostname + '</span> 的朋友';
            }
        } else {
            text = '欢迎阅读<span style="color:#0099cc;">『' + document.title.split(' - ')[0] + '』</span>';
        }
    }
    showMessage(text, 6000);
}

//window.hitokotoTimer = window.setInterval(showHitokoto,30000);
/* 检测用户活动状态，并在空闲时 定时显示一言 */
var getActed = false;
window.hitokotoTimer = 0;
var hitokotoInterval = false;

$(document).mousemove(function(e){getActed = true;}).keydown(function(){getActed = true;});
setInterval(function() { if (!getActed) ifActed(); else elseActed(); }, 1000);

function ifActed(){
    if (!hitokotoInterval) {
        hitokotoInterval = true;
        hitokotoTimer = window.setInterval(showHitokoto, 30000);
    }
}

function elseActed(){
    getActed = hitokotoInterval = false;
    window.clearInterval(hitokotoTimer);
}

function showHitokoto(){
    $.getJSON('//api.fghrsh.net/hitokoto/rand/?encode=jsc&uid=3335',function(result){
        var text = '这句一言出处是 <span style="color:#0099cc;">『{source}』</span>，是 <span style="color:#0099cc;">FGHRSH</span> 在 {date} 收藏的！';
        text = text.render({source: result.source, date: result.date});
        showMessage(result.hitokoto, 5000);
        window.setTimeout(function() {showMessage(text, 3000);}, 5000);
    });
}

function showMessage(text, timeout, flag){
    if(flag || sessionStorage.getItem('waifu-text') === '' || sessionStorage.getItem('waifu-text') === null){
        if(Array.isArray(text)) text = text[Math.floor(Math.random() * text.length + 1)-1];
        //console.log(text);

        if(flag) sessionStorage.setItem('waifu-text', text);

        $('.waifu-tips').stop();
        $('.waifu-tips').html(text).fadeTo(200, 1);
        if (timeout === undefined) timeout = 5000;
        hideMessage(timeout);
    }
}

function hideMessage(timeout){
    $('.waifu-tips').stop().css('opacity',1);
    if (timeout === undefined) timeout = 5000;
    window.setTimeout(function() {sessionStorage.removeItem('waifu-text')}, timeout);
    $('.waifu-tips').delay(timeout).fadeTo(200, 0);
}

var waifuJson = {
    "mouseover": [
        {
            "selector": ".content-wrap a[href^='http']",
            "text": ["要看看 <span style=\"color:#0099cc;\">{text}</span> 么？"]
        },
        {
            "selector": ".logo",
            "text": ["点击前往首页，想回到上一页可以使用浏览器的后退功能哦"]
        },
        {
            "selector": ".social-share",
            "text": ["觉得文章有帮助的话，可以分享给更多需要的朋友呢"]
        },
        {
            "selector": "#tor_show",
            "text": ["翻页比较麻烦吗，点击可以显示这篇文章的目录呢"]
        },
        {
            "selector": ".menu-follow",
            "text": ["利用 feed 订阅器，就能快速知道博客有没有更新了呢"]
        },
        {
            "selector": ".creativecommons",
            "text": ["转载前要先注意下文章的 版权协议 呢"]
        },
        {
            "selector": ".comm",
            "text": ["想要去评论些什么吗？"]
        },
        {
            "selector": "#qrcode",
            "text": ["手机扫一下就能继续看，很方便呢"]
        },
        {
            "selector": "#postcomments",
            "text": ["要吐槽些什么呢"]
        },
        {
            "selector": ".rollto",
            "text": ["回到开始的地方吧"]
        },
        {
            "selector": "#inpName",
            "text": ["该怎么称呼你呢"]
        },
        {
            "selector": "#inpEmail",
            "text": ["留下你的邮箱，不然就是无头像人士了"]
        },
        {
            "selector": "#inpHomePage",
            "text": ["你的家在哪里呢，好让我去参观参观"]
        },
        {
            "selector": "#txaArticle",
            "text": ["认真填写哦，垃圾评论是禁止事项"]
        },
        {
            "selector": ".OwO-logo",
            "text": ["要插入一个表情吗"]
        },
        {
            "selector": "#submit",
            "text": ["要提交了吗，评论可能需要审核，请耐心等待~"]
        },
        {
            "selector": ".ImageBox",
            "text": ["点击图片可以放大呢"]
        },
        {
            "selector": "input[name=q]",
            "text": ["找不到想看的内容？搜索看看吧"]
        },
        {
            "selector": ".paging",
            "text": ["去下一页看看吧"]
        },
        {
            "selector": "#htmer_time",
            "text": ["已经过了这么久了呀，日子过得好快呢", "<span style=\"color:#0099cc;\">2010</span> 是多久来着…<br>1，2，3……有好多好多天呢！"]
        },
        {
            "selector": ".updatelog",
            "text": ["这里记录着我搬家的历史呢"]
        },
        {
            "selector": ".aboutme",
            "text": ["发现主人出没地点！", "这里有主人的联系方式"]
        },
        {
            "selector": ".donate-qrcode",
            "text": ["主人最近在吃土呢，很辛苦的样子。给他一些钱钱吧~"]
        },
        {
            "selector": "#wenkmPlayer div.switch-player",
            "text": ["想要听点音乐吗"]
        },
        {
            "selector": "#wenkmPlayer div.volume",
            "text": ["在这里可以调整<span style=\"color:#0099cc;\">音量</span>呢"]
        },
        {
            "selector": "#wenkmPlayer div.switch-playlist",
            "text": ["<span style=\"color:#0099cc;\">播放列表</span>里都有什么呢"]
        },
        {
            "selector": "#wenkmPlayer div.switch-ksclrc",
            "text": ["有<span style=\"color:#0099cc;\">歌词</span>的话就能跟着一起唱呢"]
        },
        {
            "selector": "#navbar-page-116",
            "text": ["要去大佬们的家看看吗？"]
        },
        {
            "selector": "#navbar-page-34",
            "text": ["有什么想说的吗？"]
        },
        {
            "selector": "navbar-page-97",
            "text": ["这里都是主人的黑历史呢"]
        },
        {
            "selector": "#navbar-page-31",
            "text": ["这里有一些关于我家主人的秘密哦，要不要看看呢"]
        },
        {
            "selector": ".waifu-tool .fui-home",
            "text": ["回首页看看吧"]
        },
        {
            "selector": ".waifu-tool .fui-eye",
            "text": ["要切换看板娘吗？"]
        },
        {
            "selector": ".waifu-tool .fui-chat",
            "text": ["猜猜我要说些什么"]
        },
        {
            "selector": ".waifu-tool .fui-user",
            "text": ["喜欢换装PLAY吗？"]
        },
        {
            "selector": ".waifu-tool .fui-info-circle",
            "text": ["想要知道更多有关我的事吗？"]
        },
        {
            "selector": ".waifu-tool .fui-cross",
            "text": ["到了要说再见的时候了吗"]
        },
        {
            "selector": ".waifu-tool .fui-photo",
            "text": ["你要给我拍照呀，一二三~茄子~~"]
        },
        {
            "selector": ".waifu #live2d",
            "text": ["干嘛呢你，快把手拿开", "鼠…鼠标放错地方了！"]
        }
    ],
    "click": [
        {
            "selector": ".waifu #live2d",
            "text": ["是…是不小心碰到了吧", "萝莉控是什么呀", "你看到我的小熊了吗", "再摸的话我可要报警了！⌇●﹏●⌇", "110吗，这里有个变态一直在摸我(ó﹏ò｡)"]
        }
    ],
    "seasons": [
        {
            "date": "01/01",
            "text": "<span style=\"color:#0099cc;\">元旦</span>了呢，新的一年又开始了，今年是{year}年~"
        },
        {
            "date": "02/14",
            "text": "又是一年<span style=\"color:#0099cc;\">情人节</span>，{year}年找到对象了嘛~"
        },
        {
            "date": "03/08",
            "text": "今天是<span style=\"color:#0099cc;\">妇女节</span>！"
        },
        {
            "date": "03/12",
            "text": "今天是<span style=\"color:#0099cc;\">植树节</span>，要保护环境呀"
        },
        {
            "date": "04/01",
            "text": "悄悄告诉你一个秘密~<span style=\"background-color:#34495e;\">今天是愚人节，不要被骗了哦~</span>"
        },
        {
            "date": "05/01",
            "text": "今天是<span style=\"color:#0099cc;\">五一劳动节</span>，计划好假期去哪里了吗~"
        },
        {
            "date": "06/01",
            "text": "<span style=\"color:#0099cc;\">儿童节</span>了呢，快活的时光总是短暂，要是永远长不大该多好啊…"
        },
        {
            "date": "09/03",
            "text": "<span style=\"color:#0099cc;\">中国人民抗日战争胜利纪念日</span>，铭记历史、缅怀先烈、珍爱和平、开创未来。"
        },
        {
            "date": "09/10",
            "text": "<span style=\"color:#0099cc;\">教师节</span>，在学校要给老师问声好呀~"
        },
        {
            "date": "10/01",
            "text": "<span style=\"color:#0099cc;\">国庆节</span>，新中国已经成立69年了呢"
        },
        {
            "date": "11/05-11/12",
            "text": "今年的<span style=\"color:#0099cc;\">双十一</span>是和谁一起过的呢~"
        },
        {
            "date": "12/20-12/31",
            "text": "这几天是<span style=\"color:#0099cc;\">圣诞节</span>，主人肯定又去剁手买买买了~"
        }
    ]
};

function initModel(){
    waifuWelcome();

    var modelId = localStorage.getItem('modelId');
    var modelTexturesId = localStorage.getItem('modelTexturesId');

    if (modelId == null) {

        /* 首次访问加载 指定模型 的 指定材质 */

        var modelId = 2;            // 模型 ID
        var modelTexturesId = 49;    // 材质 ID

    } loadModel(modelId, modelTexturesId);


    $.each(waifuJson.mouseover, function (index, tips){
        $(document).on("mouseover", tips.selector, function (){
            var text = tips.text;
            if(Array.isArray(tips.text)) text = tips.text[Math.floor(Math.random() * tips.text.length + 1)-1];
            text = text.render({text: $(this).text()});
            showMessage(text, 3000);
        });
    });
    $.each(waifuJson.click, function (index, tips){
        $(document).on("click", tips.selector, function (){
            var text = tips.text;
            if(Array.isArray(tips.text)) text = tips.text[Math.floor(Math.random() * tips.text.length + 1)-1];
            text = text.render({text: $(this).text()});
            showMessage(text, 3000, true);
        });
    });
    $.each(waifuJson.seasons, function (index, tips){
        var now = new Date();
        var after = tips.date.split('-')[0];
        var before = tips.date.split('-')[1] || after;

        if((after.split('/')[0] <= now.getMonth()+1 && now.getMonth()+1 <= before.split('/')[0]) &&
            (after.split('/')[1] <= now.getDate() && now.getDate() <= before.split('/')[1])){
            var text = tips.text;
            if(Array.isArray(tips.text)) text = tips.text[Math.floor(Math.random() * tips.text.length + 1)-1];
            text = text.render({year: now.getFullYear()});
            showMessage(text, 6000, true);
        }
    });
}

function loadModel(modelId, modelTexturesId){
    localStorage.setItem('modelId', modelId);
    if (modelTexturesId === undefined) modelTexturesId = 0;
    localStorage.setItem('modelTexturesId', modelTexturesId);
    loadlive2d('live2d', '//api.fghrsh.net/live2d/get/?id='+modelId+'-'+modelTexturesId, console.log('live2d','模型 '+modelId+'-'+modelTexturesId+' 加载完成'));
}

function loadRandModel(){
    var modelId = localStorage.getItem('modelId');
    var modelTexturesId = localStorage.getItem('modelTexturesId');

    var modelTexturesRandMode = 'rand';     // 可选 'rand'(随机), 'switch'(递增)

    $.ajax({
        cache: false,
        url: '//api.fghrsh.net/live2d/'+modelTexturesRandMode+'_textures/?id='+modelId+'-'+modelTexturesId,
        dataType: "json",
        success: function (result){
            loadModel(modelId, result.textures['id']);
        }
    });
}

function loadOtherModel(){
    var modelId = localStorage.getItem('modelId');

    var modelTexturesRandMode = 'switch';     // 可选 'rand'(随机), 'switch'(递增)

    $.ajax({
        cache: false,
        url: '//api.fghrsh.net/live2d/'+modelTexturesRandMode+'/?id='+modelId,
        dataType: "json",
        success: function (result){
            loadModel(result.model['id']);
            showMessage(result.model['message'], 3000, true);
        }
    });
}
