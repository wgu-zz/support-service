class AnswersController < ApplicationController

  def show
    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]
    @answer = so_client.instance_eval "answers_#{params[:id]}?"

    respond_to do |format|
      format.html { render :layout => false }
      format.json { render :json => @answer }
    end
  end

  def index
    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]
    @answers = so_client.instance_eval("questions_#{params[:question_id]}_answers?(:filter => 'withbody')")["items"]

    respond_to do |format|
      format.html { render :layout => false }
      format.json { render :json => @answers }
    end
  end

end
