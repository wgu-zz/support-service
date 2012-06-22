class Users::OmniauthCallbacksController < Devise::OmniauthCallbacksController
  
  def stackoverflow

    # You need to implement the method below in your model
    @user = User.find_for_stack_overflow_oauth(request.env["omniauth.auth"], current_user)
    session["oauth_creds"] = request.env["omniauth.auth"].credentials

    @user.update_attributes :display_name => request.env["omniauth.auth"].info.display_name, :profile_image => request.env["omniauth.auth"].info.image

    if @user.persisted?
      flash[:notice] = I18n.t "devise.omniauth_callbacks.success", :kind => "StackOverflow"
      sign_in_and_redirect @user, :event => :authentication
    else
      session["devise.stackoverflow_data"] = request.env["omniauth.auth"]
      redirect_to new_user_registration_url
    end
  end

  def passthru
    render :file => "#{Rails.root}/public/404.html", :status => 404, :layout => false
  end
  


end