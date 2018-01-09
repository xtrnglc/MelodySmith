class CreateModels < ActiveRecord::Migration[5.1]
  def change
    create_table :models do |t|
      t.string :username
      t.string :training_models_json
      t.string :parameters_json

      t.timestamps
    end
  end
end
