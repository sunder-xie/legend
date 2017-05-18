/**
 * Created by sky on 2016/12/21.
 */
var gulp = require('gulp')
//- 插件按需加载
var $ = require('gulp-load-plugins')()
//- 添加浏览器前缀 == stats.json 为浏览器兼容配置
var autoprefixer = require('autoprefixer')

/**
 * CSS 编译任务
 */

gulp.task('css-compile', function () {
    return gulp.src([
        'static/style/**/*.scss',
        '!static/style/base.scss'
    ])
        .pipe($.sourcemaps.init())
        .pipe($.sass().on('error', $.sass.logError))
        .pipe($.csscomb())
        .pipe($.postcss([
            autoprefixer({
                browsers: ['Chrome > 20'],
                cascade: false,
                add: true,
                remove: true
            })
        ]))
        .pipe($.cleanCss({debug: true}, function (details) {
            console.log('==================================================== ' + details.name + ' ====================================================')
            console.log(details.name + '\'s original size:' + (details.stats.originalSize / 1024).toFixed(2) + 'KB')
            console.log(details.name + '\'s minify size:' + (details.stats.minifiedSize / 1024).toFixed(2) + 'KB')
            console.log(details.name + '\'s efficiency:' + Math.round(details.stats.efficiency * 100) + '%')
        }))
        .pipe($.sourcemaps.write())
        .pipe(gulp.dest('static/style'))
})

/* CSS 编译任务 end */

/**
 * JS 编译任务
 */

gulp.task('js-widget-compile', function () {
    return gulp.src([
        'static/script/widget/**/*.js', 
        '!static/script/widget/**/*.min.js'
    ])
        .pipe($.uglify())
        .pipe($.rename({
            suffix: '.min',
            extname: '.js'
        }))
        .pipe(gulp.dest('static/script/widget'))
});

gulp.task('vue-combo', function () {
    var src = [
            'static/script/lib/vue/hammer.js',
            'static/script/lib/vue/*.js',
            '!static/script/lib/vue/*-combo.js',
            '!static/script/lib/vue/*-combo.min.js'
    ],
        destPath = 'static/script/lib/vue';
    
    gulp.src([
        'static/script/lib/vue/vue.dev.js',
        '!static/script/lib/vue/*.prod.js'
    ].concat(src))
        .pipe($.concat('vue-combo.js'))
        .pipe(gulp.dest(destPath))
    
    gulp.src([
        'static/script/lib/vue/vue.prod.js',
        'static/script/lib/vue/*.dev.js'
    ].concat(src))
        .pipe($.concat('vue-combo.min.js'))
        .pipe($.uglify())
        .pipe(gulp.dest(destPath))
})

/* JS 编译任务 end */

/**
 * Lint 代码检测任务
 */

gulp.task('js-lint', function () {
    return gulp.src([
        'static/script/**/*.js',
        'html/**/*.html',
        '!static/script/**/*.min.js',
        '!static/script/lib/**/*.js'
    ])
        .pipe($.eslint())
        .pipe($.eslint.format())
        .pipe($.eslint.failOnError())
})

/* Lint 代码检测任务 end */

/**
 * watch 代码监听任务
 */

var watch_css_modules = ['css-compile'];
gulp.task('watch:css-compile', watch_css_modules, function () {
    gulp.watch([
        'static/style/**/*.scss'
    ], watch_css_modules)
})

var watch_js_modules = ['js-widget-compile'];
gulp.task('watch:js-compile', watch_js_modules, function () {
    gulp.watch([
        'static/script/widget/**/*.js',
        '!static/script/widget/**/*.min.js'
    ], watch_js_modules)
})

/* watch 代码监听任务 end */

/**
 * 组合任务
 */

gulp.task('default', ['watch:css-compile', 'watch:js-compile'])

gulp.task('build', ['js-lint'].concat(watch_css_modules, watch_js_modules))

/* 组合任务 end */
