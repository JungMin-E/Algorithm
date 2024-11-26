const readline = require("readline");
const rl = readline.Interface({
    input: process.stdin,
    output: process.stdout,
});

var max = -10000000; 
var min = 100000000;

function func() {
    rl.question('? ', (a) => {

        var b = parseInt(a);

        if(a == '-1') {
            console.log('max = ' + max);
            console.log('min = ' + min);
            rl.close();
            return;
        }
        else {
            if(max < b) {
                max = b;
            }
            if(min > b) {
                min = b;
            }
        }
        func();
    })
}

func();