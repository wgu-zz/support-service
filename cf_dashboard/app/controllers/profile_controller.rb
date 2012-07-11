class ProfileController < ApplicationController

  before_filter :authenticate_user!

  def index
    @me = current_user

    so_client = StackOverflowClient.new session["oauth_creds"].token
    @me_so_info = so_client.me?["items"][0]
    
  end

  def answers
    so_client = StackOverflowClient.new session["oauth_creds"].token
    @answers = so_client.me_answers?(:filter => 'withbody')["items"]

    respond_to do |format|
      format.html { render :layout => false }
      format.json { render :json => @answers }
    end
  end
end
