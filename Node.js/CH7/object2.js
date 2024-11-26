let user = {
    id : "jamsu",
    pw : "1111",
    name : "lch",
    moblie : "010-5341-xxxx",
    country : "가나"
};

//user객체에 있는 info정보 모두 반복하는 루프 
for(let info in user) {
    console.log(`${info} : ${user[info]}`);
    //${info}해당 user객체에 key값을 가져옴 
    //${user[info]}user객체에 value값을 가져옴
}