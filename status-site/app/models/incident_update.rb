class IncidentUpdate < ActiveRecord::Base
  attr_accessible :content, :title, :update_type
  belongs_to :incident
  belongs_to :creator, class_name: "User", foreign_key: "creator_user_id"
  belongs_to :updater, class_name: "User", foreign_key: "updater_user_id"
end
