class AddZendeskIdToAssignments < ActiveRecord::Migration
  def change
    add_column :assignments, :zendesk_id, :string
  end
end
