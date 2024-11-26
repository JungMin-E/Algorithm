// 프로미스 기본 코드 var pro1 = new Prommise(function(resolve, reject) {}); 외우지는 않아도 존재는 알자
//비동기임을 보여준다.
console.log('start');

//
var pro1 = new Promise(function(resolve, reject) {
    setTimeout(function() {
        console.log('promise');
        resolve('success');
        //resolve(99);
    }, 1000);
    
});

pro1.then(function(result) {
    console.log('result' + result);
})
//console.log('end');


