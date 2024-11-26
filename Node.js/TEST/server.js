var mysql = require('mysql2');
const express = require('express');

var conn = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "",
    database: "proj"
});

conn.connect();

const app = express();

app.listen(8080, function(){
    console.log("포트 8080으로 서버 대기중 ... ")
});

app.get('/list', (req, res) => {
    const { author, title, isbn } = req.query;

    var query = '';

    if(author === undefined && title === undefined && isbn !== undefined) {
        query = `select * from book where author like '${author}%'`;
    }
    else if(author !== undefined && title === undefined && isbn === undefined) {
        query = `select * from book where title like '${title}%'`;
    }
    else if(author === undefined && title !== undefined && isbn == undefined) {
        query = `select * from book where isbn like '${isbn}%'`;
    }

    conn.query(query, (err, result) => {
        if(err) {
            console.log(err);
            throw err;
        }

        res.send(result);
    });
})

app.get('/rentallist', (req, res) => {
    const { notreturned = 'false', onlycount = 'false' } = req.query; /*위처럼 초기값을 설정할 수 있음*/

    if(notreturned == 'true')
    {
        var query1 = `select count(rental_id) from rental 
                        left join book on rental.book_id = book.book_id
                        left join rental_user on rental.rental_user_id = rental_user.rental_user_id`

        var query2 = `select rental_id, title, rental_date, return_date, 
	                    rental.rental_user_id, rental_user_name from rental 
                        left join book on rental.book_id = book.book_id
                        left join rental_user on rental.rental_user_id = rental_user.rental_user_id
                        where return_date is null`;

        conn.query(onlycount == 'true' ? query2 : query1, (err, result) => {
            if(err) {
                console.log(err);
                throw err;
            }
            
            res.send(result);
        });
    }
})

app.get('/rental', (req, res) => {
    const { book_id, rental_user_id } = req.query;

    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    const yyyymmdd = `${year}-${month}-${day}`;

    var query1 = `insert into rental values (0, ${book_id}, '${yyyymmdd}', null, ${rental_user_id})`;
    conn.query(query1, (err, result) => {
        if(err) {
            console.log(err);
            throw err;
        }

        res.send(result);
    });
})

app.get('/return', (req, res) => {
    const { rental_id } = req.query;

    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    const yyyymmdd = `${year}-${month}-${day}`;

    var query1 = `update rental set return_date = '${yyyymmdd}' where rental_id = ${rental_id}`;
    conn.query(query1, (err, result) => {
        if(err) {
            console.log(err);
            throw err;
        }

        res.send(result);
    });
})
/*
app.get('/delaylist', (req, res) => {
    const { date } = req.query;

    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    const yyyymmdd = `${year}-${month}-${day}`;

    var query1 = `select b.title, b.author, r.rental_date, r.return_date from rental r join book b on r.book_id = b.book_id where (r.return_date is null and datediff('${date}', r.rental_date) > 7)`;

    conn.query(query1, (err, result) => {
        if(err) {
            console.log(err);
            throw err;
        }

        res.send(result);
    });
})
*/

app.get('/delaylist', (req, res) => {
    const { date } = req.query;
    //console.log(date); 
    const convertDate = new Date(
        date.substring(0, 4),
        date.substring(4, 6) - 1,
        date.substring(6, 8)
    );
    console.log(convertDate);
    //const criteriaDate = new Date(convertDate);
    const criteriaDate = new Date(2024, 9, 20);
    criteriaDate.setDate(criteriaDate.getDate() - 7);
    conn.query('select * from rental', (err, result) => {
        if(err) {
            console.log(err);
            return res.status(500).json({ error : 'DB Error'});
        }
        var filterResult = result.filter(row => {
            const rental_date = new Date(row.rental_date)
            /*(
              row.rental_date.substring(0,4),
              row.rental_date.substring(5,7) - 1,
              row.rental_date.substring(8,10),
            );*/
            return rental_date <= criteriaDate;
        });
        res.send(filterResult);
        //res.send(result);
    });
});

app.get('/book', function(req, res){
    res.send('도서 목록 관련 페이지입니다.');
})
app.get('/getPost', function(req, res){
    const {title} = req.query;
    var query1 = `select * from post where title = '${title}'`;

    conn.query(query1, (err,rows, fields) =>
    {
        if(err) throw err;
        res.send(rows);    
    });    

})


/*
1. 저자의 성이 Kim인 모든 책을 조회
http://localhost/list?title=알고리즘
http://localhost/list?isbn=978
http://localhost/list?author=Kim
2. 대여된 책 중 반납되지 않은 책의 개수는?
http://localhost/rentalist?notreturned=true 
http://localhost/rentalist?notreturned=true&onlycount=true
3. 반납일자가 연체된 책을 조회
http://localhost/delaylist?date=20241111
*/