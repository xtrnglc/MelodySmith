var MidiPlayer = MidiPlayer;
var loadFile, loadDataUri, Player;
var AudioContext = window.AudioContext || window.webkitAudioContext || false;
var ac = new AudioContext || new webkitAudioContext;
var eventsDiv = document.getElementById('events');
var songDataURI = '';
var width = 960;
var height = 400;
var numComparisons = 6;
var restType = 'Constructive';
var rhythmicImportance = '0';
var melodicImportance = '0';
var syncopation = '0';
var phraseLength = '0';
var creativity = '0';
var speed = '0';
var restAmount = '0';
// Rhythmic importance (Int 0-100)  "How closely should rhythmic details be analyzed?" (edited)
// Melodic importance (Int 0-100)  "How closely should intervalic details be analyzed?" (edited)
//
// Composition Controls:
// Rest Type (Switch with options: Constructive, Destructive)  "How should rests be added to the melody?" (edited)
// Rest Amount (Int 0-20) "How often should rests be played?" (edited)
// Syncopation  (Int 0-100) "How syncopated should the rhythm be?" (edited)
// Phrase Length (Int 0-20) "About how many notes should one phrase last?" (edited)
// Creativity (Int 0-10) "How often can less conventional notes be played?" (edited)
// Speed (Int 0-100) "Should shorter or longer notes be preferred?" (edited)

var toggleRestType = function() {
    if(restType === 'Constructive') {
        restType = 'Destructive';
    } else {
        restType = 'Constructive';
    }
}

var setRestAmount = function(val) {
    restAmount = val * 2;
}

var setRhythmicImportance = function(val) {
    rhythmicImportance = val * 10;
}

var setMelodicImportance = function(val) {
    melodicImportance = val * 10;
}

var setSyncopation = function(val) {
    syncopation = val * 10;
}

var setPhraseLength = function(val) {
    phraseLength = val * 2;
}

var setCreativity = function(val) {
    creativity = val;
}

var setSpeed = function(val) {
    speed = val * 10;
}

const grandPiano = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/acoustic_grand_piano-mp3.js';
const acousticGuitar = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/acoustic_guitar_nylon-mp3.js';
const flute = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/flute-mp3.js';
const electricGuitar = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/electric_guitar_clean-mp3.js';
const musicBox = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/music_box-mp3.js';

var instrument = grandPiano;

var changeTempo = function(tempo) {
	Player.tempo = tempo;
}

var play = function() {
	Player.play();
	document.getElementById('play-button').innerHTML = 'Pause';
}

var keySignature = 'gChromatic';


var changeNumComparisons = function(newVal) {
    numComparisons = newVal;
}

var changeNgrams = function(newVal) {
    nGramVal = newVal;
}

var svg = d3.select("#canvas")
    .append("svg")
    .attr("width", width)
    .attr("height", height);

var color = d3.scaleLinear().domain([45,75])
    .interpolate(d3.interpolateHcl)
    .range([d3.rgb("#007AFF"), d3.rgb('#FFF500')]);

var forge = function() {

    if(Player) {
        stop();
    }
    document.getElementById('loading').style.display = 'none';

    document.getElementById('loadingcube').style.display = 'block';
    var artistslider1 = document.getElementById('artist-slider1').value;
    var artistslider2 = document.getElementById('artist-slider2').value;
    var artistslider3 = document.getElementById('artist-slider3').value;

    if(document.getElementById('keysigdropdown').value === '') {

    } else {
        keySignature = document.getElementById('keysigdropdown').value;
    }


    var formData = {
        bieber: artistslider1,
        bach:artistslider2,
        beatles:artistslider3,
        rhythmicImportance: rhythmicImportance,
        melodicImportance: melodicImportance,
        speed: speed,
        syncopation : syncopation,
        phraseLength : phraseLength,
        restAmount : restAmount,
        creativity : creativity,
        restType : restType,
        keySignature : keySignature
    };

    console.log(formData);

    //console.log(formData);
    $.ajax({
        url : '/getmidi',
        type: 'GET',
        data: formData,
        success : function(data) {
            initiatePlayer(data);
        }
    })
}

var switchInstrument = function(val) {
    if(val === 'acousticGuitar') {
        instrument = acousticGuitar;
    }
    if(val === 'flute') {
        instrument = flute;
    }
    if(val === 'electricGuitar') {
        instrument = electricGuitar;
    }
    if(val === 'musicBox') {
        instrument = musicBox;
    }if(val === 'grandPiano') {
        instrument = grandPiano;
    }
}

var showControls = function() {
    document.getElementById('loadingcube').style.display = 'none';
    document.getElementById('canvas').style.display = 'block';
    document.getElementById('music-controls').style.display = 'block';
    document.getElementById('tempo-div').style.display = 'block';
    document.getElementById('play-bar-button').style.display = 'block';
    document.getElementById('visualization').style.display = 'block';
}

var initiatePlayer = function(data, instrumentVal) {

    showControls();

    switchInstrument(instrumentVal);

    Soundfont.instrument(ac, instrument).then(function (instrument) {

        loadDataUri = function(dataUri) {
            Player = new MidiPlayer.Player(function(event) {
                //console.log(event);
                if (event.name == 'Note on' && event.velocity > 0) {
                    instrument.play(event.noteName, ac.currentTime, {gain:event.velocity/100});
                    //document.querySelector('#track-' + event.track + ' code').innerHTML = JSON.stringify(event);
                    //console.log(event);

                }
                if(event.name=="Note on"){

                    //http://localhost/gist/audio/jesu/
                    var elLength = 40*(event.delta<=1?1:event.delta/120);
                    var element = svg.append("g");

                    //console.log(element);
                    element.attr("transform","translate("+(-1*elLength)+" 0)");
                    element.append("rect")
                        .attr("width", elLength)
                        .attr("height", 20)
                        .attr("rx", 5)
                        .attr("ry", 5)
                        .attr("x", 0)
                        .attr("y", (event.noteNumber - 45)*12)
                        .attr("fill", color(event.noteNumber));

                    element.append("text")
                        .attr("x", 3)
                        .attr("y", 15 + (event.noteNumber - 45)*12)
                        .text(event.noteName);


                    var t = d3.transition()
                        .duration(4000)
                        .ease(d3.easeLinear);
                    element.transition(t)
                        .attr("transform","translate("+(width+300-elLength)+" 0)")
                        .remove();
                   // console.log(element);
                }


                document.getElementById('tempo-display').innerHTML = Player.tempo;
                document.getElementById('play-bar').style.width = 100 - Player.getSongPercentRemaining() + '%';
            });

            Player.loadDataUri(dataUri);

            document.getElementById('play-button').removeAttribute('disabled');

            //buildTracksHtml();
            play();
        }

        songDataURI = data;

        loadDataUri(songDataURI);
    });
}

var changeInstrument = function(instrumentval) {
    console.log('changing instruments ' + instrumentval);
    switchInstrument(instrumentval);
    if(Player) {
        Player.stop();
        initiatePlayer(songDataURI, instrument);
    }

}

var pause = function() {
	Player.pause();
	document.getElementById('play-button').innerHTML = 'Play';
}

var stop = function() {
	Player.stop();
	document.getElementById('play-button').innerHTML = 'Play';
}

var buildTracksHtml = function() {
	Player.tracks.forEach(function(item, index) {
		var trackDiv = document.createElement('div');
		trackDiv.id = 'track-' + (index+1);
		var h5 = document.createElement('h5');
		h5.innerHTML = 'Track ' + (index+1);
		var code = document.createElement('code');
		trackDiv.appendChild(h5);
		trackDiv.appendChild(code);
		eventsDiv.appendChild(trackDiv);
	});
}



var mario = 'data:audio/midi;base64,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gDwAAJA+aAiAPgAAkDxiCIA8AACQPF8IgDwAAJA8bQiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AAH/LwA=';


