CfDashboard::Application.routes.draw do
  #devise_for :users, :controllers => { :omniauth_callbacks => "users/omniauth_callbacks" }

  devise_for :users, :controllers => { :omniauth_callbacks => "users/omniauth_callbacks" } do
    # get '/users/auth/:provider' => 'users/omniauth_callbacks#passthru'
  end

  match 'profile' => 'profile#index'
  match 'profile/answers' => 'profile#answers'

  root :to => "home#index"

  resources :questions do
    
    collection do
      get 'my' 
      get 'my_open'
      get 'all_open'
      get 'unassigned'
      get 'unanswered'
      get 'search'

      get 'more'
      get 'questions_for_day'
      get 'questions_for_week'
      get 'questions_for_month'
    end

    resources :answers
    resources :assignments
  end
  
  resources :answers
  
end
