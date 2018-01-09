Rails.application.routes.draw do
  root 'welcome#index'
  get 'forge', to: 'forge#new'
  get 'guild', to: 'guild#forum'
  get 'store', to: 'store#store_home'
  post 'upload', to: 'forge#upload'


  devise_for :users
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end
