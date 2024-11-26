const readline = require("readline");
const rl = readline.Interface({
    input: process.stdin,
    output: process.stdout,
});

var arr = ["abc", "aaa", "ddd"];
/*
console.log(arr);
arr.push('eee');
console.log(arr.pop()); //push시 배열의 제일 뒤에 넣고 pop시 배열의 제일 뒤에 있는 거 삭제

console.log(arr);
*/

for(var i = 0; i < arr.length; i++) {
    console.log((i+1) + "." + arr[i]);
}
console.log();

rl.question("바꾸고자하는 문자열은? ", (a) => {
    var n = arr.indexOf(a);
    arr.splice(n, 1); //arr배열에서 n번째 인덱스 1(하나)삭제

    rl.question("문자열을 입력하시오 : ", (b) => {
        arr.splice(n, 0, b); //n번쨰 위치에 b를 넣는다 두번쨰 매개변수인 0은 요소를 삭제하지 않는다는 양수라면 지정된 개수의 요소를 제거

        for(var j = 0; j < arr.length; j++) {
            console.log((j+1) + "." + arr[j]);
        }
        rl.close();
    })
});



