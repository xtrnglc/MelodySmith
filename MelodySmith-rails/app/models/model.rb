class Model < ApplicationRecord
  has_many :training_models
  belongs_to :user
end