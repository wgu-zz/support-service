class Note < ActiveRecord::Base
  belongs_to :assignment
  belongs_to :user

  attr_accessible :content, :user_id

end
