class Assignment < ActiveRecord::Base
  belongs_to :user
  has_many :notes

  attr_accessible :user_id, :question_id, :priority, :status

  def self.open_assignment_table
    table = {}
    Assignment.where("status != 'Closed'").each { |x| table[x.question_id.to_s] = x }

    table
  end
end
