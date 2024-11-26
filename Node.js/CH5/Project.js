const readline = require("readline");
const rl = readline.Interface({
    input: process.stdin,
    output: process.stdout,
});

var arr = [];

function func() {
    rl.question('? ', (a) => {
        
        const [a1, a2] = a.split(' ');
        
        if(a1 == 'add') {
            arr.push(a2);
            for(var i = 0; i < arr.length; i++) {
                console.log(arr[i]);   
            }
            func();
        }
        if(a1 == 'sum') {
            var s = 0;
            for(var i = 0; i < arr.length; i++) {
                s += parseInt(arr[i]);
            }
            console.log(s);
            func();
        }
        if(a1 == 'remove') {
            var num = arr.indexOf(a2);
            arr.splice(num, 1);
            for(var i = 0; i < arr.length; i++) {
                console.log(arr[i]);
            }
            func();
        }
        if(a1 == 'avg') {
            var ss = 0;
            for(var i = 0; i < arr.length; i++) {
                ss += parseInt(arr[i]);
            }
            var avg = ss / arr.length;
            console.log(avg);
            func();
        }
        if(a1 == 'exit') {
            rl.close();
            return;
        }

    })
}

func();