class Assignment < ActiveRecord::Base
  belongs_to :user
  has_many :notes

  attr_accessible :user_id, :question_id, :priority, :status, :jira_id, :zendesk_id, :notes_attributes

  accepts_nested_attributes_for :notes, :reject_if => proc { |attributes| attributes['content'].blank? }

  def self.open_assignment_table
    table = {}
    Assignment.where("status != 'Closed'").each { |x| table[x.question_id.to_s] = x }

    table
  end

  def self.assignment_table
    table = {}
    Assignment.all.each { |x| table[x.question_id.to_s] = x }

    table
  end

end
