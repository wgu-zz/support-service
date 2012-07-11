class CreateNotes < ActiveRecord::Migration
  def change
    create_table :notes do |t|
      t.integer   :assignment_id
      t.string    :content
      t.integer   :user_id
      t.timestamps
    end
  end
end
