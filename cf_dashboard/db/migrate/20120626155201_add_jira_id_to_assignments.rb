class AddJiraIdToAssignments < ActiveRecord::Migration
  def change
  	add_column :assignments, :jira_id, :string
  end
end
