import React, { Component } from 'react';

class ParametersBlock extends Component {

    constructor(props) {
        super(props);

        this.state = {
            chaosSliderValue: '',
            keySignatureIsC: true
        };
    }

    aClick(event) {
        this.setState({keySignatureIsC: false});
    }

    cClick(event) {
        this.setState({keySignatureIsC: true});
    }

    onChaosSliderChange(chaosSliderValue) {
        this.setState({chaosSliderValue});
    }

    render() {
        return (
            <div>
                <h4>Parameters</h4>
                <p>Key Signature</p>
                <button className="button button-primary" onClick={this.cClick.bind(this)} id="c-button">C</button>
                <button className="button button-primary" onClick={this.aClick.bind(this)} id="a-button">a</button>
                <p>Chaos</p>
                <input type="range" onChange={event => this.onChaosSliderChange(event.target.value)} id="chaos-slider" min="0" max="100" />
            </div>
        );
    }

}

export default ParametersBlock;
