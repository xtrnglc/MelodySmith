var express = require('express');
var app = express();
var path = require('path');
var formidable = require('formidable');
var fs = require('fs');

app.use(express.static(path.join(__dirname, 'public')));

app.get('/', function(req, res){
  res.sendFile(path.join(__dirname, 'views/forge.html'));
});

app.get('/getmidi', function(req, res) {
    //console.log(req.query);
    

    //call melodysmith jar with args here
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



    res.send('yello');
});


app.post('/upload', function(req, res){
  console.log('upload');
});

var server = app.listen(3000, function(){
  console.log('Server listening on port 3000');
});
