var MidiPlayer = MidiPlayer;
var loadFile, loadDataUri, Player;
var AudioContext = window.AudioContext || window.webkitAudioContext || false;
var ac = new AudioContext || new webkitAudioContext;
var eventsDiv = document.getElementById('events');

var changeTempo = function(tempo) {
	Player.tempo = tempo;
}

var play = function() {
	Player.play();
	document.getElementById('play-button').innerHTML = 'Pause';
}

var keySignature = 'gChromatic';

var cbutton = function() {
    document.getElementById('c-button').style.backgroundColor = '#0FA0CE';
    document.getElementById('a-button').style.backgroundColor = '#33C3F0';
}

var abutton = function() {
    document.getElementById('c-button').style.backgroundColor = '#33C3F0';
    document.getElementById('a-button').style.backgroundColor = '#0FA0CE';
}

var forge = function() {
    document.getElementById('loading').style.display = 'none';

    document.getElementById('loadingcube').style.display = 'block';
    var artistslider1 = document.getElementById('artist-slider1').value;
    var artistslider2 = document.getElementById('artist-slider2').value;
    var artistslider3 = document.getElementById('artist-slider3').value;

    //var chaosSliderval = document.getElementById('chaos-slider').value;
    var durationOfNotesSliderval = document.getElementById('durationOfNotes-slider').value;
    var intervalOfNoteSliderval = document.getElementById('intervalOfNote-slider').value;
    //var restSlider = document.getElementById('rest-slider').value;
    var nGramVal = document.getElementById('n-gram-selector').value;
    var numComparisons = document.getElementById('num-comparisons-selector').value;

    if(document.getElementById('keysigdropdown').value === '') {

    } else {
        keySignature = document.getElementById('keysigdropdown').value;
    }


    var formData = {
        backstreetboys: artistslider1,
        beethoven:artistslider2,
        katy:artistslider3,
        durationsOfNotesSliderval: durationOfNotesSliderval,
        intervalOfNoteSliderval: intervalOfNoteSliderval,
        nGramVal: nGramVal,
        numComparisons: numComparisons,
        keySignature : keySignature
    };

    //console.log(formData);
    $.ajax({
        url : '/getmidi',
        type: 'GET',
        data: formData,
        success : function(data) {

            document.getElementById('loadingcube').style.display = 'none';

            Soundfont.instrument(ac, 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/acoustic_guitar_nylon-mp3.js').then(function (instrument) {
                document.getElementById('loading').style.display = 'none';
                // loadFile = function() {
                //     var file    = document.querySelector('input[type=file]').files[0];
                //     var reader  = new FileReader();
                //     if (file) reader.readAsArrayBuffer(file);
                //
                //     eventsDiv.innerHTML = '';
                //
                //     reader.addEventListener("load", function () {
                //         Player = new MidiPlayer.Player(function(event) {
                //             if (event.name == 'Note on') {
                //                 instrument.play(event.noteName, ac.currentTime, {gain:event.velocity/100});
                //                 //document.querySelector('#track-' + event.track + ' code').innerHTML = JSON.stringify(event);
                //             }
                //
                //             document.getElementById('tempo-display').innerHTML = Player.tempo;
                //             document.getElementById('file-format-display').innerHTML = Player.format;
                //             document.getElementById('play-bar').style.width = 100 - Player.getSongPercentRemaining() + '%';
                //         });
                //
                //         Player.loadArrayBuffer(reader.result);
                //
                //         document.getElementById('play-button').removeAttribute('disabled');
                //
                //         //buildTracksHtml();
                //         play();
                //     }, false);
                // }

                loadDataUri = function(dataUri) {
                    Player = new MidiPlayer.Player(function(event) {
                        if (event.name == 'Note on' && event.velocity > 0) {
                            instrument.play(event.noteName, ac.currentTime, {gain:event.velocity/100});
                            //document.querySelector('#track-' + event.track + ' code').innerHTML = JSON.stringify(event);
                            //console.log(event);
                        }

                        document.getElementById('tempo-display').innerHTML = Player.tempo;
                        document.getElementById('play-bar').style.width = 100 - Player.getSongPercentRemaining() + '%';
                    });

                    Player.loadDataUri(dataUri);

                    document.getElementById('play-button').removeAttribute('disabled');

                    //buildTracksHtml();
                    play();
                }



                loadDataUri(data);
            });
        }
    })
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


/**
 * Author: Kris Olszewski
 * CodePen: https://codepen.io/KrisOlszewski/full/wBQBNX
 */

;(function($, window, document, undefined) {

    'use strict';

    var $html = $('html');

    $html.on('click.ui.dropdown', '.js-dropdown', function(e) {
        e.preventDefault();
        $(this).toggleClass('is-open');
    });

    $html.on('click.ui.dropdown', '.js-dropdown [data-dropdown-value]', function(e) {
        e.preventDefault();
        var $item = $(this);
        var $dropdown = $item.parents('.js-dropdown');
        $dropdown.find('.js-dropdown__input').val($item.data('dropdown-value'));
        $dropdown.find('.js-dropdown__current').text($item.text());
    });

    $html.on('click.ui.dropdown', function(e) {
        var $target = $(e.target);
        if (!$target.parents().hasClass('js-dropdown')) {
            $('.js-dropdown').removeClass('is-open');
        }
    });

})(jQuery, window, document);
