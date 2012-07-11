class AssignmentsController < ApplicationController
  
  before_filter :authenticate_user!

  def update
    params[:assignment][:notes_attributes].first[1][:user_id] = current_user.id if not params[:assignment][:notes_attributes].nil?

    assignment = Assignment.find params[:id]
    assignment.update_attributes params[:assignment]

    redirect_to "/questions"
  end

  def create

    assignment = Assignment.new params[:assignment]
    assignment.question_id = params[:question_id]

    assignment.save

    redirect_to "/questions/#{params[:question_id]}"
  end


end
