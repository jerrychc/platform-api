/**
 * Created by admin on 2018/2/26.
 */
$(function () {
    $('#generateEncryPassword').on('click',function () {
        $('#superMgrForm').attr('data-validate-success','generateEncryPassword()');
        $('#superMgrForm').submit();
    });

    window.generateEncryPassword = function () {
        var jsonData = {
            userName:$('#userName').val(),
            password:$('#password').val()
        };
        $.ajax({
            url:hostUrl + 'sys/superMgr/generateEncryPassword',
            data:JSON.stringify(jsonData),
            type:'POST',
            contentType:'application/json',
            dataType:'JSON',
            success:function (resultData ) {
                if(resultData&&resultData.success) {
                    $('#encryPassword').val(resultData.result);
                    $.xljUtils.tip('green',resultData.msg);
                }else {
                    $.xljUtils.tip('red',resultData.msg);
                }
            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
                var status = XMLHttpRequest.status;
                $.xljUtils.getError(status);
            }
        });
    };

    $('#closeWindowBtn').on('click',function () {
        window.close();
    });
});