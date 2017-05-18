var gulp = require('gulp');
//- 插件按需加载
var $ = require('gulp-load-plugins')();
//- 共享stream
var lazypipe = require('lazypipe');
//- 添加浏览器前缀 == stats.json 为浏览器兼容配置
var autoprefixer = require('autoprefixer');

// 系统自带模块
var process = require('process');


var cleanCSSLazyPipe = lazypipe()
    .pipe($.cleanCss, {debug: true}, function (details) {
        console.log(OLD_OUTPUT_PATH.css + '/' + details.name);
        console.log(details.name + '\'s original size:' + (details.stats.originalSize / 1024).toFixed(2) + 'KB');
        console.log(details.name + '\'s minify size:' + (details.stats.minifiedSize / 1024).toFixed(2) + 'KB');
        console.log(details.name + '\'s efficiency:' + Math.round(details.stats.efficiency * 100) + '%');
    });


//- TEST TASK START
////////////////////////////////////////////////////////////////////

// gulp.task('testAutoFx', function () {
//     gulp.src('test/**/*.css')
//         .pipe($.postcss([
//             autoprefixer({
//                 // 全球使用率大于5%（数据来源CanIUse, caniuse.com）
//                 browsers: ['> 5%'],
//                 cascade: false,
//                 add: true,
//                 remove: true
//                 // stats: './stats.json'
//             })
//         ]))
//         .pipe(gulp.dest('test'));
// });
//
// gulp.task('testCSSComb', function () {
//     gulp.src('test/test-combo.css')
//         .pipe($.csscomb())
//         .pipe(gulp.dest('test'));
// });

////////////////////////////////////////////////////////////////////
//- END

//- 看板 START
//////////////////////////////////////////////////////////////////////////

var KB_INPUT_PATH = {
    'baseCSS': 'kb/css/common/base.css',
    'baseSCSS': 'kb/scss/base.scss',
    'scss': 'kb/scss/module/**/*.scss',
    'css': 'kb/css/module/**/*.css'
};

var KB_OUTPUT_PATH = {
    'scss': 'kb/css/module',
    'css': 'kb/css/common'
};

/**
 * sass编译
 */
gulp.task('kb-sass-base', function () {
    return gulp.src(KB_INPUT_PATH.baseSCSS)
        .pipe($.sass({outputStyle: 'expanded'}).on('error', $.sass.logError))
        .pipe(gulp.dest(KB_OUTPUT_PATH.css));
});

/**
 * 基础css压缩
 */
gulp.task('kb-clean-css-base', ['kb-sass-base'], function () {
    return gulp.src(KB_INPUT_PATH.baseCSS)
        .pipe(cleanCSSLazyPipe())
        .pipe($.rename({extname: '.min.css'}))
        .pipe(gulp.dest(KB_OUTPUT_PATH.css));
});

/**
 * watch 任务
 */
gulp.task('kb-sass:watch', function () {
    gulp.watch(KB_INPUT_PATH.scss, ['kb-sass-base']);
});

/**
 * 看板框架任务集
 * 任务名 kb-css-base 更名为 kbFramework
 */
gulp.task('kbFramework', ['kb-clean-css-base']);

/////////////////////////////////////////////////////////////////////////
//- END

//- 老版本云修gulp工具 START
////////////////////////////////////////////////////////////////////////

// 源文件目录
var OLD_INPUT_PATH = {
    'moduleCSS': 'css/module/**/*.css',
    'css': 'css/**/*.css',
    'js': 'js/**/*.js',
    'img': 'img/*.{png,jpg,gif,ico}',
    'layerjs': 'third-plugin/layer/layer-2.4.js'
};
// 输出文件目录
var OLD_OUTPUT_PATH = {
    'commonCSS': 'css/common',
    'css': 'css',
    'js': 'dist/js',
    'img': 'dist/img',
    'layerjs': 'third-plugin/layer'
};


/**
 * 压缩layer.js文件
 */
gulp.task('layer-uglify', function () {
    return gulp.src('third-plugin/layer/layer-2.4.js')
        .pipe($.uglify(OLD_INPUT_PATH.layerjs))
        .pipe($.concat('layer.min.js'))
        .pipe(gulp.dest(OLD_OUTPUT_PATH.layerjs));
});

/**
 * CSS前缀补全
 * 任务名 cssAutoFx 更名为 css-auto-prefix
 */
gulp.task('css-auto-prefix', function () {
    return gulp.src(OLD_INPUT_PATH.css)
        .pipe($.postcss([
            autoprefixer({
                browsers: ['Chrome > 20'],
                cascade: false,
                add: true,
                remove: true
            })
        ]))
        .pipe(gulp.dest(OLD_OUTPUT_PATH.css))
});

/**
 * CSS代码压缩
 * 任务名 clean-css 更名为 old-clean-css
 */
gulp.task("old-clean-css", function () {
    return gulp.src([
            'css/module/reset.css',
            'css/module/common.css',
            OLD_INPUT_PATH.moduleCSS,
            '!css/module/chosen.css'
        ])
        .pipe($.concat('base.min.css'))
        .pipe(cleanCSSLazyPipe())
        .pipe(gulp.dest(OLD_OUTPUT_PATH.commonCSS));
});

gulp.task('oldCSS', ['css-auto-prefix', 'old-clean-css']);

gulp.task('oldFramework', ['layer-uglify', 'oldCSS']);

////////////////////////////////////////////////////////////////////////
//- END


//- 新版本云修gulp工具 START
///////////////////////////////////////////////////////////////////////

/**
 * sass文件编译
 * create by sky 2016 11 25
 */
gulp.task('scss-compile', function () {
    return gulp.src([
        'css/**/*.scss', 
        '!css/common/var.scss',
        '!css/page/home/tqmall-join/**/*.scss',
        '!css/scss/**/*.scss',
        '!css/scss-widgets/**/*.scss'
    ])
        .pipe($.sourcemaps.init())
        .pipe($.sass({
            outputStyle: 'compressed'
        }).on('error', $.sass.logError))
        .pipe($.sourcemaps.write())
        .pipe(gulp.dest('css'));
});

/**
 * JavaScript代码检测工具
 * create by sky 2016 10 30
 */
gulp.task('js-lint', function () {
    return gulp.src(['js/**/*.js', '!js/common/bxslider.js'])
        .pipe($.eslint({
            useEslintrc: true,
            configFile: '.eslintrc.yml'
        }))
        .pipe($.eslint.result(function (result) {
            if (!(result.messages.length + result.warningCount + result.errorCount)) {
                return;
            }
            console.log('\n==============================================================================================================\n');
            console.log('ESLint result: ' + result.filePath);
            console.log('# Messages: ' + result.messages.length);
            console.log('# Warnings: ' + result.warningCount);
            console.log('# Errors: ' + result.errorCount);
        }))
        .pipe($.eslint.formatEach());
});

/**
 * CSS代码整理工具
 * create by sky 2016 10 30
 */
gulp.task('css-comb', function () {
    return gulp.src(['css/**/*.css', '!css/**/*.min.css'])
        .pipe($.csscomb())
        .pipe(gulp.dest('css'));
});

/**
 * 前端代码检查工具
 * create by sky 2016 10 30
 */
gulp.task('lint', ['js-lint']);

/**
 * 监听sass编译
 * create by sky 2016 11 25
 */
gulp.task('watch:scss-compile', ['scss-compile'], function () {
    gulp.watch('css/**/*.scss', ['scss-compile']);
});

gulp.task('newFramework', ['scss-compile'/*, 'lint', 'css-comb'*/]);

///////////////////////////////////////////////////////////////////////
//- END


//- 公共任务处理 START
///////////////////////////////////////////////////////////////////////

/**
 * 压缩全部CSS
 * 任务名 $$CleanCss 更名为 cleanCSS
 */
gulp.task('cleanCSS', ['old-clean-css', 'kb-clean-css-base']);

/**
 * 监听压缩全部CSS
 * 任务名 $$CleanCss:watch 更名为 cleanCSS:watch
 */
gulp.task('cleanCSS:watch', ['cleanCSS'], function () {
    gulp.watch(OLD_INPUT_PATH.moduleCSS, ['cleanCSS']);
});

//////////////////////////////////////////////////////////////////////
//- END

//////////////////////////////////////////////////////////////////////
//////////////////////////// 上线任务 ////////////////////////////////
/////////////////////////////////////////////////////////////////////

var waitingTasks = ['kbFramework', 'oldFramework', 'newFramework'];

/**
 * 上线任务(创建文件指纹)
 * ++++ 发布前使用 ++++
 */
gulp.task('build', waitingTasks, function () {
    // 在script、link引用的js、css路径后面添加文件指纹（MD5）
    require('./private_modules/fingerprint')({
        // 基础路径，src、dist、mapping都是基于base的
        base: __dirname,
        src: ['css', 'js', 'third-plugin/path.config.js'],
        dist: [
            '../WEB-INF/ftl/yqx',
            '../WEB-INF/ftl/layout/header.ftl',
            '../WEB-INF/ftl/layout/ng-header.ftl'
        ],
        mapping: '/'
    });
    process.nextTick(function () {
        console.log("=============================== build finished ======================================");
    });
});

/**
 * 编译所有框架
 * ++++ 开发、测试时使用 ++++
 */
gulp.task('default', waitingTasks);

/////////////////////////////////////////////////////////////////////
///////////////////////////// 上线任务 //////////////////////////////
////////////////////////////////////////////////////////////////////



