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


//tabs

$(function() {
    $('ul.tab-nav li a.button').click(function() {
        var href = $(this).attr('href');

        $('li a.active.button', $(this).parent().parent()).removeClass('active');
        $(this).addClass('active');
        console.log(href);
        $('.tab-pane.active', $(href).parent()).removeClass('active');
        $(href).addClass('active');
        if(href === '#notesVisualizer') {
            console.log('true');
            document.getElementById('canvas').style.display = 'block';
        } else {
            document.getElementById('canvas').style.display = 'none';
        }


        return false;
    });
});

(function() {
    var selectors = {
        nav: '[data-features-nav]',
        tabs: '[data-features-tabs]',
        active: '.__active'
    }
    var classes = {
        active: '__active'
    }
    $('a', selectors.nav).on('click', function() {
        let $this = $(this)[0];
        if($this.toString().includes('forge')) {
            return true;
        } else {
            $(selectors.active, selectors.nav).removeClass(classes.active);
            $($this).addClass(classes.active);
            $('div', selectors.tabs).removeClass(classes.active);
            $($this.hash, selectors.tabs).addClass(classes.active);
            return false
        }

    });
}());

$(".btn-with-icon").on("click", function() {
    $(".wave-anim").addClass('visible').one("webkitAnimationEnd mozAnimationEnd MSAnimationEnd", function() {
        $(".wave-anim").removeClass('visible');
    });
});




