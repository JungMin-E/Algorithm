const readline = require("readline");
const rl = readline.Interface({
    input: process.stdin,
    output: process.stdout,
});

var arr = ["aaa", "bbb", "ccc"]; 

for(var i = 0; i < arr.length; i++) {
    console.log((i + 1) + '.' + arr[i]);
}

function ele() {
    rl.question('추가할 단어를 입력하세요: ', (a) => {
        if(arr.length <= 3) {
            arr.push(a);
            for(var i = 0; i < arr.length; i++) {
                console.log((i + 1) + '.' + arr[i]);
            }
        }
        else {
            console.log('최대 단어는 3개까지 입력할 수 있습니다.');
        }
        rl.close();
    })
}

ele();


