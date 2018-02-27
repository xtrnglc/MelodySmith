import React, { Component } from 'react';

class ParamBlock extends Component {
    constructor(props) {
        super(props);

        this.state = {
            focusOnRhythmSlider: '50',
            focusOnIntervalSlider: '50',
            ngram: '2',
            scaleval: 'cMajor',
            decisionsToCompare: '6'
        };
    }

    onInputChangefocusOnRhythmSlider(focusOnRhythmSlider) {
        this.setState({focusOnRhythmSlider}, () => {
            this.props.onSliderChange(this.state);
        });
    }
    onInputChangefocusOnIntervalSlider(focusOnIntervalSlider) {
        this.setState({focusOnIntervalSlider}, () => {
            this.props.onSliderChange(this.state);
        });
    }
    onInputChangengram(ngram) {
        this.setState({ngram}, () => {
            this.props.onSliderChange(this.state);
        });
    }

    onInputChangeScale(scaleval) {
        this.setState({scaleval}, () => {
            this.props.onSliderChange(this.state);
        });
    }

    onInputDecisionsToCompare(decisionsToCompare) {
        this.setState({decisionsToCompare}, () => {
            this.props.onSliderChange(this.state);
        });
    }

    render() {
        return (
            <div>
                <h4>Parameters</h4>
                <div className="four columns">

                    <div className="tooltip">Focus on Rhythm
                        <span className="tooltiptext">How important are rhythymic details?</span>
                    </div>
                    <p></p>
                    <input type="range" id="durationOfNotes-slider" min="0" max="100" value={this.state.term} onChange={event => this.onInputChangefocusOnRhythmSlider(event.target.value)} />
                        <div className="tooltip">N-gram(s)
                            <span className="tooltiptext">What is the length of phrases analyzed?</span>
                        </div>
                        <p></p>
                        <div className="input-group input-number-group">
                            <input className="input-number" id="n-gram-selector" type="number" value="2" min="2" max="10" value={this.state.term} onChange={event => this.onInputChangengram(event.target.value)} />

                        </div>
                        <div className="tooltip">Decisions to Compare
                            <span className="tooltiptext">How important is the current context?</span>
                        </div>
                        <p></p>
                        {/*<radioform action="#">*/}
                            {/*<div>*/}
                                {/*<fieldset>*/}
                                    {/*<input type="radio" name="radio-choice"  id="radio-choice-8" value="choice-8" />*/}
                                    {/*<label for="radio-choice-8">2</label>*/}
                                    {/*<input type="radio" name="radio-choice" id="radio-choice-7" value="choice-7" />*/}
                                    {/*<label for="radio-choice-7">3</label>*/}
                                    {/*<input type="radio" name="radio-choice" id="radio-choice-1" value="choice-1" />*/}
                                    {/*<label for="radio-choice-1">4</label>*/}
                                    {/*<input type="radio" name="radio-choice" id="radio-choice-2" value="choice-2" />*/}
                                    {/*<label for="radio-choice-2">5</label>*/}
                                    {/*<input type="radio" name="radio-choice" id="radio-choice-3" value="choice-3" />*/}
                                    {/*<label for="radio-choice-3">6</label>*/}
                                    {/*<input type="radio" name="radio-choice"  id="radio-choice-4" value="choice-4" />*/}
                                    {/*<label for="radio-choice-4">7</label>*/}
                                    {/*<input type="radio" name="radio-choice"  id="radio-choice-5" value="choice-5" />*/}
                                    {/*<label for="radio-choice-5">8</label>*/}
                                    {/*<input type="radio" name="radio-choice" id="radio-choice-6" value="choice-6" />*/}
                                    {/*<label for="radio-choice-6">9</label>*/}
                                    {/*<input type="radio" name="radio-choice"  id="radio-choice-9" value="choice-9" />*/}
                                    {/*<label for="radio-choice-9">10</label>*/}
                                    {/*<div class="dial">▲</div>*/}
                                {/*</fieldset>*/}
                            {/*</div>*/}
                        {/*</radioform>*/}
                </div>

                <div className="four columns">

                    <div className="tooltip">Focus on Intervals
                        <span className="tooltiptext">How important are melodic details?</span>
                    </div>
                    <p></p>
                    <input type="range" id="intervalOfNote-slider" min="0" max="100" value={this.state.term} onChange={event => this.onInputChangefocusOnIntervalSlider(event.target.value)} />


                        <p>Scale</p>
                        <div className="c-dropdown js-dropdown">
                            <input type="hidden" name="Framework" id="keysigdropdown" className="js-dropdown__input" />
                                <span className="c-button c-button--dropdown js-dropdown__current">G Chromatic</span>
                                <ul className="c-dropdown__list">
                                    <li className="c-dropdown__item" data-dropdown-value="cGypsy" value={this.state.term} onClick={event => this.onInputChangeScale('cGypsy')}>c Gypsy</li>
                                    <li className="c-dropdown__item" data-dropdown-value="aMinor" value={this.state.term} onClick={event => this.onInputChangeScale('aMinor')}>a Minor</li>
                                    <li className="c-dropdown__item" data-dropdown-value="DBlues" value={this.state.term} onClick={event => this.onInputChangeScale('DBlues')}>D Blues</li>
                                    <li className="c-dropdown__item" data-dropdown-value="fHarmonic" value={this.state.term} onClick={event => this.onInputChangeScale('fHarmonic')}>f Harmonic</li>
                                    <li className="c-dropdown__item" data-dropdown-value="GChromatic" value={this.state.term} onClick={event => this.onInputChangeScale('GChromatic')}>G Chromatic</li>
                                    <li className="c-dropdown__item" data-dropdown-value="cMajor" value={this.state.term} onClick={event => this.onInputChangeScale('cMajor')}>C Major</li>
                                </ul>
                        </div>
                </div>

            </div>
        );
    }


}

export default ParamBlock;
