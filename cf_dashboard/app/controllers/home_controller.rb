class HomeController < ApplicationController
  # before_filter :authenticate_user!

  def index
    @users = User.includes :assignments
    
  end

end
