class Incident < ActiveRecord::Base
  attr_accessible :title, :resolved, :upcoming
  has_many :incident_updates
  belongs_to :creator, class_name: "User", foreign_key: "creator_user_id"
  belongs_to :updater, class_name: "User", foreign_key: "updater_user_id"
end
