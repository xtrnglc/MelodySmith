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


$(function(){

    var UNSELECTABLE = {
        'onSelectStart'	:'return false;',
        'onMouseDown'	:'return false',
        'style'			:'-moz-user-select: none; -khtml-user-select: none; user-select: none;'
    };
    var password = [];


    init();


    // INIT
    function init(){
        enableDials(true);
        $('#btn-register').click(register);
        $('#btn-lock').click(lock);
        $('#btn-open').click(unlock);
        $('#btn-reset').click(reset);
        $('#dials').attr(UNSELECTABLE);
    }

    // REGISTOR
    function register(){
        var $nums = $('#dials li.on');
        for(var i=0; i<$nums.length; i++){
            password[i] = $nums.eq(i).text();
        }
        lock();
    }

    // LOCK
    function lock(){
        enableDials(true);
        phase('LOCKED');
        $('#light').addClass('lock');
    }

    // UNLOCK
    function unlock(){
        if(checkPass()){
            enableDials(false);
            phase('UNLOCKED');
            massage('OK');
        }else{
            massage('NG');
        }
        $('#light').removeClass('lock');
    }

    // RESET
    function reset(){
        password = [];
        enableDials(true);
        phase('RESETED');
        massage('RESETED');
        $('#light').removeClass('lock');
    }


    // CHECK PASS WORD
    function checkPass(){
        var $nums = $('#dials li.on');
        var flg = true;
        for(var i=0; i<$nums.length; i++){
            var letter = $nums.eq(i).text();
            if(letter != password[i]){
                flg = false;
            }
        }
        return flg;
    }

    // INIT DIALS
    function initDials(){
        var $dials = $('.dial');
        for(var i=0; i<$dials.length; i++){
            var $dial = $dials.eq(i);
            $dial.find('.line').css({ WebkitTransform: 'rotate(' + 0 + 'deg)'});
            $dial.find('li.on').removeClass('on');
            $dial.find('li').eq(0).addClass('on');
        }
    }

    // ENABLE DIALS
    function enableDials(_flg){
        var $dials = $('.dial');
        var $nums = $('.numset li');
        if(_flg){
            $dials.removeClass('off').bind('mousewheel', onDialWheel);
            $nums.bind('click', onNumberClick);
            initDials();
        }else{
            $dials.addClass('off').unbind('mousewheel', onDialWheel);
            $nums.removeClass('on')
                .unbind('click', onNumberClick);
        }
    }

    // CLICK NUMBER
    function onNumberClick(){
        var $list = $(this).parent().find('li');
        var $line = $(this).parent().parent().find('.knob .line');
        var num = $list.index(this);
        var deg = 36 * num;
        $line.css({ WebkitTransform: 'rotate(' + deg + 'deg)'});
        $list.removeClass('on');
        $(this).addClass('on');
    }

    // WHHEL
    function onDialWheel(_e){
        _e.preventDefault();
        var delta = _e.deltaY;
        var $on = $(this).find('.numset li.on');
        var $list = $(this).find('.numset li');
        var $line = $(this).find('.knob .line');
        var now = $list.index($on);
        var lim = $list.length - 1;
        var next, deg;
        if(delta<0){
            next = (now+1>lim) ? lim : now+1;
        }else if(delta>0){
            next = (now-1<0) ? 0 : now-1;
        }
        deg = (36*next);
        $list.removeClass('on');
        $list.eq(next).addClass('on');
        $line.css({ WebkitTransform: 'rotate(' + deg + 'deg)'});
    }

    // PHASE
    function phase(_step){
        if(_step=='LOCKED'){
            $('#ph0').stop(true,false).fadeOut(200);
            $('#ph1').stop(true,false).fadeOut(200);
            $('#ph2').stop(true,false).fadeIn(200);
        }else if(_step=='UNLOCKED'){
            $('#ph2').stop(true,false).fadeOut(200);
            $('#ph1').stop(true,false).fadeIn(200);
        }else {
            $('#ph2').stop(true,false).fadeOut(200);
            $('#ph1').stop(true,false).fadeOut(200);
            $('#ph0').stop(true,false).fadeIn(200);
        }
    }

    // MESSAGE
    function massage(_status){
        if(_status=='OK'){
            $('#msg')
                .removeClass('ng')
                .html('You did it.')
                .stop(true,false)
                .fadeIn(250)
                .delay(500)
                .fadeOut(300);
        }else if(_status=='RESETED'){
            $('#msg')
                .removeClass('ng')
                .html('I did, sir.')
                .stop(true,false)
                .fadeIn(250)
                .delay(500)
                .fadeOut(300);
        }else if(_status=='NG'){
            $('#msg')
                .addClass('ng')
                .html('Are you sure ?')
                .stop(true,false)
                .fadeIn(250)
                .delay(500)
                .fadeOut(300);
        }
    }


});
// $(document).ready(function(){
//     // to fade in on page load
//     $("body").css("display", "none");
//     $("body").fadeIn(500);
//     // to fade out before redirect
//     $('a').click(function(e){
//
//         redirect = $(this).attr('href');
//         if(redirect.includes('forge')) {
//             e.preventDefault();
//             $('body').fadeOut(400, function(){
//                 document.location.href = redirect
//             });
//         }
//     });
// })
//
