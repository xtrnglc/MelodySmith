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

    // //pathToCorpus keySignature artist:weight artist:weight
    // { backstreetboys: '50',
    //     beethoven: '50',
    //     katy: '50',
    //     durationsOfNotesSliderval: '0',
    //     intervalOfNoteSliderval: '0',
    //     nGramVal: '10',
    //     numComparisons: '1',
    //     keySignature: 'c' }

    //java -jar MelodySmith.jar corpusPath keySignature intervalWeight durationWeight nGramLength numberOfComparisons artist1Name:artist1Weight artist2Name:artist2Weight...
    //call melodysmith jar with args here

    console.log('running jar file\n');

    var child = require('child_process').spawn(
        'java', ['-jar', './MelodySmith.jar', 'public/corpus', req.query.keySignature, req.query.intervalOfNoteSliderval, req.query.durationsOfNotesSliderval, req.query.nGramVal, req.query.numComparisons, 'beatles:87']
    );

    // var exec = require('child_process').exec;
    // var child = exec('java -jar ./MelodySmith.jar public/corpus cGypsy 12 14 4 5 beatles:87',
    //     function (error, stdout, stderr){
    //         console.log('Output -> ' + stdout);
    //         if(stderr !== null){
    //             console.log("Error -> "+stderr);
    //         }
    //     });

    var encoded = 'test';
    child.stdout.on('data', function(data) {
        console.log(data.toString());
        if(data.toString().includes('100')){

            var filename = 'output.mid';

            const Datauri = require('datauri').sync;
            encoded = Datauri(filename);
            res.send(encoded);

        }
    });

    child.stderr.on("data", function (data) {
        console.log(data.toString());
    });


    //
    // var file = fs.readFileSync(filePath);
    // console.log('Initial File content : ' + file);
    //
    // fs.watch(filePath, function(event, filename) {
    //     if(filename){
    //         console.log('Event : ' + event);
    //         console.log(filename + ' file Changed ...');
    //         file = fs.readFileSync(filePath);
    //         console.log('File content at : ' + new Date() + ' is \n' + file);
    //     }
    //     else{
    //         console.log('filename not provided')
    //     }
    // });

    // var found = false;
    //
    // while(!found) {
    //     console.log(1);
    //     if (fs.existsSync(filePath)) {
    //         console.log('Found file');
    //         found = true;
    //     }
    // }
});


app.post('/upload', function(req, res){
  console.log('upload');
});

var server = app.listen(3000, function(){
  console.log('Server listening on port 3000');
});
