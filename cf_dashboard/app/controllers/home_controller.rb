class HomeController < ApplicationController
  before_filter :authenticate_user!

  def index
    @env = session["oauth_creds"].token
    
    c = StackOverflowClient.new session["oauth_creds"].token
    @me = c.me_answers?

  end
end
