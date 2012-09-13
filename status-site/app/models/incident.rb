class Incident < ActiveRecord::Base
  # string   "title",           :null => false
  # integer  "creator_user_id", :null => false
  # integer  "updater_user_id", :null => false
  # boolean  "upcoming",        :null => false, :default => false
  # boolean  "resolved",        :null => false, :default => false
  # datetime "created_at",      :null => false
  # datetime "updated_at",      :null => false

  attr_accessible :title, :resolved, :upcoming
  has_many :incident_updates
  belongs_to :creator, class_name: "User", foreign_key: "creator_user_id"
  belongs_to :updater, class_name: "User", foreign_key: "updater_user_id"

  validates :title, presence: true
  validates :resolved, inclusion: { :in => [true,false] }
  validates :upcoming, inclusion: { :in => [true,false] }
  validates :creator_user_id, presence: true
  validates :updater_user_id, presence: true

  after_initialize :set_defaults

  private

    def set_defaults
      self.resolved ||= false
      self.upcoming ||= false
    end
end
