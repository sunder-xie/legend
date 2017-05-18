$(function () {
   seajs.use([
      'table',
       'dialog',
      'ajax'], function (table,dialog, ajax) {

      dialog.titleInit();
      table.init({
         url: BASE_PATH + '/shop/shop_service_info/get_gift_list',
         tplid: 'listTpl',
         pageid: 'listPage',
         fillid: 'listFill'
      });

   })
});