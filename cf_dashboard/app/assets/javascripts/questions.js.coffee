# Place all the behaviors and hooks related to the matching controller here.
# All this logic will automatically be available in application.js.
# You can use CoffeeScript in this file: http://jashkenas.github.com/coffee-script/

$(document).ready ->
  $('#load-answers').click ->
    $('#answers').toggle()

    $(this).html(if $('#answers:visible').length > 0 then "Hide" else "Show")

    if $('#answers').html() == ''
      $('#answers').load "#{document.location}/answers"
    return false

  # $("a[rel=popover]").popover
  #   trigger: "manual"
  #   animate: false
  #   placement: "bottom"
  #   offset: 0
  #   html: true
  # .mouseover (e) ->
  #   e.preventDefault()
  #   $("a[rel=popover]").each ->
  #     $(this).popover "hide"

  #   $(this).popover "show"


  jQuery (a) ->
    a("select").each (b, c) ->
      unless a(c).data("convert") is "no"
        
        a(c).hide().wrap "<div class=\"btn-group pull-right\" id=\"select-group-" + b + "\" />"
        d = a("#select-group-" + b)
        e = (if a(c).val() then a(c).val() else " ")
        d.html "<input type=\"hidden\" value=\"" + a(c).val() + "\" name=\"" + a(c).attr("name") + "\" id=\"" + a(c).attr("id") + "\" class=\"" + a(c).attr("class") + "\" /><a class=\"btn\" href=\"javascript:;\">" + e + "</a><a class=\"btn btn-primary dropdown-toggle\" data-toggle=\"dropdown\" href=\"javascript:;\"><span class=\"caret\"></span></a><ul class=\"dropdown-menu\"></ul>"
        a(c).find("option").each (b, c) ->
          d.find(".dropdown-menu").append "<li><a href=\"javascript:;\" data-value=\"" + a(c).attr("value") + "\">" + a(c).text() + "</a></li>"
          if a(c).attr("selected")
            d.find(".btn:eq(0)").text a(c).text()
            d.find(".dropdown-menu li:eq(" + b + ")").click() 
        d.find(".dropdown-menu a").click ->
          d.find("input[type=hidden]").val(a(this).data("value")).change()
          d.find(".btn:eq(0)").text a(this).text()

window.init_load_on_scroll = () ->
  window.current_page = 1
  window.load_lock = false
  $(window).scroll ->
    load_more_questions() if $(window).scrollTop() == ($(document).height() - $(window).height()) and not load_lock

load_more_questions = () ->
  window.current_page++
  $('#loading-more').show()
  window.load_lock = true
  url = (if document.location.href.match(/\?/) then "#{document.location.href}&page=#{current_page}" else "#{document.location.href}?page=#{current_page}")
  $.get url, (data) ->
    $('#loading-more').hide()
    $('ul#open-questions, ul#questions').append data
