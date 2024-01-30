function currentYear() {
    var today = new Date();
    var year = today.getFullYear();
    return year;
}

function currentMonth() {
    var today = new Date();
    var month = (today.getMonth() + 1).toString().padStart(2, '0');
    return month;
}

function  currentDay() {
    var today = new Date();
    var day = today.getDate().toString().padStart(2, '0');
    return day;
}

function selectDate(time) {
    console.log(time);
    console.log(parseInt(currentDay()) - 7);
    var date1 = null;
    var date2 = null;
    if (time === 0) {
        date1 = currentYear() + '-' + currentMonth() + '-' + currentDay();
        date2 = currentYear() + '-' + currentMonth() + '-' + currentDay();
    }else if (time === 7) {
        date1 = currentYear() + '-' + currentMonth() + '-' + (parseInt(currentDay()) - 7);
        date2 = currentYear() + '-' + currentMonth() + '-' + currentDay();
    }else if (time === 30) {
        if (currentMonth() == 1) {
            date1 = (currentYear() - 1) + '-' + 12 + '-' + currentDay();
        }else {
            date1 = currentYear() + '-' + (parseInt(currentMonth()) - 1) + '-' + currentDay();
        }
        date2 = currentYear() + '-' + currentMonth() + '-' + currentDay();
    }

    $('#predate').val(date1);
    $('#postdate').val(date2);
}

$(document).ready(function() {
    $('#predate').val(currentYear() + '-' + currentMonth() + '-' + currentDay());
    $('#postdate').val(currentYear() + '-' + currentMonth() + '-' + currentDay());
});