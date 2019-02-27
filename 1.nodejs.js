function group() {
    this.s = 0;
    this.e = 0;
    this.p = 0;
}

function inScop(gourpA, gourpB) {
    if (gourpA.constructor.name == group.name &&
        gourpB.constructor.name == group.name) {
        if (gourpA.s >= gourpB.e && gourpA.e <= gourpB.e) {
            return true;
        }
    }
    return false;
}

function oneScop(gourpA, gourpB) {
    if (gourpA.constructor.name == group.name &&
        gourpB.constructor.name == group.name) {
        if (gourpA.s === gourpB.s && gourpA.e === gourpB.e) {
            return true;
        }
    }
    return false;
}

function sortGroup(groupArr) {
    if (!Array.isArray(groupArr)) {
        return;
    }
    groupArr.sort((a, b) => {
        return a.s - b.s;
    });
    return groupArr;
}

function calcP(arr) {
    var re = new Array();
    if (!Array.isArray(arr)) {
        return re;
    }

    var end = 0;
    var g;
    var a;
    var b;
    var last = arr.length - 1;
    for (i = 0; i < arr.length - 1; i++) {
        a = arr[i];
        b = arr[i + 1];
        g = new group();
        if (oneScop(a, b)) {
            g.s = a.s;
            g.e = a.e;
            g.p = a.p > b.p ? b.p : a.p;
            --last;
        } else {
            if (end > 0) {
                g.s = end;
                end = 0;
            }

            if (a.e <= b.s) {
                g.e = a.e;
                g.p = a.p;
            } else {
                end = a.e - b.s;
                g.s = a.s;
                g.e = b.s;
                if (inScop(a, b)) {
                    if (b.p < a.p) {
                        g.p = b.p;
                    }
                }
            }

            if (0 === g.p) {
                g.p = a.p;
            }
            if (0 === g.s) {
                g.s = a.s;
            }
        }

        re.push(g);
    }

    if (last > 0) {
        a = arr[arr.length - 1];
        re.push(a);
    }
    return re;
}
/**[1, 5, 10], [3, 6, 5]
Returns:
[1, 3, 10], [3, 6, 5]

 */
function main() {
    var groupArr = new Array();
    var g = new group();
    g.s = 1;
    g.e = 6;
    g.p = 20;
    groupArr.push(g);
    g = new group();
    g.s = 5;
    g.e = 9;
    g.p = 25;
    groupArr.push(g);
    g = new group();
    g.s = 3;
    g.e = 5;
    g.p = 10;
    groupArr.push(g);
    g = new group();
    g.s = 10;
    g.e = 15;
    g.p = 8;
    groupArr.push(g);
    g = new group();
    g.s = 9;
    g.e = 12;
    g.p = 20;
    groupArr.push(g);

    groupArr = sortGroup(groupArr);

    var m_resultArr = calcP(groupArr);

    console.log(m_resultArr);

}

//call main function to start
main();
