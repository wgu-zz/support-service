class QuestionsController < ApplicationController
  before_filter :authenticate_user!

  def index
    @assigned_questions = current_user.open_assigned_questions
    question_ids = @assigned_questions.keys.join ";"
    
    @all_assigned_questions = Assignment.open_assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    so_client = StackOverflowClient.new session["oauth_creds"].token

    @questions = so_client.instance_eval("questions_ids? :filter => 'withbody', :ids=>'#{question_ids}'")["items"]
    @all_questions = so_client.instance_eval("questions? :tagged => 'cloudfoundry', :filter => 'withbody'")["items"]

    respond_to do |format|
      format.html 
      format.json { render :json => @questions }
    end

  end

  def show
    
    so_client = StackOverflowClient.new session["oauth_creds"].token
    @question = so_client.instance_eval("questions_#{params[:id]}? :filter => 'withbody'")["items"].first
 
    # find assignment
    @assignment = Assignment.find_by_question_id(params[:id]) || Assignment.new
    @assignment.notes.build

    respond_to do |format|
      format.html
      format.json { render :json => @question }
    end

  end

end
