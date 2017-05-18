/**
 * 配件详情页面
 * zmx 2016-08-10
 */

$(function(){
   var doc = $(document);

   seajs.use([
        'art',
        'dialog'
   ],function(at,dg){

       doc.on('click','.js-history-record',function(){
           var html = at('recordTpl',{});
           dg.open({
               area:['580px','350px'],
               content:html
           })
       });

       doc.on('click','.list-tab',function(){
           $(this).addClass('current-tab').siblings().removeClass('current-tab');
       })
   });
});