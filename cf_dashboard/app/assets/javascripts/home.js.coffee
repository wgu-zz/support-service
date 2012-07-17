# Place all the behaviors and hooks related to the matching controller here.
# All this logic will automatically be available in application.js.
# You can use CoffeeScript in this file: http://jashkenas.github.com/coffee-script/

# functions for breaking down questions in to subsets based on a property


tag_breakdown = (questions) ->
  tag_counts = {}

  for question in questions
    for tag in question["tags"]
      if not tag_counts[tag]?
        tag_counts[tag] = 1
      else
        tag_counts[tag]++

  tag_counts

status_breakdown = (questions) ->
  status_counts = {}
  status_counts.answered = 0
  status_counts.unanswered = 0
  status_counts.has_answers = 0

  for question in questions
    status_counts.answered++ if question["is_answered"] == true
    status_counts.has_answers++ if question["is_answered"] == false and question["answer_count"] > 0
    status_counts.unanswered++ if question["is_answered"] == false and question["answer_count"] == 0

  status_counts

window.do_live_stats = () ->
  # get, day, week, month data

  day = load_json '/questions/questions_for_day.json'
  week = load_json '/questions/questions_for_week.json'
  month = load_json '/questions/questions_for_month.json'

  # Tags

  day_tags = tag_breakdown day
  week_tags = tag_breakdown week
  month_tags = tag_breakdown month

  day_tags_series = create_series_from_hash day_tags
  week_tags_series = create_series_from_hash week_tags
  month_tags_series = create_series_from_hash month_tags

  draw_pie_chart "tags-today", "Today", day_tags_series
  draw_pie_chart "tags-week", "Last 7 days", week_tags_series
  draw_pie_chart "tags-month", "Last month", month_tags_series

  # Status

  day_status = create_series_from_hash status_breakdown(day)
  week_status = create_series_from_hash status_breakdown(week)
  month_status = create_series_from_hash status_breakdown(month)

  add_status_rows status_breakdown(day), status_breakdown(week), status_breakdown(month)

  draw_pie_chart "status-today", "Today", day_status, "bottom"
  draw_pie_chart "status-week", "Last 7 days", week_status, "bottom"
  draw_pie_chart "status-month", "Last month", month_status, "bottom"

add_status_rows = (day, week, month) ->

  last_row = $('#status-table tr:last')
  last_row.after("<tr><td>Month</td><td>#{month.unanswered}</td><td>#{month.has_answers}</td><td>#{month.answered}</td></tr>");
  last_row.after("<tr><td>Week</td><td>#{week.unanswered}</td><td>#{week.has_answers}</td><td>#{week.answered}</td></tr>");
  last_row.after("<tr><td>Today</td><td>#{day.unanswered}</td><td>#{day.has_answers}</td><td>#{day.answered}</td></tr>");

load_json = (url) ->
  questions = null

  $.ajax url,
    async: false
    dataType: 'json'
    success: (data) ->
      questions = data

  questions

create_series_from_hash = (hash) ->
  series = []
  for key in _.keys(hash)
    series.push [key, hash[key]]

  series

draw_pie_chart = (target, title, series, legend_pos) ->

  legend = null

  if legend_pos == "bottom"
    legend =
      align : 'center'
      layout: 'horizontal'
      verticalAlign: 'bottom'
  else
    legend =
      align : 'right'
      layout: 'vertical'
      verticalAlign: 'middle'

  chart = new Highcharts.Chart(
    chart:
      renderTo: target
      plotBackgroundColor: null
      plotBorderWidth: null
      plotShadow: false

    title:
      text: title
      align: 'left'

    legend: legend

    tooltip:
      formatter: ->
        "<b>" + @point.name + "</b>: " + @percentage.toFixed(1) + " %"

    plotOptions:
      pie:
        allowPointSelect: true
        cursor: "pointer"
        showInLegend: true
        dataLabels:
          enabled: false
        events:
          click: (event) ->
            console.log event

    series: [
      type: "pie"
      name: title
      data: series
    ]
  )