class CreateTrainingModels < ActiveRecord::Migration[5.1]
  def change
    create_table :training_models do |t|
      t.string :name
      t.string :artist

      t.timestamps
    end
  end
end
