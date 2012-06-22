class AssignmentsController < ApplicationController
  
  def update
    assignment = Assignment.find params[:id]
    assignment.update_attributes params[:assignment]

    redirect_to "/questions/#{params[:question_id]}"
  end

  def create
    assignment = Assignment.new params[:assignment]
    assignment.question_id = params[:question_id]

    assignment.save

    redirect_to "/questions/#{params[:question_id]}"
  end

end
