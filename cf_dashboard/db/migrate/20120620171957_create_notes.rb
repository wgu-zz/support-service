class CreateNotes < ActiveRecord::Migration
  def change
    create_table :notes do |t|
      t.integer   :assignment_id
      t.string    :content
      t.timestamps
    end
  end
end
