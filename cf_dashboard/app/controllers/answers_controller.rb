class AnswersController < ApplicationController
  before_filter :authenticate_user!

  def show
    so_client = StackOverflowClient.new session["oauth_creds"].token
    @answer = so_client.instance_eval "answers_#{params[:id]}?"

    respond_to do |format|
      format.html { render :layout => false }
      format.json { render :json => @answer }
    end
  end

  def index
    so_client = StackOverflowClient.new session["oauth_creds"].token
    @answers = so_client.instance_eval("questions_#{params[:question_id]}_answers?(:filter => 'withbody')")["items"]

    respond_to do |format|
      format.html { render :layout => false }
      format.json { render :json => @answers }
    end
  end

end
