class CreateIncidents < ActiveRecord::Migration
  def change
    create_table :incidents do |t|
      t.string :title, null: false
      t.integer :creator_user_id, null: false
      t.integer :updater_user_id, null: false
      t.boolean :upcoming, null: false
      t.boolean :resolved, null: false

      t.timestamps
    end
  end
end
