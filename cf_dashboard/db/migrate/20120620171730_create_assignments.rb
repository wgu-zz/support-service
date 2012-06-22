class CreateAssignments < ActiveRecord::Migration
  def change
    create_table :assignments do |t|
      t.integer   :question_id
      t.integer   :user_id
      t.string    :priority
      t.string    :status
      t.timestamps
    end
  end
end
