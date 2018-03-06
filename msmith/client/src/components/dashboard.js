import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchPosts } from '../actions';
import _ from 'lodash';
import { Link } from 'react-router-dom';

import ParamBlock from "./param_block";
import ArtistBlock from "./artist_block";
import Canvas from "./canvas";

class Dashboard extends Component {

    constructor(props) {
        super(props);

        this.state = {
            artistslider1: '50',
            artistslider2: '50',
            artistslider3: '50',
            focusOnRhythmSlider: '50',
            focusOnIntervalSlider: '50',
            ngram: '2',
            scaleval: 'cMajor',
            decisionsToCompare: '6'
        };

        // YTSearch({key: API_KEY, term: 'surfboards'}, (data) => {
        //   this.setState({ videos: data});
        // });
        //this.videoSearch('arteezy');
    }

    componentDidMount() {
        //this.props.fetchPosts();
    }

    artistSliderChange(slidervals) {
        this.setState({artistslider1: slidervals.artistslider1, artistslider2: slidervals.artistslider2, artistslider3: slidervals.artistslider3});
    }

    paramSliderChange(slidervals) {
        this.setState({focusOnRhythmSlider: slidervals.focusOnRhythmSlider, focusOnIntervalSlider: slidervals.focusOnIntervalSlider, ngram: slidervals.ngram, scaleval: slidervals.scaleval, decisionsToCompare: slidervals.decisionsToCompare});
    }

    forgeButtonPress() {
        console.log('Forge button press');
        //React.findDOMNode(this.refs.loading1).style.display = 'none';
        // this.refs.loading1.style.display = 'none';
        forge(this);
    }

    tempoSliderChange(newVal) {
        Player.pause();
        Player.setTempo(newVal);
        Player.play()
    }

    playButtonPress() {
        if(Player.isPlaying()){
            pause(this);
        } else{
            play(this);
        }
    }

    stopButtonPress() {
        stop(this);
    }

    newNote(event) {
        console.log(event);
        this.refs.canvas.style.display = 'block';

    }

    render() {
        const artistSliderChange = _.debounce((slidervals) => {this.artistSliderChange(slidervals)}, 0);
        const paramSliderChange = _.debounce((slidervals) => {this.paramSliderChange(slidervals)}, 0);
        const forgeButtonPress = _.debounce(() => {this.forgeButtonPress()}, 0);
        const tempoSliderChange = _.debounce((newVal) => {this.tempoSliderChange(newVal)}, 0);
        const playButtonPress = _.debounce(() => {this.playButtonPress()}, 0);
        const stopButtonPress = _.debounce(() => {this.stopButtonPress()}, 0);

        return (
            <div>
                <h1>MelodySmith</h1>
                <div className="row">
                    <div className="four columns">
                        <ArtistBlock onSliderChange={artistSliderChange}/>
                    </div>

                    <div className="eight columns">
                        <ParamBlock onSliderChange={paramSliderChange}/>
                    </div>

                    <button className="button button-primary" onClick={ () => {forgeButtonPress()}} id="forge-button">Forge</button>

                    <p id="loading" ref="loading">Hit forge when you are ready to listen to some new music!</p>
                    <div id="canvas" ref='canvas' style={{display:'none'}}>
                        canvas
                    </div>

                    <Canvas ref='canvas1' style={{display:'none'}}/>

                    <div id="loadingcube" ref='loadingCube' className="sk-folding-cube" style={{display:'none'}}>
                        <div className="sk-cube1 sk-cube"></div>
                        <div className="sk-cube2 sk-cube"></div>
                        <div className="sk-cube4 sk-cube"></div>
                        <div className="sk-cube3 sk-cube"></div>
                    </div>

                    <div id="play-bar-button" ref='playBarButton' style={{display: 'none', border:'1px solid #ccc', marginBottom:'12px',background:'#f7f7f7'}}>
                        <div id="play-bar" ref='playBar' style={{height:'20px',background:'#33C3F0',width:'0%'}}></div>
                    </div>

                    <div id="music-controls" ref='musicControls' style={{display: 'none'}}>

                        <button className="button button-primary" ref='playButton' id="play-button" onClick={ () => {playButtonPress()}}>Play</button>
                        <button className="button" id="stop-button" ref='stopButton' onClick={ () => {stopButtonPress()}}>Stop</button>

                        <div className="c-dropdown js-dropdown">
                            <input type="hidden" name="Framework" id="instrument-dropdown" className="js-dropdown__input" />
                                <span className="c-button c-button--dropdown js-dropdown__current">Grand Piano</span>
                                <ul className="c-dropdown__list">
                                    <li className="c-dropdown__item" data-dropdown-value="acousticGuitar" onClick={ () => {changeInstrument('acousticGuitar', this)}}>Acoustic Guitar</li>
                                    <li className="c-dropdown__item" data-dropdown-value="musicBox" onClick={ () => {changeInstrument('musicBox', this)}}>Music Box</li>
                                    <li className="c-dropdown__item" data-dropdown-value="grandPiano" onClick={ () => {changeInstrument('grandPiano', this)}}>Grand Piano</li>
                                    <li className="c-dropdown__item" data-dropdown-value="electricGuitar" onClick={ () => {changeInstrument('electricGuitar', this)}}>Electric Guitar</li>
                                </ul>
                        </div>
                    </div>
                    <p id="tempo-div" ref='tempoDiv' style={{display: 'none'}}>

                        Tempo: <span ref='tempoDisplay' id="tempo-display"></span> bpm<br />
                        <input type="range" value={this.state.term} onChange={event => tempoSliderChange(event.target.value)} min="50" max="200" />
                            <br />
                    </p>

                    <div id="events" className="well"></div>

                </div>
            </div>
        );
    }
}

function mapStateToProps(state) {
    return { posts: state.posts };
}

export default connect(mapStateToProps, { fetchPosts })(Dashboard);
