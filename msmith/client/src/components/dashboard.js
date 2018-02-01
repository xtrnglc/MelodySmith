import React, { Component } from 'react';
import ArtistBlock from './artists_block';
import ParamsBlock from './parameters_block';
import Playbar from './playbar';
import { func, play, forge, cbutton, abutton, stop, pause, changeTempo } from '../../javascripts/app';

class Dashboard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            chaosSliderValue: '',
            keySignatureIsC: true,
            artist1slider: '',
            artist2slider: '',
            artist3slider: ''
        };

    }

    stopClick(event) {
        console.log('stop clicked');
        stop();
    }

    playClick(event) {
        console.log('play clicked');
        play();
    }

    forgeClick(event) {
        console.log('forge click');
        forge();
    }

    onArtist1SliderChange(artist1slider) {
        this.setState({artist1slider});
    }

    onArtist2SliderChange(artist2slider) {
        this.setState({artist2slider});
    }

    onArtist3SliderChange(artist3slider) {
        this.setState({artist3slider});
    }

    aClick(event) {
        this.setState({keySignatureIsC: false});
        abutton();
    }

    cClick(event) {
        this.setState({keySignatureIsC: true});
        cbutton();
    }

    onChaosSliderChange(chaosSliderValue) {
        this.setState({chaosSliderValue});
    }


    render() {
        return (
            <div>
                <h1>&#9836; MelodySmith</h1>
                <div id="container2">
                    <div className="row double">

                        <div className="column-2 left">
                            <div>
                                <h4>Artists</h4>
                                <p>Backstreet Boys</p>
                                <input type="range" onChange={event => this.onArtist1SliderChange(event.target.value)} id="artist-slider1" min="0" max="100" />
                                <p>Katy Perry</p>
                                <input type="range" onChange={event => this.onArtist2SliderChange(event.target.value)} id="artist-slider2" min="0" max="100" />
                                <p>Beethoven</p>
                                <input type="range" onChange={event => this.onArtist3SliderChange(event.target.value)} id="artist-slider3" min="0" max="100" />
                            </div>

                        </div>
                        <div className="column-2 right">
                            <div>
                                <h4>Parameters</h4>
                                <p>Key Signature</p>
                                <button className="button button-primary" onClick={this.cClick.bind(this)} id="c-button">C</button>
                                <button className="button button-primary" onClick={this.aClick.bind(this)} id="a-button">a</button>
                                <p>Chaos</p>
                                <input type="range" onChange={event => this.onChaosSliderChange(event.target.value)} id="chaos-slider" min="0" max="100" />
                            </div>
                        </div>
                    </div>
                </div>
                <hr />
                <div>
                    <p>Hit forge when you are ready to listen to some new music!</p>
                    <div className="playbarborder">
                        <div className="playbar" id='play-bar'>
                        </div>
                    </div>
                    <p>
                        <button className="button button-primary" onClick={this.playClick.bind(this)} id="play-button" >Play</button>
                        <button className="button" onClick={this.stopClick.bind(this)}>Stop</button>
                        <button className="button button-primary" onClick={this.forgeClick.bind(this)} id="forge-button">Forge</button>
                    </p>
                    <p>
                        Tempo: <span id="tempo-display"></span> bpm<br />
                        <input type="range" min="50" max="200" />
                        <br />
                    </p>
                    <div id="events" className="well"></div>
                </div>
            </div>

        );
    }

}

export default Dashboard;
