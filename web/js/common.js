/**
 * Created with IntelliJ IDEA.
 * User: caiwm
 * Date: 13-8-21
 * Time: 上午9:48
 * To change this template use File | Settings | File Templates.
 */



function postToServer(param, _ajax_type, _is_async, url, _func_map) {
    if (null == _ajax_type || _ajax_type == undefined)
        _ajax_type = "POST";
    if (null == _is_async || _is_async == undefined)
        _is_async = false;

    $.ajax({
        type: _ajax_type,
        async: _is_async,
        url: url,
        data: param,
        success: function (data) {
            _func_map["success"](data);
        }
    });
}


/**
 * 使用JQuery队列，实现延迟加载
 *
 * $obj 是 $(this)
 * eventMap的结构[{"dt":延后时间-毫秒,"fc":延时结束调用的方法},...]
 *
 * @param $obj
 * @param eventMap
 */
function putFuncInQueue($obj, eventMap){
    if(null == eventMap || eventMap == undefined){
        eventMap.length = 0;
    }
    for(var i = 0; i< eventMap.length; i++){
        $obj.delay(eventMap[i]["dt"], "qa_queue_" + i)
            .queue("qa_queue_" + i, eventMap[i]["fc"])
            .dequeue("qa_queue_" + i);
    }
}


function html_encode(str) {
    var s = "";
    if (str.length == 0) return "";
    s = str.replace(/&/g, "&gt;");
    s = s.replace(/</g, "&lt;");
    s = s.replace(/>/g, "&gt;");
    s = s.replace(/ /g, "&nbsp;");
    s = s.replace(/\'/g, "&#39;");
    s = s.replace(/\"/g, "&quot;");
    s = s.replace(/\n/g, "<br>");
    return s;
}

function html_decode(str) {
    var s = "";
    if (str.length == 0) return "";
    s = str.replace(/&gt;/g, "&");
    s = s.replace(/&lt;/g, "<");
    s = s.replace(/&gt;/g, ">");
    s = s.replace(/&nbsp;/g, " ");
    s = s.replace(/&#39;/g, "\'");
    s = s.replace(/&quot;/g, "\"");
    s = s.replace(/<br>/g, "\n");
    return s;
}
