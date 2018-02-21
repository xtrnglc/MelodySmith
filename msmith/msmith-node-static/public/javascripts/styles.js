
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

$("#num-comparisons-selector").keypress(function (evt) {
    evt.preventDefault();
});

$("#n-gram-selector").keypress(function (evt) {
    evt.preventDefault();
});

$(document).keydown(function(e) {
    var elid = $(document.activeElement).hasClass('input-number');
    console.log(e.keyCode + ' && ' + elid);
    if (e.keyCode === 8 && !elid) {
        return false;
    };
});