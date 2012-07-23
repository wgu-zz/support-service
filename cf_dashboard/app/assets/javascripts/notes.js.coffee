# Place all the behaviors and hooks related to the matching controller here.
# All this logic will automatically be available in application.js.
# You can use CoffeeScript in this file: http://jashkenas.github.com/coffee-script/

$(document).ready ->
  $('.note .edit-switch a').click ->
    $(this).parent().hide()

    $(this).parent().siblings('.note-edit').show()
    $(this).parent().parent().siblings().hide()

    console.log "click"

    false