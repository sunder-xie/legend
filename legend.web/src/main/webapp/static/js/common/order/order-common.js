/**wry 2016-04-22
 * 判断输入的小数的位数及超出位数的截掉
 * @param num
 * @param digit
 * @returns {string|*}
 */
function checkFloat(num, digit) {
    num = num + '';
    num = num.replace('。', '.');
    var index = num.indexOf('.'), reg = /^[0-9]*\.{0,1}[0-9]*$/;

    if(digit == 0 && index > 0) {
        return num.slice(0, index);
    }

    if(! +num && !reg.test(num)) {
        num = '';
    }
    if(num.slice(index+1).length > digit && index >= 0) {
        num = num.slice(0, index + digit + 1);
    }

    // string
    return num;
}