class AddExtraFieldsToUser < ActiveRecord::Migration
  def change
    add_column :users, :display_name, :string
    add_column :users, :profile_image, :string  
  end
end
