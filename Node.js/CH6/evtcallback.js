//coffee라는 이름의 함수표현식 const라고 굳이 선언하지 않더라도 var, let으로 선언해줘야함
const coffee = function() {
    console.log("주문하신 아메리카노 나왔습니다.");
}

setTimeout(coffee, 5000); //setTime함수 첫번째 매개변수에 coffee라는 함수, 5000밀리초(5초) 
