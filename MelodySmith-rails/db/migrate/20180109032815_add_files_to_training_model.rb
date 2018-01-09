class AddFilesToTrainingModel < ActiveRecord::Migration[5.1]
  def change
    def up
      add_attachment :training_models, :file
    end
  end
end