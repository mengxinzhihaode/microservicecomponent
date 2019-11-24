var express = require('express');
var zookeeper = require('node-zookeeper-client');
var httpProxy = require('http-proxy');
var CONNECTION_STRING = '172.20.10.10:2181';
var REGISTRY_ROOT = '/registry';
//连接zookeeper
var zk = zookeeper.createClient(CONNECTION_STRING);
zk.connect();

var proxy = httpProxy.createProxyServer();
proxy.on('error', function (err, req, res) {
    res.end();
});

var app = express();
app.use(express.static('public'));

app.all('*', function (req, res) {
    //处理图标请求
    if(req.path == '/favicon.ico') {
        res.end();
        return;
    }

    //获取服务名称
    var serviceName = req.get('Service-Name');
    console.log('serviceName: %s', serviceName);
    if(!serviceName) {
        console.log('Service-Name request header is not exist');
        res.end();
        return;
    }
    
    //获取服务路径
    var servicePath = REGISTRY_ROOT + "/" + serviceName;
    console.log('servicePath: %s', servicePath);
    //获取服务路径下的地址节点
    zk.getChildren(servicePath, function (error, addressNodes) {
        if(error) {
            console.log(error.stack);
            res.end();
            return;
        }
        var size = addressNodes.length;
        if(size == 0){
            console.log('address node is not exist');
            res.end();
            return;
        }
        //生成地址路径
        var addressPath = servicePath + '/';
        if(size == 1) {
            //若只有一个地址，则获取该地址
            addressPath += addressNodes[0];
        } else {
            //若存在多个地址，随机获取一个地址
            addressPath += addressNodes[parseInt(Math.random() * size)];
        }
        console.log('addressPath: %s', addressPath);
        zk.exists(addressPath, function (event) {
            if(event.NODE_DELETED) {
                console.log('node deleted...');
            }
        },function (error, stat) {
            if(stat) {
                //获取服务地址
                zk.getData(addressPath, function (error, serviceAddress) {
                    if(error) {
                        console.log(error.stack);
                        res.end();
                        return;
                    }
                    console.log('serviceAddress:%s', serviceAddress);
                    if(!serviceAddress) {
                        console.log('service address is not exist');
                        res.end();
                        return;
                    }
                    //执行反代理
                    proxy.web(req, res, {
                        target: 'http://' + serviceAddress //目标地址
                    });
                });
            }
        });


    })
});

module.exports = app;
