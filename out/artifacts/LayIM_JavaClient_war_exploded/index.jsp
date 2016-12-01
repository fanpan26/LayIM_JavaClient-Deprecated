
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title>LayIM_JavaClient by Pz11111</title>
<link rel="stylesheet" href="js/layui/css/layui.css"/>
 <script type="text/javascript" src="js/layui/layui.js"></script>
  <style>
    html{background-color: #D9D9D9;}
  </style>
</head>
<body>

<div style="margin: 300px auto; text-align: center; font-size: 20px;">
  <a href="http://layim.layui.com/demo.html">前去看官方演示</a>
</div>

<script>

    var socket = null;
    var im = {
        getUid:function () {
          var uid = '${param.uid}';
            return uid;
        },
        init:function(){
            if ('WebSocket' in window){
                var uid = im.getUid();
                if(!uid){
                    console.log('当前用户未登陆，应该跳到login');
                }else {
                    var socketUrl = 'ws://localhost:8080/layim/websocket/'+ uid;
                    socket = new WebSocket(socketUrl);
                    im.startListener();
                }
            } else {
                alert('当前浏览器不支持WebSocket功能，请更换浏览器访问。');
            }
        },
        startListener:function () {
            if (socket) {
                // 连接发生错误的回调方法
                socket.onerror = function () {
                    console.log("连接失败!");
                };
                // 连接成功建立的回调方法
                socket.onopen = function (event) {
                    console.log("连接成功");
                }
                // 接收到消息的回调方法
                socket.onmessage = function (event) {
                    console.log("接收到消息");
                    im.handleMessage(event.data);
                }
                // 连接关闭的回调方法
                socket.onclose = function () {
                    console.log("关闭连接！!");
                }
            }
        },
        handleMessage:function (msg) {
            var msg = JSON.parse(event.data);
            console.log(msg);
            switch (msg.type){
                case 'TYPE_TEXT_MESSAGE':
                    layim.getMessage(msg.msg);
                    break;
                default:
                    break;
            }
        }
    };

    im.init();


  layui.use('layim', function(layim){

    window.layim = layim;
    var autoReplay = [
      '您好，我现在有事不在，一会再和您联系。',
      '你没发错吧？face[微笑] ',
      '洗澡中，请勿打扰，偷窥请购票，个体四十，团体八折，订票电话：一般人我不告诉他！face[哈哈] ',
      '你好，我是主人的美女秘书，有什么事就跟我说吧，等他回来我会转告他的。face[心] face[心] face[心] ',
      'face[威武] face[威武] face[威武] face[威武] ',
      '<（@￣︶￣@）>',
      '你要和我说话？你真的要和我说话？你确定自己想说吗？你一定非说不可吗？那你说吧，这是自动回复。',
      'face[黑线]  你慢慢说，别急……',
      '(*^__^*) face[嘻嘻] ，是贤心吗？'
    ];

    //基础配置
    layim.config({

      //初始化接口
      init: {
          url: '/layim/api?action=base'
          , data: {id: im.getUid()}
      }

      //简约模式（不显示主面板）
      //,brief: true

      //查看群员接口
      ,members: {
        url: '/layim/api?action=member'
        ,data: {}
      }

      ,uploadImage: {
        url: '/layim/upload?t=img' //（返回的数据格式见下文）
      }

      ,uploadFile: {
        url: '/layim/upload?t=file' //（返回的数据格式见下文）
      }

      //,skin: ['aaa.jpg'] //新增皮肤
      //,isfriend: false //是否开启好友
      //,isgroup: false //是否开启群组
      //,min: true //是否始终最小化主面板（默认false）
      ,chatLog: './demo/chatlog.html' //聊天记录地址
      ,find: './demo/find.html'
      //,copyright: true //是否授权
    });

    /*
     layim.chat({
     name: '在线客服-小苍'
     ,type: 'kefu'
     ,avatar: 'http://tva3.sinaimg.cn/crop.0.0.180.180.180/7f5f6861jw1e8qgp5bmzyj2050050aa8.jpg'
     ,id: -1
     });
     layim.chat({
     name: '在线客服-心心'
     ,type: 'kefu'
     ,avatar: 'http://tva1.sinaimg.cn/crop.219.144.555.555.180/0068iARejw8esk724mra6j30rs0rstap.jpg'
     ,id: -2
     });
     */
    //layim.setChatMin();

    //监听发送消息
    layim.on('sendMessage', function(data){

        console.log(data);
        var msg = JSON.stringify(data);
        socket.send(msg);
      //演示自动回复
        /*
      setTimeout(function(){
        var obj = {};
        if(To.type === 'group'){
          obj = {
            username: '模拟群员'+(Math.random()*100|0)
            ,avatar: layui.cache.dir + 'images/face/'+ (Math.random()*72|0) + '.gif'
            ,id: To.id
            ,type: To.type
            ,content: autoReplay[Math.random()*9|0]
          }
        } else {
          obj = {
            username: To.name
            ,avatar: To.avatar
            ,id: To.id
            ,type: To.type
            ,content: autoReplay[Math.random()*9|0]
            ,timestamp: new Date().getTime()
          }
        }
        layim.getMessage(obj);
      }, 1000);
      */
    });

    //监听在线状态的切换事件
    layim.on('online', function(data){
      console.log(data);
    });



    //监听收到的聊天消息
    /*
     socket.on('chatMessage', function (res) {
     layim.getMessage({
     username: res.name
     ,avatar: res.avatar
     ,id: res.id
     ,type: res.type
     ,content: res.content
     });
     });
     */

    //layim建立就绪
    layim.on('ready', function(res){
/*
      //添加好友（如果检测到该socket）
      layim.addList({
        type: 'group'
        ,avatar: "http://tva3.sinaimg.cn/crop.64.106.361.361.50/7181dbb3jw8evfbtem8edj20ci0dpq3a.jpg"
        ,groupname: 'Angular开发'
        ,id: "12333333"
        ,members: 0
      });
      layim.addList({
        type: 'friend'
        ,avatar: "http://tp2.sinaimg.cn/2386568184/180/40050524279/0"
        ,username: '冲田杏梨'
        ,groupid: 2
        ,id: "1233333312121212"
        ,remark: "本人冲田杏梨将结束AV女优的工作"
      });
      */

      //接受消息（如果检测到该socket）
      /*setTimeout(function(){
        //不在好友列表，则为临时会话
        layim.getMessage({
          username: "Hi"
          ,avatar: "http://tva1.sinaimg.cn/crop.7.0.736.736.50/bd986d61jw8f5x8bqtp00j20ku0kgabx.jpg"
          ,id: "198909151014"
          ,type: "friend"
          ,content: "临时："+ new Date().getTime()
        });

        //在好友列表
        layim.getMessage({
          username: "贤心"
          ,avatar: "http://tp1.sinaimg.cn/1571889140/180/40030060651/1"
          ,id: "100001"
          ,type: "friend"
          ,content: "嗨，你好！欢迎体验LayIM。演示标记："+ new Date().getTime()
          ,timestamp: new Date().getTime()
        });
      }, 1000);
      */
    });

    //监听查看群员
    layim.on('members', function(data){
      console.log(data);
    });

    //监听聊天窗口的切换
    layim.on('chatChange', function(data){
      console.log(data);
    });



  });

</script>
</body>
</html>

