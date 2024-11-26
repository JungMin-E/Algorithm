function pro1() {
    return new Promise(function(resolve, reject) {
        setTimeout(function() {
            reject('pro1 fail!'); 
        }, 1000);
    });
}

function pro2() {
    return new Promise(function(resolve, reject) {
        setTimeout(function() {
            resolve('pro2 success!');
        }, 1000);
    });
}

pro1()
    .then(function(result) {
        console.log('result', result);
        return pro2();
    })
    .catch(function(err) {
        console.log('err', err);
        return Promise.reject(err);
    })
    .then(function(result) {
        console.log('result2', result);
    })
    .catch(function(err) {
        console.log('Final catch:', err);
    });

const fetchNumber = new Promise((resolve, reject) => {
    setTimeout(() => resolve(1), 1000);
});

//프로미스 체이닝
fetchNumber
.then(num => num * 2)
.then(num => num * 3)
.then(num => {
    return new Promise((resolve, reject) => {
        setTimeout(() => resolve(num -1), 1000);
    });
})
.then(num => console.log(num)); //5가 출력된다