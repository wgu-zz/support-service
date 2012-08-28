class CreateIncidentUpdates < ActiveRecord::Migration
  def change
    create_table :incident_updates do |t|
      t.string :title, null: false
      t.string :update_type, null: false
      t.text :content
      t.integer :creator_user_id, null: false
      t.integer :updater_user_id, null: false
      t.references :incident, null: false

      t.timestamps
    end
  end
end
