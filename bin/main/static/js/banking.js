function getMoney(data, callback){
    var money = 0;
    $.ajax({
        url: '/banking/getmyacc',
        type: 'post',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify(data),
        success: function (result){
            callback(result.money);
            //console.log(result.money);
        },
        error: function (error){
            console.log(error);
        }
    });
};

function myaccmoneyview(){
    var myacc = $('#myacc').val();
    if (myacc != '0'){
        var data = {
            'myacc':myacc
        };
        getMoney(data, function (money) {
            $('#myaccmoney').val(money.toLocaleString('ko-KR'));
        });
    }else {
        alert('출금계좌번호를 선택해주세요.');
    }
}

function sendMoney(money){
    if ('100' === money) {
        $('#sendmoney').val('1000000');
    }else if ('50' === money) {
        $('#sendmoney').val('500000');
    }else if ('10' === money) {
        $('#sendmoney').val('100000');
    }else if ('5' === money) {
        $('#sendmoney').val('50000');
    }else if ('3' === money) {
        $('#sendmoney').val('30000');
    }else if ('1' === money) {
        $('#sendmoney').val('10000');
    }else if ('all' === money) {
        var myacc = $('#myacc').val();
        if (myacc != '0'){
            var data = {
                'myacc':myacc
            };
            getMoney(data, function (money) {
                $('#sendmoney').val(money);
            });
        }else {
            alert('출금계좌번호를 선택해주세요.');
        }
    }else if ('cancel' === money) {
        $('#sendmoney').val('');
    }
};

function checkselect(aim) {
    var result = true;
    var select = $('#' + aim);
    if (select.val() != '0') {
        result = false;
    }
    return result;
}

function selectStore() {
    var myacc = localStorage.getItem('myacc');
    var select = $('#myacc');
    select.each(function () {
        $(this).find('option').each(function () {
            if (myacc == $(this).val()) {
                $(this).prop('selected', true);
            }
        });
    });
}

function accList(){
    var accs = document.querySelectorAll(`tr[id^='myacc']`);
    accs.forEach((item) => {
        item.style.cursor = 'pointer';
        item.addEventListener('click', function (){
            localStorage.setItem('myacc', $('td:nth-child(2)').text());
        });
    });
}

function check() {
    var newWindow = window.open(
        '/banking/myaccount',
        '_blank',
        'width=350, height=400');
    newWindow.addEventListener('load', function () {
        newWindow.accList();
    });

    setInterval(function () {
        var myacc = localStorage.getItem('myacc');
        if (myacc != null) {
            newWindow.close();
            selectStore();
            localStorage.removeItem('myacc');
            return;
        }
    });
}

$(document).ready(function () {
    var url = window.location.pathname;
    if (url != '/banking/myaccount' && url != '/banking/checksend') {
        const form = document.forms['sendform'];
        form.addEventListener('submit', function (e) {
            e.preventDefault();
            var mypw = $('#mypw').val();
            var sendacc = $('#sendacc').val();
            var sendmoney = $('#sendmoney').val();
            var myaccioname = $('#myaccioname').val();
            var myaccmemo = $('#myaccmemo').val();
            var regnum = /^[0-9]+$/

            checkselect('myacc');

            if (checkselect('myacc')) {
                alert('출금계좌번호를 선택해주세요.');
                return;
            }else if (!mypw.match(regnum)) {
                alert('계좌비밀번호는 숫자만 입력해주세요.');
                return;
            }else if (mypw.length != 4) {
                alert('계좌비밀번호를 4자 이내로 입력해주세요.');
                return;
            }else if (checkselect('sendbank')) {
                alert('입금은행을 선택해주세요.');
                return;
            }else if (!sendacc.match(regnum)) {
                alert('입금계좌번호는 숫자만 입력해주세요.');
                return;
            }else if (!sendmoney.match(regnum)) {
                alert('이체금액은 숫자만 입력해주세요.');
                return;
            }else if (myaccioname.length != 0 && myaccioname.length > 15) {
                alert('받는통장 메모는 15자 이내로 작성해주세요.');
                return;
            }else if (myaccmemo.length != 0 && myaccmemo.length > 500) {
                alert('내통장 메모는 500자 이내로 작성해주세요.');
                return;
            }

            var bank = {
                'myacc':$('#myacc').val(),
                'mysendbank':$('#sendbank').val(),
                'mysendacc':$('#sendacc').val(),
                'myaccbalance':$('#sendmoney').val(),
                'myaccioname':$('#myaccioname').val(),
                'myaccmemo':$('#myaccmemo').val()
            }

            $.ajax({
                url: '/banking/confirm',
                type: 'post',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(bank),
                async: false,
                success: function (result){
                    console.log(result);
                },
                error: function (error){
                    console.log(error);
                }
            });

            var newWindow = window.open(
                '/banking/checksend',
                '_blank',
                'width=400, height=500');

            newWindow.addEventListener('beforeunload', function (e) {
                form.submit();
            });
        });
    }
});