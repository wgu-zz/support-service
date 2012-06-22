class Note < ActiveRecord::Base
  belongs_to :assignment

  attr_accessible :content
end
