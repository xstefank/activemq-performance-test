var data_array_2 =
[['Time', 'ActiveMQ microservice', 'JMS Standard']];

var columns = [2, 2]; // 2, 2
var limiter = [http_throughput20160504165920.length, http_throughput20160504165418.length]; // arr.length, arr.length
var data = [http_throughput20160504165920, http_throughput20160504165418];
var len = data.length;

var counter = [];
for (var i = 0; i < len; i++) {
    counter.push(1); // 0 is header
}

function countersUnderLimit() {
    var res = false;

    for (var i = 0; i < len; i++) {
        res = res || (counter[i] < limiter[i]);
    }

    return res;
}

function getTm(counter, dataArray) {
    if (counter < dataArray.length) {
        var tmStr = dataArray[counter][0];
        if (tmStr instanceof Date) {
            return tmStr.getTime();
        }
        return +tmStr;
    }

    return null;
}

var warmUpCol = [];
for (var i = 0; i < len; i++) {
    warmUpCol.push(data[i][0].indexOf('warmUp'));
}

function isWarmUp() {
    var res = false;

    for (var i = 0; i < len; i++) {
        res = res || isWarmUpFor(i);
    }

    return res;
}

function isWarmUpFor(i) {
    if (warmUpCol[i] != -1) {
        return data[i][counter[i]][warmUpCol[i]];
    } else {
        return false;
    }
}

while (countersUnderLimit()) {
    var d = [];
    for (var i = 0; i < len; i++) {
        d.push(i);
    }

    var tm = [];
    for (var i = 0; i < len; i++) {
        tm.push(getTm(counter[i], data[i]));
    }

    var stillWarmUp = isWarmUp();
    var min = null, minPos = null;
    for (var i = 0; i < len; i++) {
        if (!stillWarmUp || isWarmUpFor(i)) {
            if (min == null || (tm[i] != null && tm[i] < min)) {
                min = tm[i];
                minPos = i;
            }
        }
    }

    if (minPos != null) {
        d[0] = data[minPos][counter[minPos]][0];
        for (var i = 0; i < len; i++) {
            if ((!stillWarmUp || isWarmUpFor(i)) && tm[i] == min) {
                d[i + 1] = data[i][counter[i]][columns[i]];
                counter[i]++;
            } else {
                d[i + 1] = null;
            }
        }

        data_array_2
    .
        push(d);
    }
}
