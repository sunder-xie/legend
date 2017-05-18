module.exports = function(grunt) {
    var transport = require('grunt-cmd-transport');
    var style = transport.style.init(grunt);
    var text = transport.text.init(grunt);
    var script = transport.script.init(grunt);
    var pkg = grunt.file.readJSON("package.json");
    //压缩后文件头部信息
    var banner = '/*-- <%= pkg.description %> <%= grunt.template.today("yyyy-mm-dd hh:MM:ss") %> --*/\n';
    var pathStr = {
        //开发环境静态资源路径
        src_script: "script/",
        //线上环境静态资源路径
        out_script: "online/script/",
        //产出图片目录
        out_img:"online/style/img/"
    }
    //文件系统对象
    var fs = require('fs');
    //系统路径对象
    var path = require('path');
    
    grunt.initConfig({
        pkg: pkg,
        clean: ['./online'],
        //提取 script 下的 cmd require的依赖模块
        transport: {
            options: {
                paths: ['.'],
                parsers: {
                    '.js': [script.jsParser],
                    '.css': [style.css2jsParser],
                    '.html': [text.html2jsParser]
                }
            },
            app: {
                options:{
                    idleading: "legend/resources/online/script/"
                },
                files:[{
                    cwd: pathStr.src_script + "libs/",
                    src: '**/*.js',
                    filter: 'isFile',
                    dest: pathStr.out_script + "libs/"
                },{
                    cwd: pathStr.src_script + "modules/",
                    src: '**/*.js',
                    filter: 'isFile',
                    dest: pathStr.out_script + "modules/"
                }]
            }
        },
        //合并js模块
        concat: {
            options:{
                // paths: ["."],
                include: "relative"
            },
            app:{
                files: [{
                    expand: true,
                    cwd: pathStr.out_script + "modules/",
                    src: '**/*.js',
                    dest: pathStr.out_script + "modules/"
                }]
            }
        },
        //压缩js
        uglify: {
            options: {
                banner: banner,
                mangle: {
                    except: ['require','module','exports']
                }
            },
            all: {
                files:[{
                    expand: true,
                    cwd: pathStr.out_script,
                    src: ['**/*.js','!**/*-debug.js'],
                    dest: pathStr.out_script
                }]
            },
            libs: {
                files:[{
                    expand: true,
                    cwd: pathStr.out_script + "libs/",
                    src: ['**/*.js'],
                    dest: pathStr.out_script + "libs/"
                }]
            },
            modules: {
                files:[{
                    expand: true,
                    cwd: pathStr.out_script + "modules/",
                    src: ['**/*.js','!**/*-debug.js'],
                    dest: pathStr.out_script + "modules/"
                }]
            },
            page: {
                files:[{
                    expand: true,
                    cwd: pathStr.src_script + "page/",
                    src: ['**/*.js'],
                    dest: pathStr.out_script + "page/"
                }]
            }
        },
        //压缩css
        cssmin: {
            //文件头部输出信息
            options: {
                banner: banner,
                compatibility : 'ie7', //设置兼容模式 
                beautify: {
                    //中文ascii化，防止中文乱码的神配置
                    ascii_only: true
                }
            },
            all: {
                files: [{
                    expand: true,
                    cwd: 'style/',
                    src: ['**/*.css'],
                    dest: 'online/style/',
                    ext: '.css'
                }]
            },
            base: {
                files: [{
                    expand: true,
                    cwd: 'style/',
                    src: ['base.css'],
                    dest: 'online/style/',
                    ext: '.css'
                }]
            }
        },
        copy:{
            all:{
                files:[{
                    expand: true,
                    cwd: 'style/img/',
                    src: ['**/*.jpg','**/*.gif','**/*.png','**/*.jpeg'],
                    dest: 'img/'
                },{
                    expand: true,
                    cwd: 'style/img/',
                    src: ['**/*.jpg','**/*.gif','**/*.png','**/*.jpeg'],
                    dest: 'online/img/'
                },{
                    expand: true,
                    cwd: pathStr.src_script + 'libs/',
                    src: ['**/*.*'],
                    dest: pathStr.out_script + 'libs/',
                },{
                    expand: true,
                    cwd: pathStr.src_script + 'page/',
                    src: '**/*.*',
                    dest: pathStr.out_script + 'page/',
                },{
                    expand: true,
                    cwd: 'style/page/',
                    src: '**/*.*',
                    dest: 'online/style/page/',
                }]
            },
            img: {
                files:[{
                    expand: true,
                    cwd: 'style/img/',
                    src: ['**/*.jpg','**/*.gif','**/*.png','**/*.jpeg'],
                    dest: 'img/'
                },{
                    expand: true,
                    cwd: 'style/img/',
                    src: ['**/*.jpg','**/*.gif','**/*.png','**/*.jpeg'],
                    dest: 'online/img/'
                }]
            },
            jsLibs: {
                files:[{
                    expand: true,
                    cwd: pathStr.src_script + 'libs/',
                    src: ['**/*.*'],
                    dest: pathStr.out_script + 'libs/',
                }]
            },
            jsModules: {
                files:[{
                    expand: true,
                    cwd: pathStr.src_script + 'modules/',
                    src: ['**/*.*'],
                    dest: pathStr.out_script + 'modules/',
                }]
            },
            jsPage: {
                files:[{
                    expand: true,
                    cwd: pathStr.src_script + 'page/',
                    src: ['**/*.*'],
                    dest: pathStr.out_script + 'page/',
                }]
            },
            js: {
                files:[{
                    expand: true,
                    cwd: pathStr.src_script + 'libs/',
                    src: ['**/*.*'],
                    dest: pathStr.out_script + 'libs/',
                },{
                    expand: true,
                    cwd: pathStr.src_script + 'page/',
                    src: '**/*.*',
                    dest: pathStr.out_script + 'page/',
                }]
            }
        },
        imagemin: {
            /* 压缩优化图片大小 */
            app: {
                options: {
                    optimizationLevel: 3
                },
                files: [
                    {
                        expand: true,
                        cwd: 'style/img/',
                        src: ['**/*.{png,jpg,jpeg,gif}'], // 优化 img 目录下所有 png/jpg/jpeg 图片
                        dest: 'img/' // 优化后的图片保存位置，默认覆盖
                    },
                    {
                        expand: true,
                        cwd: 'style/img/',
                        src: ['**/*.{png,jpg,jpeg,gif}'], // 优化 img 目录下所有 png/jpg/jpeg 图片
                        dest: pathStr.out_img // 优化后的图片保存位置，默认覆盖
                    },
                ]
            }
        },
        //监测文件改变
        watch: {
            js: {
                files: [ pathStr.src_script+"libs/**/*.js", pathStr.src_script+"modules/**/*.js"],
                tasks: ['legend-not-page-js']
            },
            css: {
                files: ['style/modules/*.css'],
                tasks: ['legend-css-base']
            }
        }
    });

    
    //清理文件夹
    grunt.loadNpmTasks('grunt-contrib-clean');
    //提取js模块依赖
    grunt.loadNpmTasks('grunt-cmd-transport');
    //合并js模块
    grunt.loadNpmTasks('grunt-cmd-concat');
    //压缩js模块
    grunt.loadNpmTasks('grunt-contrib-uglify');
    //压缩css样式表
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    //复制未压缩的静态资源和图片到产出目录下(上线环境)
    grunt.loadNpmTasks('grunt-contrib-copy');
    //优化图片
    grunt.loadNpmTasks('grunt-contrib-imagemin');
    //监听文件改变
    grunt.loadNpmTasks('grunt-contrib-watch');

    //全局任务
    grunt.registerTask('legend',
        [
            'clean',
            'copy:all',
            'transport',
            // 'concat',
            'uglify:all',
            'cssmin:all'
            // 'imagemin'
        ]
    );
    //不压缩page下的js 和 css
    grunt.registerTask('legend-not-page',
        [
            'clean',
            'copy:all',
            'transport',
            // 'concat',
            'uglify:libs',
            'uglify:modules',
            'cssmin:base',
            'imagemin'
        ]
    );
    //只压缩libs和modules下的js
    grunt.registerTask('legend-not-page-js',
        [
            'copy:js',
            'transport',
            'uglify:libs',
            'uglify:modules'
        ]
    );
    //局部任务
    //压缩所有css文件
    grunt.registerTask("legend-css-all",["copy:img","cssmin:all"]);

    //压缩base。css文件
    grunt.registerTask("legend-css-base",["cssmin:base"]);

    //复制所有不压缩的静态资源
    grunt.registerTask("legend-copy-all",["copy:all"]);

    //只复制图片
    grunt.registerTask("legend-copy-img",["copy:img"]);

    //复制并压缩所有js文件
    grunt.registerTask("legend-js-all",["copy:js","transport","uglify:all"]);

    //复制并压缩libs下js文件
    grunt.registerTask("legend-js-libs",["copy:jsLibs","uglify:libs"]);

    //复制并压缩moduels下js文件
    grunt.registerTask("legend-js-modules",["copy:jsModules","uglify:modules"]);

    //复制并压缩page下js文件
    grunt.registerTask("legend-js-page",["copy:jsPage","uglify:page"]);


    //更改path.config.js文件里的base路径到线上产出环境。
    grunt.registerTask("legend-path","更改path.config.js文件到线上目录",function(){
        this.async();
        var readFileName = path.join(pathStr.src_script+"libs/path.online.config.js");
        var writeFileName = path.join(pathStr.out_script+"libs/path.config.js");

        fs.readFile(readFileName, function (err, data) {
            if(err) throw err;
            //写入文件
            fs.writeFile(writeFileName,data,"utf8",function (err) {
                if (err) throw err;
                grunt.log.oklns('path.config.js文件路径已更改成线上模块路径'); 
            });
        });
    });

    //node里的工具类
    var util = require('util');
    //加密算法模块
    var crypto = require('crypto');
    //计算文件的md5摘要32位值
    var md5 = function(data) {
        var Buffer = require("buffer").Buffer;
        var buf = new Buffer(data);
        var str = buf.toString("binary");
        return crypto.createHash("md5").update(str).digest("hex");
    }
    //获取控制台颜色插件
    var colors = require("colors");

    
    //根据上一个任务生成的mapping.json检索xml或者ftl文件中的链接
    //更新静态文件后加md5戳
    grunt.registerTask("chen",function(){
        var pagePath = "./../WEB-INF";
        // 静态资源文件映射集合
        var mapping = {};
        //页面文件总数量
        var count = 0;
        //静态文件改变的列表
        var changeFileList = [];
        //引用静态文件的数量
        var relCount = 0;
        
        var getStaticsFile = function(fPath){
            //获取文件夹内文件信息
            var fileNames = fs.readdirSync(fPath);
            for(var i=0;i<fileNames.length;i++){
                var currentPath = fPath + '/' + fileNames[i];
                var stat = fs.statSync(currentPath);

                if(
                    fileNames[i] == "node_modules"
                ){
                    //node模块和online文件夹不参与md5映射。
                    continue;
                }

                if(stat.isDirectory()){
                    //是目录的话,递归查找
                    getStaticsFile(currentPath);
                }else{
                    //不是目录的话,只匹配js和css文件
                    if(/(\.js|\.css)$/.test(currentPath)){
                        var buffer = fs.readFileSync(currentPath);
                        mapping[fPath.substring(fPath.lastIndexOf("/resources/"))+"/"+fileNames[i]] = md5(buffer);
                    }
                }
            }
        }
        //写入文件
        var writeFile = function(mapping){
            var data = (function(){
                var temp = "{ \n";
                var i = 0;
                for(var index in mapping){
                    temp += "\t\"" + index + "\" : \"" + mapping[index]+"\", \n";
                    i++;
                }
                console.log(("静态文件总数量: "+i).green);
                temp = temp.substring(0,temp.lastIndexOf(",")) + "\n";
                temp += "}";
                return temp;
            })();
            try{
                fs.writeFileSync("./mapping.json",data,'utf-8');
                console.log("静态文件路径映射成功..".green); 
            }catch(error){
                console.error("写入静态文件路径映射失败".red);
            }
        }

        if(!fs.existsSync(__dirname+"/mapping.json")){
            //读文件夹
            getStaticsFile(__dirname);
            writeFile(mapping);
        }else{
            // console.log("mapping.json已经存在".green);
            mapping = grunt.file.readJSON("mapping.json");
        }
        
        
        

        var getChangeFile = function(fPath){
            //获取文件夹内文件信息
            var fileNames = fs.readdirSync(fPath);
            for(var i=0;i<fileNames.length;i++){
                var currentPath = fPath + '/' + fileNames[i];
                var stat = fs.statSync(currentPath);

                if(
                    fileNames[i] == "node_modules" || 
                    // fileNames[i] == "online" ||
                    fileNames[i] == "Gruntfile.js"
                ){
                    //node模块和online文件夹不参与md5映射。
                    continue;
                }

                if(stat.isDirectory()){
                    //是目录的话,递归查找
                    getChangeFile(currentPath);
                }else{
                    //不是目录的话,只匹配js和css文件
                    if(/(\.js|\.css)$/.test(currentPath)){
                        var buffer = fs.readFileSync(currentPath);
                        var mappingPath = currentPath.substring(currentPath.lastIndexOf("/resources/"));
                        var existMD5 = mapping[mappingPath];
                        
                        if( existMD5 != md5(buffer) ){
                            //console.log(("改变前md5摘要:"+existMD5).cyan+" ---- "+("改变后md5摘要:"+md5(buffer)).yellow);
                            if(existMD5 == undefined){
                                console.log(("\t"+mappingPath+" ---- add").green);
                            }else{
                                console.log(("\t"+mappingPath+" ---- update").red);
                            }
                            mapping[mappingPath] = md5(buffer);
                            changeFileList.push(mappingPath);
                        }
                    }
                }
            }
        }
        getChangeFile(__dirname);
        writeFile(mapping);

        var getPageFile = function(fPath){
            //获取文件夹内文件信息
            var fileNames = fs.readdirSync(fPath);
            
            for(var i=0;i < fileNames.length;i++){
                var currentPath = fPath + '/' + fileNames[i];
                var stat = fs.statSync(currentPath);

                if(stat.isDirectory()){
                    //是目录的话,递归查找
                    getPageFile(currentPath);
                }else{
                    //不是目录的话,只匹配ftl和xml文件
                    if(/(\.ftl|\.xml)$/.test(currentPath)){
                        var flag = false;
                        for(var j=0;j<changeFileList.length;j++){
                            //var reg = eval("/"+changeFileList[j].replace(/\//g,"\\/").replace(/\./g,"\\.")+"[.]+[\"]$/g");
                            var buffer = fs.readFileSync(currentPath);
                            var bufferStr = buffer.toString();
                            var startPoint = bufferStr.indexOf(changeFileList[j]);


                            if(startPoint != -1){
                                if(!flag){
                                    console.log("页面文件路径："+currentPath);
                                }
                                //ftl或xml文件引用了当前的改变的静态资源
                                var endPoint = bufferStr.indexOf("\"",startPoint);
                                var subStr = bufferStr.substring(startPoint,endPoint);
                                bufferStr = bufferStr.replace(subStr,changeFileList[j]+"?"+mapping[changeFileList[j]]);
                                fs.writeFileSync(currentPath,bufferStr,'utf-8');
                                console.log(("\t原文件引用资源路径："+subStr).gray);
                                console.log(("\t更新后引用资源路径："+changeFileList[j]+"?"+mapping[changeFileList[j]]).green);
                                //console.log("页面文件路径："+currentPath+"\n静态资源文件："+changeFileList[j]);
                                //console.log(position+","+currentPath);
                                //console.log(bufferStr.substring(position,position+index.length));
                                flag = true;
                            }
                        }
                        if(flag){
                           relCount++; 
                       }
                        buffer = bufferStr = null;
                        count++;
                    }
                }
            }
        }
        getPageFile(pagePath);

        console.log(("-------------------------------").magenta);
        console.log(("静态资源改变数量: "+changeFileList.length).green);
        console.log(("ftl或xml文件总数: "+count).green);
        console.log(("更新静态md5影响到的页面数量: "+relCount).yellow);
        console.log(("-------------------------------").magenta)
    });
};
