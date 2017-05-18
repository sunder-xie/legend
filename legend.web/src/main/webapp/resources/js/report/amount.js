/*
 * create by hyx 2016-04-22
 * 经营报表--营业页面
 */
$(function(){
  $doc=$(document);
  //增加两个图标
  $('.amount>thead>tr>th:last-child').append('<img class="toggle icon" src="'+BASE_PATH+'/resources/images/report/amount/down.jpg"/>');
  $('.amount>tbody>tr>td:last-child').append('<img class="more icon" src="'+BASE_PATH+'/resources/images/report/amount/more.jpg"/>');
  //加号增加icon类
  $('.rp-plus-icon').addClass('icon');
  $('.icon').parent().css('position','relative');
  //执行一次设置三个div等于最大的高
  maxHeight();
  //点击第一行之后
  $doc.on('click', '.amount>thead>tr', function () {
    var path;
    //要隐藏显示的内容
    var $toggle = $(this).parents('.amount').children('tbody');
    if ($toggle.is(':hidden')) {
      $toggle.show();
      //图标变为up
      path = BASE_PATH + '/resources/images/report/amount/up.jpg';
    } else {
      $toggle.hide();
      //图标变为down
      path = BASE_PATH + '/resources/images/report/amount/down.jpg';
    }
    $(this).find('img.toggle').attr("src", path);
    //执行一次设置三个div等于最大的高
    maxHeight();
  });
  //maxHeight()函数
  function maxHeight(){
    var $panel = $('.info'),
        ha, hb, hc, max;
    $panel.css('height', 'auto');
    //得到每一个div包括padding及border的高
    ha = $panel.eq(0).outerHeight();
    hb = $panel.eq(1).outerHeight();
    hc = $panel.eq(2).outerHeight();
    max = Math.max(ha, hb, hc);
    $panel.css('height',max);
  }
  //点击+出现的下拉列表里面的checkbox时,对应的table出现与否
  $('.check-box input').attr('checked',true);
  $doc.on('click','.check-box input',function(){
    var index1=$(this).parents('.info').index();
    var index2=$(this).parents('li').index();
    $tog=$(this).parents('#mainAmount').find('.info').eq(index1).find('.amount').eq(index2);
    console.log(index1);
    console.log(index2);
    if($(this).is(':checked')){
      console.log('show');
      $tog.show();

    }else{
      $tog.hide();
      console.log('hide');

    }
  })
  //点击跳转页面
$(document).on('click','.amount>tbody>tr',function(){
  var url=$(this).data('url');
  window.open(url);
});

});