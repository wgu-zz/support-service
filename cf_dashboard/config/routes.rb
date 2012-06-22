CfDashboard::Application.routes.draw do
  #devise_for :users, :controllers => { :omniauth_callbacks => "users/omniauth_callbacks" }

  devise_for :users, :controllers => { :omniauth_callbacks => "users/omniauth_callbacks" } do
    # get '/users/auth/:provider' => 'users/omniauth_callbacks#passthru'
  end

  match 'profile' => 'profile#index'
  match 'profile/answers' => 'profile#answers'

  root :to => "home#index"

  resources :questions do
    resources :answers
    resources :assignments
  end
  
  resources :answers
  
end
