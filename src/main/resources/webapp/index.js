$(function(){
    $.ajax({
        cache: false,
        url:  contextPath + "client/servlet/SvnPackServlet?method=sysemOverview",
        type: "POST",
        dataType: "json",
        complete: function(){
            $.messager.progress("close");
        },
        success: function(res){
            debugger;
            if(res && res.busy){
                $("#releaseNo").text(res.releaseNo);
                $("#releaseNum").text(res.releaseNum);
                $("#updateNum").text(res.updateNum);
                $("#updateNo").text(res.updateNo);
                $("#busy").text(res.busy);
            }
        }
    })


})