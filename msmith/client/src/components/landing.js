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

    }

    componentDidMount() {
        //this.props.fetchPosts();
    }

    changeNode(val) {
        stop(this);
    }


    render() {
        const changeNode = _.debounce(() => {this.changeNode(val)}, 0);
        return (
            <div>
                <section id="features" className="features">
                    <div className="box foo">
                        <h3 className="features-title">MelodySmith</h3>
                        <div className="features-content">
                            <div data-features-tabs className="features-content-col">
                                <div id="feature-1" className="features-textblock animated fadeIn __active">
                                    <h2> A Music Composition Application</h2>
                                    <p>For musicians, ideas are currency. After so many years, and so many composers, however, those valuable unique ideas are hard and harder to come by.  </p> <p>Wouldn’t it be great if you could generate hundreds of ideas automatically
                                    The MelodySmith is an algorithmic composer that learns its style from the artists and genres you like. </p> <p>Using machine learning, the MelodySmith will digest a corpus of MIDI files and generate a melody that best encompasses the styles of each song it’s trained on. Users will be able to “combine” artists such as Bach and Katy Perry and hear what a fusion of their styles would sound like.
                                </p>
                                </div>
                                <div id="feature-2" className="features-textblock animated fadeIn">
                                    <h2>Association Network</h2>
                                    <p>Network: The backbone of the MelodySmith is known as an association network. </p><p>This network can be thought of as a big web of musical events. Every musical event that is observed in the music you provide to the MelodySmith is put into this web, and subsequently linked to every other event in the web.  </p><p>Links between musical events are all given a score based on things like the interval between the events, whether or not the events were played by the same artist or in the same song, and how similar the tempo of the events are. </p><p> By looking at the highest scores associated with any given musical event, the MelodySmith is able to decide which event is the most likely to occur next!</p>
                                </div>
                                <div id="feature-3" className="features-textblock animated fadeIn">
                                    <h2>VST Plug-in</h2>
                                    <p>The main interface for the MelodySmith is a VST (Virtual Studio Technology) plug in
                                        which would be compatible with any Digital Audio Workstation (Ableton Live, Logic Pro, Cubase…). </p> <p>The plugin could be pulled up alongside all of the other tools a producer is used to, and will feed MIDI commands directly into the instrument tracks. Music producers would use the MelodySmith to incorporate melodies into their songs, creating a stepping off point for a new song.
                                </p>
                                </div>
                                <div id="feature-4" className="features-textblock animated fadeIn">
                                    <h2>N-Grams</h2>
                                    <p>In natural language processing, N-grams are combinations of words that appear next to one another. </p><p>For example, the sentence "I am here." could be represented as 1-grams ("I", "am" and "here."), 2-grams ("I am" and "am here.") and 3-grams ("I am here."). N-grams can be used in music as well though! </p><p>In music, N-grams are combinations of musical events which occur next to one another. For example, a song which plays the notes A then C then E could be split into 2-grams ("A C" and "C E"). By keeping a count of the number of times every musical N-gram occurs, we are able to build a probability distribution which can tell the MelodySmith how likely a combination of notes is to occur.</p><p> This is a very powerful ability when it comes to composing new music!</p>
                                </div>
                                <div id="feature-5" className="features-textblock animated fadeIn">
                                    <h2>audioMIDIum</h2>
                                    <p>We are a team of four Computer Science students at the University of Utah. This is the culmination of our years here at the U.</p>
                                    <ul>
                                        <li>Alex Blackburn</li>
                                        <li>Trung Le</li>
                                        <li>Dan Mattheiss</li>
                                        <li>Steven Sun</li>
                                    </ul>
                                </div>
                                <div id="feature-6" className="features-textblock animated fadeIn">
                                    <h2>Web Demo App</h2>
                                    <p>A demo version of the MelodySmith would be available as a website for users who just want to hear the fusion of two or more musicians, and don’t have a use for the MIDI output.
                                        The website would allow users to experiment with different models to hear what the tool is capable of. If they wanted to train the MelodySmith on their own MIDI corpus, however, users would need to obtain the plugin version of the software.</p>
                                    <p>Click on the music button to demo the application</p>
                                </div>
                            </div>
                            <div className="features-content-col">
                                <div data-features-nav className="features-graph">
                                    <div className="button-holder">
                                        <a href="#feature-1" className="icon-features-1 btn-with-icon __active"><span className="sq-bt-label label-top-left">Composition</span></a>
                                        <a href="#feature-2" className="icon-features-2 btn-with-icon"><span className="sq-bt-label label-top">Network </span></a>
                                        <a href="#feature-3" className="icon-features-3 btn-with-icon"><span className="sq-bt-label label-top-right" style={{textAlign: 'center'}} >VST</span></a></div>
                                    <div className="animation-holder">
                            <span className="flash-oval">
                            <a href="/forge">
                                <img src="style/note.png" alt="pulse" />
                            </a>
                           <div className="wave hidden wave-anim"></div>
                           <div className="wave2 hidden wave-anim"></div>
                           <div className="wave3 hidden wave-anim"></div>
                           <div className="wave4 hidden wave-anim"></div>
                        </span>
                                    </div>
                                    <div className="button-holder">
                                        <a href="#feature-4" className="icon-features-4 btn-with-icon"><span className="sq-bt-label label-bottom-left">N-Grams</span></a>
                                        <a href="#feature-5" className="icon-features-5 btn-with-icon"><span className="sq-bt-label label-bottom">audioMIDIum</span></a>
                                        <a href="#feature-6" className="icon-features-6 btn-with-icon"><span className="sq-bt-label label-bottom-right">Web Demo</span></a></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

                <footer style={{position: 'absolute'}}>
                    by <a href="https://melodysmith.herokuapp.com/" target="_blank">audioMIDIum</a>
                </footer>
            </div>
        );
    }
}

function mapStateToProps(state) {
    return { posts: state.posts };
}

export default connect(mapStateToProps, { fetchPosts })(Dashboard);
