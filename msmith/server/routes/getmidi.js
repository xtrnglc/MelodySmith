var express = require('express');
var router = express.Router();
var fs = require('fs');


router.get('/', function(req, res, next) {
    res.send({ val: 'Hello From Express' });
});

/* GET users listing. */
router.post('/', function(req, res, next) {
    console.log('post')
    var child = require('child_process').spawn(
        'java', ['-jar', './Main.jar', 'argument to pass in']
    );

    child.stdout.on('data', function(data) {
        console.log(data.toString());
    });

    child.stderr.on("data", function (data) {
        console.log(data.toString());
    });

    var readStream = fs.createReadStream('./test.txt');
    readStream.pipe(res);

    res.send({ val: 'Hello From Express' });

});

module.exports = router;

